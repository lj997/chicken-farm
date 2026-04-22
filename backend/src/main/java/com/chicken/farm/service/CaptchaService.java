package com.chicken.farm.service;

public interface CaptchaService {
    
    String getCaptcha(String key);
    
    void setCaptcha(String key, String code);
    
    void removeCaptcha(String key);
    
    boolean validateCaptcha(String key, String code);
}