package com.chicken.farm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("ai_config")
public class AiConfig {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String apiKey;
    
    private String baseUrl;
    
    private String modelVersion;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
}
