package com.example.atmdemo.rollbar;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
 
@Configuration("rollbarConfiguration")
 
@ConfigurationProperties("rollbar")
 
public class RollbarConfigurationProperties {
 
   private String accessToken;
 
   private String branch;
 
   private String codeVersion;
 
   private String environment;
}