package com.example.atmdemo.rollbar;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.rollbar.notifier.Rollbar;
import com.rollbar.notifier.config.Config;
import com.rollbar.spring.webmvc.RollbarSpringConfigBuilder;
/*
@Configuration()
@EnableWebMvc
@ComponentScan({
    // ADD YOUR PROJECT PACKAGE HERE
    "com.example.atmdemo.rollbar"
})
*/
public class RollbarConfig {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	  /**
	   * Register a Rollbar bean to configure App with Rollbar.
	  
	  @Bean
	  public Rollbar rollbar() {

	    return new Rollbar(getRollbarConfigs("ce4f83f936f8487ca24934db5383babb"));
	  }

	  private Config getRollbarConfigs(String accessToken) {

	    // Reference ConfigBuilder.java for all the properties you can set for Rollbar
	    return RollbarSpringConfigBuilder.withAccessToken(accessToken)
	            .environment("development")
	            .codeVersion("1.0.0")
	            .build();
	  }
 */
}
