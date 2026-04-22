package com.chicken.farm.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.crypto.digest.BCrypt;
import com.chicken.farm.common.Result;
import com.chicken.farm.common.UserContext;
import com.chicken.farm.dto.LoginDTO;
import com.chicken.farm.dto.RegisterDTO;
import com.chicken.farm.entity.User;
import com.chicken.farm.service.CaptchaService;
import com.chicken.farm.service.UserService;
import com.chicken.farm.utils.JwtUtil;
import com.chicken.farm.vo.LoginVO;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "123456";
    
    @Autowired
    private CaptchaService captchaService;
    
    @Autowired
    private UserService userService;
    
    @PostConstruct
    public void init() {
        User admin = userService.getByUsername(DEFAULT_USERNAME);
        if (admin == null) {
            admin = new User();
            admin.setUsername(DEFAULT_USERNAME);
            admin.setPassword(BCrypt.hashpw(DEFAULT_PASSWORD));
            admin.setCreateTime(LocalDateTime.now());
            admin.setUpdateTime(LocalDateTime.now());
            userService.save(admin);
        }
    }
    
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
    
    @PostMapping("/register")
    public Result<LoginVO> register(@RequestBody RegisterDTO registerDTO) {
        if (!captchaService.validateCaptcha(registerDTO.getCaptchaKey(), registerDTO.getCaptcha())) {
            return Result.error("验证码错误或已过期");
        }
        
        try {
            User user = ((com.chicken.farm.service.impl.UserServiceImpl) userService).register(registerDTO);
            
            String token = JwtUtil.generateToken(user.getId(), user.getUsername());
            
            LoginVO vo = new LoginVO();
            vo.setToken(token);
            vo.setUsername(user.getUsername());
            
            return Result.success(vo);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody LoginDTO loginDTO) {
        if (!captchaService.validateCaptcha(loginDTO.getCaptchaKey(), loginDTO.getCaptcha())) {
            return Result.error("验证码错误或已过期");
        }
        
        User user = userService.getByUsername(loginDTO.getUsername());
        if (user == null) {
            return Result.error("用户名或密码错误");
        }
        
        if (!BCrypt.checkpw(loginDTO.getPassword(), user.getPassword())) {
            return Result.error("用户名或密码错误");
        }
        
        String token = JwtUtil.generateToken(user.getId(), user.getUsername());
        
        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setUsername(user.getUsername());
        
        return Result.success(vo);
    }
    
    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.success();
    }
    
    @GetMapping("/check")
    public Result<LoginVO> checkLogin() {
        User user = UserContext.getUser();
        if (user == null) {
            return Result.error("未登录");
        }
        LoginVO vo = new LoginVO();
        vo.setUsername(user.getUsername());
        return Result.success(vo);
    }
}