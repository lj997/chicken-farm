package com.chicken.farm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@TableName("entry_record")
public class EntryRecord {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private Integer totalChicks;
    
    private LocalDate entryDate;
    
    private String breed;
    
    private BigDecimal pricePerChick;
    
    private BigDecimal totalPrice;
    
    private java.time.LocalDateTime createTime;
    
    private java.time.LocalDateTime updateTime;
}
