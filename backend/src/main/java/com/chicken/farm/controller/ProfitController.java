package com.chicken.farm.controller;

import com.chicken.farm.common.Result;
import com.chicken.farm.dto.ProfitCalculateDTO;
import com.chicken.farm.service.ProfitService;
import com.chicken.farm.vo.ProfitVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profit")
public class ProfitController {

    @Autowired
    private ProfitService profitService;

    @PostMapping("/calculate")
    public Result<ProfitVO> calculate(@RequestBody ProfitCalculateDTO dto) {
        try {
            ProfitVO vo = profitService.calculateProfit(dto);
            return Result.success(vo);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
}
