package com.chicken.farm.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CostCategoryVO {
    
    private String category;
    
    private String categoryName;
    
    private BigDecimal total;
}
