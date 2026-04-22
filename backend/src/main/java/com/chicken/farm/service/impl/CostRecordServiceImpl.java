package com.chicken.farm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chicken.farm.common.UserContext;
import com.chicken.farm.dto.CostRecordDTO;
import com.chicken.farm.entity.CostRecord;
import com.chicken.farm.mapper.CostRecordMapper;
import com.chicken.farm.service.CostRecordService;
import com.chicken.farm.vo.CostCategoryVO;
import com.chicken.farm.vo.CostRecordVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CostRecordServiceImpl extends ServiceImpl<CostRecordMapper, CostRecord> implements CostRecordService {

    private static final List<String> CATEGORIES = Arrays.asList("medicine", "feed", "other");
    private static final Map<String, String> CATEGORY_NAMES = Map.of(
            "medicine", "药品",
            "feed", "饲料",
            "other", "其他"
    );

    @Override
    public CostRecordVO saveCost(CostRecordDTO dto) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }
        
        if (!CATEGORIES.contains(dto.getCategory())) {
            throw new RuntimeException("无效的类别");
        }
        
        CostRecord record = new CostRecord();
        record.setUserId(userId);
        record.setRecordDate(dto.getRecordDate());
        record.setCategory(dto.getCategory());
        record.setAmount(dto.getAmount());
        record.setRemark(dto.getRemark());
        record.setCreateTime(java.time.LocalDateTime.now());
        record.setUpdateTime(java.time.LocalDateTime.now());
        
        this.save(record);
        
        return convertToVO(record);
    }

    @Override
    public List<CostCategoryVO> getDailySummary(String date) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return Collections.emptyList();
        }
        LocalDate localDate = LocalDate.parse(date);
        List<Map<String, Object>> results = this.baseMapper.getDailySummary(userId, localDate);
        return buildCategoryVO(results);
    }

    @Override
    public List<CostCategoryVO> getMonthlySummary(int year, int month) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return Collections.emptyList();
        }
        List<Map<String, Object>> results = this.baseMapper.getMonthlySummary(userId, year, month);
        return buildCategoryVO(results);
    }

    @Override
    public List<CostCategoryVO> getSummaryToDate(String date) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return Collections.emptyList();
        }
        LocalDate localDate = LocalDate.parse(date);
        List<Map<String, Object>> results = this.baseMapper.getSummaryToDate(userId, localDate);
        return buildCategoryVO(results);
    }

    @Override
    public BigDecimal getTotalCost() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal total = this.baseMapper.getTotalCost(userId);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public List<CostRecordVO> getAllRecords() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return Collections.emptyList();
        }
        List<CostRecord> list = this.list(new LambdaQueryWrapper<CostRecord>()
                .eq(CostRecord::getUserId, userId)
                .orderByDesc(CostRecord::getRecordDate));
        return list.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    private List<CostCategoryVO> buildCategoryVO(List<Map<String, Object>> results) {
        Map<String, BigDecimal> resultMap = results.stream()
                .collect(Collectors.toMap(
                        r -> (String) r.get("category"),
                        r -> (BigDecimal) r.get("total")
                ));
        
        return CATEGORIES.stream().map(cat -> {
            CostCategoryVO vo = new CostCategoryVO();
            vo.setCategory(cat);
            vo.setCategoryName(CATEGORY_NAMES.get(cat));
            vo.setTotal(resultMap.getOrDefault(cat, BigDecimal.ZERO));
            return vo;
        }).collect(Collectors.toList());
    }

    private CostRecordVO convertToVO(CostRecord record) {
        CostRecordVO vo = new CostRecordVO();
        BeanUtils.copyProperties(record, vo);
        return vo;
    }
}
