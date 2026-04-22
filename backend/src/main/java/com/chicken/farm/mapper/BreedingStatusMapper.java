package com.chicken.farm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chicken.farm.entity.BreedingStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

@Mapper
public interface BreedingStatusMapper extends BaseMapper<BreedingStatus> {
    
    @Select("SELECT SUM(dead_count) FROM breeding_status WHERE user_id = #{userId}")
    Integer getTotalDeadCount(@Param("userId") Long userId);
    
    @Select("SELECT avg_weight FROM breeding_status WHERE user_id = #{userId} ORDER BY record_date DESC LIMIT 1")
    BigDecimal getLatestAvgWeight(@Param("userId") Long userId);
}
