package com.chicken.farm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chicken.farm.dto.BreedingStatusDTO;
import com.chicken.farm.entity.BreedingStatus;
import com.chicken.farm.entity.EntryRecord;
import com.chicken.farm.mapper.BreedingStatusMapper;
import com.chicken.farm.mapper.EntryRecordMapper;
import com.chicken.farm.service.BreedingStatusService;
import com.chicken.farm.vo.BreedingStatusVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BreedingStatusServiceImpl extends ServiceImpl<BreedingStatusMapper, BreedingStatus> implements BreedingStatusService {

    @Autowired
    private EntryRecordMapper entryRecordMapper;

    @Override
    public BreedingStatusVO saveOrUpdateStatus(BreedingStatusDTO dto) {
        EntryRecord entryRecord = getEntryRecord();
        if (entryRecord == null) {
            throw new RuntimeException("请先录入入栏详情");
        }

        BreedingStatus existing = this.getOne(new LambdaQueryWrapper<BreedingStatus>()
                .eq(BreedingStatus::getRecordDate, dto.getRecordDate()));

        int survivalCount = calculateSurvivalCount(entryRecord.getTotalChicks(), dto);

        BreedingStatus status;
        if (existing != null) {
            status = existing;
            status.setAvgWeight(dto.getAvgWeight());
            status.setDeadCount(dto.getDeadCount());
            status.setSurvivalCount(survivalCount);
            status.setUpdateTime(java.time.LocalDateTime.now());
            this.updateById(status);
        } else {
            status = new BreedingStatus();
            status.setRecordDate(dto.getRecordDate());
            status.setAvgWeight(dto.getAvgWeight());
            status.setDeadCount(dto.getDeadCount());
            status.setSurvivalCount(survivalCount);
            status.setCreateTime(java.time.LocalDateTime.now());
            status.setUpdateTime(java.time.LocalDateTime.now());
            this.save(status);
        }

        return convertToVO(status);
    }

    @Override
    public List<BreedingStatusVO> getAllStatus() {
        List<BreedingStatus> list = this.list(new LambdaQueryWrapper<BreedingStatus>()
                .orderByDesc(BreedingStatus::getRecordDate));
        return list.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public BreedingStatusVO getLatestStatus() {
        BreedingStatus status = this.getOne(new LambdaQueryWrapper<BreedingStatus>()
                .orderByDesc(BreedingStatus::getRecordDate)
                .last("LIMIT 1"));
        return status != null ? convertToVO(status) : null;
    }

    private EntryRecord getEntryRecord() {
        return entryRecordMapper.selectOne(new LambdaQueryWrapper<EntryRecord>()
                .orderByDesc(EntryRecord::getId)
                .last("LIMIT 1"));
    }

    private int calculateSurvivalCount(Integer totalChicks, BreedingStatusDTO dto) {
        Integer totalDeadBefore = this.baseMapper.getTotalDeadCount();
        if (totalDeadBefore == null) {
            totalDeadBefore = 0;
        }
        return totalChicks - totalDeadBefore - dto.getDeadCount();
    }

    private BreedingStatusVO convertToVO(BreedingStatus status) {
        BreedingStatusVO vo = new BreedingStatusVO();
        BeanUtils.copyProperties(status, vo);
        return vo;
    }
}
