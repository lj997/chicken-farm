package com.chicken.farm.controller;

import com.chicken.farm.common.Result;
import com.chicken.farm.service.MarketPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/market-price")
public class MarketPriceController {

    @Autowired
    private MarketPriceService marketPriceService;

    @GetMapping
    public Result<BigDecimal> getMarketPrice(@RequestParam String breed) {
        try {
            BigDecimal price = marketPriceService.getMarketPrice(breed);
            return Result.success(price);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
}
