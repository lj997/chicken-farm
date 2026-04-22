package com.chicken.farm.controller;

import com.chicken.farm.common.Result;
import com.chicken.farm.dto.BreedingStatusDTO;
import com.chicken.farm.service.BreedingStatusService;
import com.chicken.farm.vo.BreedingStatusVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/breeding")
public class BreedingStatusController {

    @Autowired
    private BreedingStatusService breedingStatusService;

    @PostMapping
    public Result<BreedingStatusVO> saveOrUpdate(@RequestBody BreedingStatusDTO dto) {
        try {
            BreedingStatusVO vo = breedingStatusService.saveOrUpdateStatus(dto);
            return Result.success(vo);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping
    public Result<List<BreedingStatusVO>> getAll() {
        List<BreedingStatusVO> list = breedingStatusService.getAllStatus();
        return Result.success(list);
    }

    @GetMapping("/latest")
    public Result<BreedingStatusVO> getLatest() {
        BreedingStatusVO vo = breedingStatusService.getLatestStatus();
        return Result.success(vo);
    }
}
