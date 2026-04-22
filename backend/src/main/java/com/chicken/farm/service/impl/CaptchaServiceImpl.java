package com.chicken.farm.service.impl;

import com.chicken.farm.service.CaptchaService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CaptchaServiceImpl implements CaptchaService {
    
    private final Map<String, String> captchaStore = new ConcurrentHashMap<>();
    
    @Override
    public String getCaptcha(String key) {
        return captchaStore.get(key);
    }
    
    @Override
    public void setCaptcha(String key, String code) {
        captchaStore.put(key, code);
    }
    
    @Override
    public void removeCaptcha(String key) {
        captchaStore.remove(key);
    }
    
    @Override
    public boolean validateCaptcha(String key, String code) {
        if (key == null || code == null) {
            return false;
        }
        String storedCode = getCaptcha(key);
        boolean valid = code.equalsIgnoreCase(storedCode);
        if (valid) {
            removeCaptcha(key);
        }
        return valid;
    }
}