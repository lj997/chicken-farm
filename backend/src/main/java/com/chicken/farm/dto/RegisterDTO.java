package com.chicken.farm.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    
    private String username;
    private String password;
    private String confirmPassword;
    private String captcha;
    private String captchaKey;
}
