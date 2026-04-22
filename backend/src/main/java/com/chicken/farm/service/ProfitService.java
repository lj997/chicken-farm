package com.chicken.farm.service;

import com.chicken.farm.dto.ProfitCalculateDTO;
import com.chicken.farm.vo.ProfitVO;

public interface ProfitService {
    
    ProfitVO calculateProfit(ProfitCalculateDTO dto);
}
