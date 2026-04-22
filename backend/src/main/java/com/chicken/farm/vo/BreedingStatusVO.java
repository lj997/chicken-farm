package com.chicken.farm.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BreedingStatusVO {
    
    private Long id;
    
    private LocalDate recordDate;
    
    private BigDecimal avgWeight;
    
    private Integer deadCount;
    
    private Integer survivalCount;
}
