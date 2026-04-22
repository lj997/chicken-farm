package com.chicken.farm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chicken.farm.dto.EntryRecordDTO;
import com.chicken.farm.entity.EntryRecord;
import com.chicken.farm.vo.EntryRecordVO;

public interface EntryRecordService extends IService<EntryRecord> {
    
    EntryRecordVO saveOrUpdateEntry(EntryRecordDTO dto);
    
    EntryRecordVO getCurrentEntry();
}
