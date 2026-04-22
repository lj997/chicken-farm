package com.chicken.farm.vo;

import lombok.Data;

@Data
public class AiConfigVO {
    
    private Long id;
    
    private String apiKey;
    
    private String baseUrl;
    
    private String modelVersion;
}
