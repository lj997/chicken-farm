package com.chicken.farm.controller;

import com.chicken.farm.common.Result;
import com.chicken.farm.dto.AiConfigDTO;
import com.chicken.farm.service.AiConfigService;
import com.chicken.farm.vo.AiConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai-config")
public class AiConfigController {

    @Autowired
    private AiConfigService aiConfigService;

    @PostMapping
    public Result<AiConfigVO> saveOrUpdate(@RequestBody AiConfigDTO dto) {
        try {
            AiConfigVO vo = aiConfigService.saveOrUpdate(dto);
            return Result.success(vo);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping
    public Result<AiConfigVO> getCurrent() {
        AiConfigVO vo = aiConfigService.getCurrentConfig();
        return Result.success(vo);
    }

    @GetMapping("/check")
    public Result<Boolean> checkConfigured() {
        boolean configured = aiConfigService.isConfigured();
        return Result.success(configured);
    }
}
