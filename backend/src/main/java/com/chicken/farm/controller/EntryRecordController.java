package com.chicken.farm.controller;

import com.chicken.farm.common.Result;
import com.chicken.farm.dto.EntryRecordDTO;
import com.chicken.farm.service.EntryRecordService;
import com.chicken.farm.vo.EntryRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/entry")
public class EntryRecordController {

    @Autowired
    private EntryRecordService entryRecordService;

    @PostMapping
    public Result<EntryRecordVO> saveOrUpdate(@RequestBody EntryRecordDTO dto) {
        EntryRecordVO vo = entryRecordService.saveOrUpdateEntry(dto);
        return Result.success(vo);
    }

    @GetMapping
    public Result<EntryRecordVO> getCurrent() {
        EntryRecordVO vo = entryRecordService.getCurrentEntry();
        return Result.success(vo);
    }
}
