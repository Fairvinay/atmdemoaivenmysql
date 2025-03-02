package com.example.atmdemo.loaders;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.example.atmdemo.rollbar.RollbarConfigurationProperties;
import com.rollbar.api.payload.data.Server;
import com.rollbar.notifier.provider.Provider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

@Component
 
public class RollbarServerProvider implements Provider<Server> {
  private final Environment environment;
 private final RollbarConfigurationProperties rollbarConfigurationProperties;
  @Override
  public Server provide() {
   return new Server.Builder()
           .codeVersion(rollbarConfigurationProperties.getCodeVersion())
          .branch(rollbarConfigurationProperties.getBranch())
          .host(environment.getProperty("spring.application.name"))
          .root("com.gitlab.johnjvester.rollbar")
            .build();
 
   }
 
}