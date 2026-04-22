package com.chicken.farm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chicken.farm.dto.RegisterDTO;
import com.chicken.farm.entity.User;

public interface UserService extends IService<User> {
    
    User register(RegisterDTO dto);
    
    User getByUsername(String username);
}
