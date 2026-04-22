package com.chicken.farm.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CostRecordDTO {
    
    private LocalDate recordDate;
    
    private String category;
    
    private BigDecimal amount;
    
    private String remark;
}
