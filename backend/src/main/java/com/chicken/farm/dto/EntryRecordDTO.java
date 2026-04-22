package com.chicken.farm.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EntryRecordDTO {
    
    private Integer totalChicks;
    
    private LocalDate entryDate;
    
    private String breed;
    
    private BigDecimal pricePerChick;
}
