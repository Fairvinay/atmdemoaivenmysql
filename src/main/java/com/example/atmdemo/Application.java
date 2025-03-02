package com.example.atmdemo;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import com.example.atmdemo.controller.PingController;
import com.example.atmdemo.controller.UserController;
import com.example.atmdemo.rollbar.RollbarConfig;
import com.example.atmdemo.security.CustomUserDetailsService;
import com.example.atmdemo.security.SecurityConfig;
import com.example.atmdemo.security.JWT.JwtUtils;


@SpringBootApplication
//https://youtu.be/UZ8Q-X3AGHA  render mysql 
// We use direct @Import instead of @ComponentScan to speed up cold starts
 //@ComponentScan(basePackages = "com.example.atmdemo.controller")
@Import({ PingController.class, UserController.class ,CustomUserDetailsService.class,
        JwtUtils.class , SecurityConfig.class, RollbarConfig.class })
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    @Bean 
    public ApplicationRunner serverRunning() {
        return args -> {
            System.out.println("Server running");
        };
    }
    }
/*
mysql-158c35e3 
*/