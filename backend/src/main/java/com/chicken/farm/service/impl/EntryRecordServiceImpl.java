package com.chicken.farm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chicken.farm.dto.EntryRecordDTO;
import com.chicken.farm.entity.EntryRecord;
import com.chicken.farm.mapper.EntryRecordMapper;
import com.chicken.farm.service.EntryRecordService;
import com.chicken.farm.vo.EntryRecordVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class EntryRecordServiceImpl extends ServiceImpl<EntryRecordMapper, EntryRecord> implements EntryRecordService {

    @Override
    public EntryRecordVO saveOrUpdateEntry(EntryRecordDTO dto) {
        EntryRecord existing = this.getCurrentEntryEntity();
        
        BigDecimal totalPrice = dto.getPricePerChick().multiply(BigDecimal.valueOf(dto.getTotalChicks()));
        
        EntryRecord record;
        if (existing != null) {
            record = existing;
            record.setTotalChicks(dto.getTotalChicks());
            record.setEntryDate(dto.getEntryDate());
            record.setBreed(dto.getBreed());
            record.setPricePerChick(dto.getPricePerChick());
            record.setTotalPrice(totalPrice);
            record.setUpdateTime(java.time.LocalDateTime.now());
            this.updateById(record);
        } else {
            record = new EntryRecord();
            record.setTotalChicks(dto.getTotalChicks());
            record.setEntryDate(dto.getEntryDate());
            record.setBreed(dto.getBreed());
            record.setPricePerChick(dto.getPricePerChick());
            record.setTotalPrice(totalPrice);
            record.setCreateTime(java.time.LocalDateTime.now());
            record.setUpdateTime(java.time.LocalDateTime.now());
            this.save(record);
        }
        
        return convertToVO(record);
    }

    @Override
    public EntryRecordVO getCurrentEntry() {
        EntryRecord record = this.getCurrentEntryEntity();
        if (record == null) {
            return null;
        }
        return convertToVO(record);
    }

    private EntryRecord getCurrentEntryEntity() {
        return this.getOne(new LambdaQueryWrapper<EntryRecord>()
                .orderByDesc(EntryRecord::getId)
                .last("LIMIT 1"));
    }

    private EntryRecordVO convertToVO(EntryRecord record) {
        EntryRecordVO vo = new EntryRecordVO();
        BeanUtils.copyProperties(record, vo);
        
        int breedingDays = (int) ChronoUnit.DAYS.between(record.getEntryDate(), LocalDate.now());
        vo.setBreedingDays(breedingDays);
        
        return vo;
    }
}
