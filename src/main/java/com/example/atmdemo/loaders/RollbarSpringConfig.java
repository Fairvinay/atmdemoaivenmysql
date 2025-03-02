package com.example.atmdemo.loaders;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import com.example.atmdemo.rollbar.RollbarConfigurationProperties;
import com.rollbar.notifier.Rollbar;
import com.rollbar.spring.webmvc.RollbarSpringConfigBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j

@RequiredArgsConstructor
 
@ComponentScan({
 
       "com.gitlab.johnjvester.rollbar",
 
       "com.rollbar.spring",
 
})
 
@Component
 
public class RollbarSpringConfig {
 
   private final RollbarConfigurationProperties rollbarConfigurationProperties;
 
   private final RollbarServerProvider rollbarServerProvider;
 
   @Bean
 
   public Rollbar rollbar() {
 
       log.debug("rollbarConfigurationProperties={}", rollbarConfigurationProperties);
       return new Rollbar(RollbarSpringConfigBuilder
 
         .withAccessToken(rollbarConfigurationProperties.getAccessToken())
 
         .environment(rollbarConfigurationProperties.getEnvironment())
 
         .server(rollbarServerProvider)
 
         .build());
 
   }
 
}