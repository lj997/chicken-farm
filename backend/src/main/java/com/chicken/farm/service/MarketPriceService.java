package com.chicken.farm.service;

import java.math.BigDecimal;

public interface MarketPriceService {
    
    BigDecimal getMarketPrice(String breed);
}
