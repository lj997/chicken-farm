package com.chicken.farm.config;

import com.chicken.farm.common.UserContext;
import com.chicken.farm.entity.User;
import com.chicken.farm.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }
        
        String token = request.getHeader("Authorization");
        
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            if (JwtUtil.validateToken(token)) {
                Long userId = JwtUtil.getUserId(token);
                String username = JwtUtil.getUsername(token);
                
                if (userId != null) {
                    User user = new User();
                    user.setId(userId);
                    user.setUsername(username);
                    UserContext.setUser(user);
                }
                return true;
            }
        }
        
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":401,\"message\":\"未登录或登录已过期\",\"data\":null}");
        return false;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
    }
}