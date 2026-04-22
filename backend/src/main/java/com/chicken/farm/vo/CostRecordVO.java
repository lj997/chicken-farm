package com.chicken.farm.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CostRecordVO {
    
    private Long id;
    
    private LocalDate recordDate;
    
    private String category;
    
    private BigDecimal amount;
    
    private String remark;
}
