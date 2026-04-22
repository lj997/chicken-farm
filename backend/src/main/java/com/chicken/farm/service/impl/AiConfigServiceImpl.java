package com.chicken.farm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chicken.farm.dto.AiConfigDTO;
import com.chicken.farm.entity.AiConfig;
import com.chicken.farm.mapper.AiConfigMapper;
import com.chicken.farm.service.AiConfigService;
import com.chicken.farm.vo.AiConfigVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AiConfigServiceImpl implements AiConfigService {

    @Autowired
    private AiConfigMapper aiConfigMapper;

    @Override
    public AiConfigVO saveOrUpdate(AiConfigDTO dto) {
        AiConfig existingConfig = aiConfigMapper.selectOne(
                new LambdaQueryWrapper<AiConfig>().orderByDesc(AiConfig::getId).last("LIMIT 1")
        );

        AiConfig config;
        if (existingConfig != null) {
            config = existingConfig;
            config.setUpdateTime(LocalDateTime.now());
        } else {
            config = new AiConfig();
            config.setCreateTime(LocalDateTime.now());
            config.setUpdateTime(LocalDateTime.now());
        }

        config.setApiKey(dto.getApiKey());
        config.setBaseUrl(dto.getBaseUrl());
        config.setModelVersion(dto.getModelVersion());

        if (existingConfig != null) {
            aiConfigMapper.updateById(config);
        } else {
            aiConfigMapper.insert(config);
        }

        return convertToVO(config);
    }

    @Override
    public AiConfigVO getCurrentConfig() {
        AiConfig config = aiConfigMapper.selectOne(
                new LambdaQueryWrapper<AiConfig>().orderByDesc(AiConfig::getId).last("LIMIT 1")
        );
        return config != null ? convertToVO(config) : null;
    }

    @Override
    public boolean isConfigured() {
        AiConfig config = aiConfigMapper.selectOne(
                new LambdaQueryWrapper<AiConfig>().orderByDesc(AiConfig::getId).last("LIMIT 1")
        );
        return config != null && config.getApiKey() != null && !config.getApiKey().isEmpty();
    }

    private AiConfigVO convertToVO(AiConfig config) {
        AiConfigVO vo = new AiConfigVO();
        BeanUtils.copyProperties(config, vo);
        return vo;
    }
}
