package com.chicken.farm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chicken.farm.dto.ProfitCalculateDTO;
import com.chicken.farm.entity.BreedingStatus;
import com.chicken.farm.entity.EntryRecord;
import com.chicken.farm.mapper.BreedingStatusMapper;
import com.chicken.farm.mapper.CostRecordMapper;
import com.chicken.farm.mapper.EntryRecordMapper;
import com.chicken.farm.service.MarketPriceService;
import com.chicken.farm.service.ProfitService;
import com.chicken.farm.vo.ProfitVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProfitServiceImpl implements ProfitService {

    private static final Logger logger = LoggerFactory.getLogger(ProfitServiceImpl.class);

    @Autowired
    private EntryRecordMapper entryRecordMapper;

    @Autowired
    private BreedingStatusMapper breedingStatusMapper;

    @Autowired
    private CostRecordMapper costRecordMapper;

    @Autowired
    private MarketPriceService marketPriceService;

    @Override
    public ProfitVO calculateProfit(ProfitCalculateDTO dto) {
        EntryRecord entryRecord = entryRecordMapper.selectOne(
                new LambdaQueryWrapper<EntryRecord>()
                        .orderByDesc(EntryRecord::getId)
                        .last("LIMIT 1")
        );
        
        if (entryRecord == null) {
            throw new RuntimeException("请先录入入栏详情");
        }

        BreedingStatus latestStatus = breedingStatusMapper.selectOne(
                new LambdaQueryWrapper<BreedingStatus>()
                        .orderByDesc(BreedingStatus::getRecordDate)
                        .last("LIMIT 1")
        );

        if (latestStatus == null) {
            throw new RuntimeException("请先录入养殖现状");
        }

        BigDecimal avgWeight = latestStatus.getAvgWeight();
        Integer survivalCount = latestStatus.getSurvivalCount();
        String breed = entryRecord.getBreed();
        
        BigDecimal referenceMarketPrice = null;
        try {
            referenceMarketPrice = marketPriceService.getMarketPrice(breed);
            logger.info("获取市场参考价成功 - 品种: {}, 价格: {}", breed, referenceMarketPrice);
        } catch (RuntimeException e) {
            if ("AI_CONFIG_NOT_SET".equals(e.getMessage())) {
                throw new RuntimeException("AI_CONFIG_NOT_SET");
            }
            logger.warn("获取市场参考价失败: {}", e.getMessage());
        }
        
        BigDecimal estimatedTotalPrice = avgWeight
                .multiply(BigDecimal.valueOf(survivalCount))
                .multiply(dto.getMarketPrice());
        
        BigDecimal totalCost = costRecordMapper.getTotalCost();
        if (totalCost == null) {
            totalCost = BigDecimal.ZERO;
        }
        
        totalCost = totalCost.add(entryRecord.getTotalPrice());
        
        BigDecimal estimatedProfit = estimatedTotalPrice.subtract(totalCost);

        ProfitVO vo = new ProfitVO();
        vo.setMarketPrice(dto.getMarketPrice());
        vo.setReferenceMarketPrice(referenceMarketPrice);
        vo.setAvgWeight(avgWeight);
        vo.setSurvivalCount(survivalCount);
        vo.setBreed(breed);
        vo.setEstimatedTotalPrice(estimatedTotalPrice);
        vo.setTotalCost(totalCost);
        vo.setEstimatedProfit(estimatedProfit);

        return vo;
    }
}
