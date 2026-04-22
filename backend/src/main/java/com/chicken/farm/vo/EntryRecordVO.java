package com.chicken.farm.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EntryRecordVO {
    
    private Long id;
    
    private Integer totalChicks;
    
    private LocalDate entryDate;
    
    private Integer breedingDays;
    
    private String breed;
    
    private BigDecimal pricePerChick;
    
    private BigDecimal totalPrice;
}
