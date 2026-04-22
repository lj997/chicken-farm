package com.chicken.farm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@TableName("breeding_status")
public class BreedingStatus {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private LocalDate recordDate;
    
    private BigDecimal avgWeight;
    
    private Integer deadCount;
    
    private Integer survivalCount;
    
    private java.time.LocalDateTime createTime;
    
    private java.time.LocalDateTime updateTime;
}
