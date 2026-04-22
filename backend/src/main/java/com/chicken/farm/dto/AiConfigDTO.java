package com.chicken.farm.dto;

import lombok.Data;

@Data
public class AiConfigDTO {
    
    private String apiKey;
    
    private String baseUrl;
    
    private String modelVersion;
}
