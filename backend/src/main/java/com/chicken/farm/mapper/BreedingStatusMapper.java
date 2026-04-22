package com.chicken.farm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chicken.farm.entity.BreedingStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

@Mapper
public interface BreedingStatusMapper extends BaseMapper<BreedingStatus> {
    
    @Select("SELECT SUM(dead_count) FROM breeding_status")
    Integer getTotalDeadCount();
    
    @Select("SELECT avg_weight FROM breeding_status ORDER BY record_date DESC LIMIT 1")
    BigDecimal getLatestAvgWeight();
}
