package com.chicken.farm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@TableName("cost_record")
public class CostRecord {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private LocalDate recordDate;
    
    private String category;
    
    private BigDecimal amount;
    
    private String remark;
    
    private java.time.LocalDateTime createTime;
    
    private java.time.LocalDateTime updateTime;
}
