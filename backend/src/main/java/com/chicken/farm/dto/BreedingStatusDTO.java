package com.chicken.farm.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BreedingStatusDTO {
    
    private LocalDate recordDate;
    
    private BigDecimal avgWeight;
    
    private Integer deadCount;
}
