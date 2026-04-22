package com.chicken.farm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chicken.farm.dto.CostRecordDTO;
import com.chicken.farm.entity.CostRecord;
import com.chicken.farm.vo.CostCategoryVO;
import com.chicken.farm.vo.CostRecordVO;

import java.math.BigDecimal;
import java.util.List;

public interface CostRecordService extends IService<CostRecord> {
    
    CostRecordVO saveCost(CostRecordDTO dto);
    
    List<CostCategoryVO> getDailySummary(String date);
    
    List<CostCategoryVO> getMonthlySummary(int year, int month);
    
    List<CostCategoryVO> getSummaryToDate(String date);
    
    BigDecimal getTotalCost();
    
    List<CostRecordVO> getAllRecords();
}
