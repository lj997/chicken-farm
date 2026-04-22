package com.chicken.farm.service;

import com.chicken.farm.dto.AiConfigDTO;
import com.chicken.farm.vo.AiConfigVO;

public interface AiConfigService {
    
    AiConfigVO saveOrUpdate(AiConfigDTO dto);
    
    AiConfigVO getCurrentConfig();
    
    boolean isConfigured();
}
