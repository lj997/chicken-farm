package com.chicken.farm.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.chicken.farm.common.Result;
import com.chicken.farm.dto.LoginDTO;
import com.chicken.farm.service.CaptchaService;
import com.chicken.farm.utils.JwtUtil;
import com.chicken.farm.vo.LoginVO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "123456";
    
    @Autowired
    private CaptchaService captchaService;
    
    @GetMapping("/captcha")
    public void getCaptcha(@RequestParam(required = false) String t, HttpServletResponse response) throws IOException {
        String captchaKey = t != null ? t : String.valueOf(System.currentTimeMillis());
        
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(120, 40, 4, 50);
        String code = captcha.getCode();
        
        captchaService.setCaptcha(captchaKey, code);
        
        response.setContentType("image/jpeg");
        response.setHeader("Cache-Control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        
        captcha.write(response.getOutputStream());
    }
    
    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody LoginDTO loginDTO) {
        if (!captchaService.validateCaptcha(loginDTO.getCaptchaKey(), loginDTO.getCaptcha())) {
            return Result.error("验证码错误或已过期");
        }
        
        if (!DEFAULT_USERNAME.equals(loginDTO.getUsername()) || 
            !DEFAULT_PASSWORD.equals(loginDTO.getPassword())) {
            return Result.error("用户名或密码错误");
        }
        
        String token = JwtUtil.generateToken(loginDTO.getUsername());
        
        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setUsername(loginDTO.getUsername());
        
        return Result.success(vo);
    }
    
    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.success();
    }
    
    @GetMapping("/check")
    public Result<Boolean> checkLogin() {
        return Result.success(true);
    }
}