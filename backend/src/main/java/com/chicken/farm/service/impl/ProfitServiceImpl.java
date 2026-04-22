package com.chicken.farm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chicken.farm.common.UserContext;
import com.chicken.farm.dto.ProfitCalculateDTO;
import com.chicken.farm.entity.BreedingStatus;
import com.chicken.farm.entity.EntryRecord;
import com.chicken.farm.mapper.BreedingStatusMapper;
import com.chicken.farm.mapper.CostRecordMapper;
import com.chicken.farm.mapper.EntryRecordMapper;
import com.chicken.farm.service.ProfitService;
import com.chicken.farm.vo.ProfitVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProfitServiceImpl implements ProfitService {

    @Autowired
    private EntryRecordMapper entryRecordMapper;

    @Autowired
    private BreedingStatusMapper breedingStatusMapper;

    @Autowired
    private CostRecordMapper costRecordMapper;

    @Override
    public ProfitVO calculateProfit(ProfitCalculateDTO dto) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }
        
        EntryRecord entryRecord = entryRecordMapper.selectOne(
                new LambdaQueryWrapper<EntryRecord>()
                        .eq(EntryRecord::getUserId, userId)
                        .orderByDesc(EntryRecord::getId)
                        .last("LIMIT 1")
        );
        
        if (entryRecord == null) {
            throw new RuntimeException("请先录入入栏详情");
        }

        BreedingStatus latestStatus = breedingStatusMapper.selectOne(
                new LambdaQueryWrapper<BreedingStatus>()
                        .eq(BreedingStatus::getUserId, userId)
                        .orderByDesc(BreedingStatus::getRecordDate)
                        .last("LIMIT 1")
        );

        if (latestStatus == null) {
            throw new RuntimeException("请先录入养殖现状");
        }

        BigDecimal avgWeight = latestStatus.getAvgWeight();
        Integer survivalCount = latestStatus.getSurvivalCount();
        
        BigDecimal estimatedTotalPrice = avgWeight
                .multiply(BigDecimal.valueOf(survivalCount))
                .multiply(dto.getMarketPrice());
        
        BigDecimal totalCost = costRecordMapper.getTotalCost(userId);
        if (totalCost == null) {
            totalCost = BigDecimal.ZERO;
        }
        
        totalCost = totalCost.add(entryRecord.getTotalPrice());
        
        BigDecimal estimatedProfit = estimatedTotalPrice.subtract(totalCost);

        ProfitVO vo = new ProfitVO();
        vo.setMarketPrice(dto.getMarketPrice());
        vo.setAvgWeight(avgWeight);
        vo.setSurvivalCount(survivalCount);
        vo.setEstimatedTotalPrice(estimatedTotalPrice);
        vo.setTotalCost(totalCost);
        vo.setEstimatedProfit(estimatedProfit);

        return vo;
    }
}
