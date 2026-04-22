package com.chicken.farm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.chicken.farm.mapper")
public class ChickenFarmApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChickenFarmApplication.class, args);
    }
}
