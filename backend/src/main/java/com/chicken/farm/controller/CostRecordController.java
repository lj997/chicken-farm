package com.chicken.farm.controller;

import com.chicken.farm.common.Result;
import com.chicken.farm.dto.CostRecordDTO;
import com.chicken.farm.service.CostRecordService;
import com.chicken.farm.vo.CostCategoryVO;
import com.chicken.farm.vo.CostRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/cost")
public class CostRecordController {

    @Autowired
    private CostRecordService costRecordService;

    @PostMapping
    public Result<CostRecordVO> save(@RequestBody CostRecordDTO dto) {
        try {
            CostRecordVO vo = costRecordService.saveCost(dto);
            return Result.success(vo);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping
    public Result<List<CostRecordVO>> getAll() {
        List<CostRecordVO> list = costRecordService.getAllRecords();
        return Result.success(list);
    }

    @GetMapping("/summary/daily")
    public Result<List<CostCategoryVO>> getDailySummary(@RequestParam String date) {
        List<CostCategoryVO> list = costRecordService.getDailySummary(date);
        return Result.success(list);
    }

    @GetMapping("/summary/monthly")
    public Result<List<CostCategoryVO>> getMonthlySummary(@RequestParam int year, @RequestParam int month) {
        List<CostCategoryVO> list = costRecordService.getMonthlySummary(year, month);
        return Result.success(list);
    }

    @GetMapping("/summary/to-date")
    public Result<List<CostCategoryVO>> getSummaryToDate(@RequestParam String date) {
        List<CostCategoryVO> list = costRecordService.getSummaryToDate(date);
        return Result.success(list);
    }

    @GetMapping("/total")
    public Result<BigDecimal> getTotalCost() {
        BigDecimal total = costRecordService.getTotalCost();
        return Result.success(total);
    }
}
