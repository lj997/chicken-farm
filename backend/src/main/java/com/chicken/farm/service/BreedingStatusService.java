package com.chicken.farm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chicken.farm.dto.BreedingStatusDTO;
import com.chicken.farm.entity.BreedingStatus;
import com.chicken.farm.vo.BreedingStatusVO;

import java.util.List;

public interface BreedingStatusService extends IService<BreedingStatus> {
    
    BreedingStatusVO saveOrUpdateStatus(BreedingStatusDTO dto);
    
    List<BreedingStatusVO> getAllStatus();
    
    BreedingStatusVO getLatestStatus();
}
