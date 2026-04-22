package com.chicken.farm.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProfitVO {
    
    private BigDecimal marketPrice;
    
    private BigDecimal referenceMarketPrice;
    
    private BigDecimal avgWeight;
    
    private Integer survivalCount;
    
    private BigDecimal estimatedTotalPrice;
    
    private BigDecimal totalCost;
    
    private BigDecimal estimatedProfit;
    
    private String breed;
}
