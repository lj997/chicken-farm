package com.chicken.farm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chicken.farm.entity.CostRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface CostRecordMapper extends BaseMapper<CostRecord> {
    
    @Select("SELECT category, SUM(amount) as total FROM cost_record " +
            "WHERE record_date = #{date} GROUP BY category")
    List<Map<String, Object>> getDailySummary(@Param("date") LocalDate date);
    
    @Select("SELECT category, SUM(amount) as total FROM cost_record " +
            "WHERE YEAR(record_date) = #{year} AND MONTH(record_date) = #{month} GROUP BY category")
    List<Map<String, Object>> getMonthlySummary(@Param("year") int year, @Param("month") int month);
    
    @Select("SELECT category, SUM(amount) as total FROM cost_record " +
            "WHERE record_date <= #{date} GROUP BY category")
    List<Map<String, Object>> getSummaryToDate(@Param("date") LocalDate date);
    
    @Select("SELECT SUM(amount) FROM cost_record")
    BigDecimal getTotalCost();
}
