package com.example.atmdemo.security;

import com.example.atmdemo.controller.UserController;
import com.example.atmdemo.security.JWT.JwtAuthEntryPoint;
import com.example.atmdemo.security.JWT.JwtAuthenticationFilter;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.GenericFilterBean;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class); 
    private final JwtAuthEntryPoint authEntryPoint;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	
        http. cors(cors -> cors.configurationSource(corsConfigurationSource()))/*c   cors().configurationSource(corsConfigurationSource())*/. csrf(AbstractHttpConfigurer::disable)
        .exceptionHandling(
                (exceptionHandling) -> exceptionHandling
                        .authenticationEntryPoint(this.authEntryPoint)
        )
        .sessionManagement(
                (sessionManagement) -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
                (authorize) -> authorize.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/**").permitAll()
                        .anyRequest().authenticated()
        )
        	.	httpBasic(Customizer.withDefaults());
            
              
               // .httpBasic(Customizer.withDefaults());
        logger.info("Adding corsFilter httpsecurity");
        
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        .addFilterAfter(sessionFilter(), SecurityContextPersistenceFilter.class);
        return http.build();
    }
   /*@Bean
    @Order(1)
    public CorsFilter corsFilter(CorsConfigurationSource corsConfigurationSource) {
        logger.info("Creating corsFilter bean");
        CorsConfiguration configuration = new CorsConfiguration();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        configuration.addAllowedOriginPattern( "http://localhost:4200/login");
       //configuration.addAllowedOrigin( "http://localhost:4200");
        configuration.addAllowedHeader("Set-Cookie");
        configuration.setAllowedHeaders(Arrays.asList("Content-type","Authorization","Set-Cookie"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "OPTIONS", "PUT", "DELETE"));
        /*         
       When allowCredentials is true, allowedOrigins cannot contain the special value "*" 
       since that cannot be set on the "Access-Control-Allow-Origin" response header. 
       To allow credentials to a set of origins, list them explicitly or 
       consider using "allowedOriginPatterns" instead.
         */
    /*          //configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Content-type","Authorization","Set-Cookie"));
       // configuration.setExposedHeaders(Arrays.asList("Set-Cookie"));
        source.registerCorsConfiguration("/**", configuration);
      
        return new CorsFilter(corsConfigurationSource);
    }*/

    
    private Filter sessionFilter() {
    	GenericFilterBean gge = new SessionFilter();
    		 
		return gge;
	}

   
	@Bean
    public  UrlBasedCorsConfigurationSource corsConfigurationSource(  ) {
    	 CorsConfiguration configuration = new CorsConfiguration();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        configuration.addAllowedOriginPattern( "http://localhost:4200");
        configuration.addAllowedOrigin( "http://localhost:4200");
        // response.setHeader("Access-Control-Allow-Headers", "X-Test-Header");
        configuration.setAllowedHeaders(Arrays.asList("Content-type","Authorization","Set-Cookie","X-Test-Header"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "OPTIONS", "PUT", "DELETE"));
        configuration.addAllowedHeader("X-Test-Header");
        
        /*         
       When allowCredentials is true, allowedOrigins cannot contain the special value "*" 
       since that cannot be set on the "Access-Control-Allow-Origin" response header. 
       To allow credentials to a set of origins, list them explicitly or 
       consider using "allowedOriginPatterns" instead.
         */
       configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Content-type","Authorization","Set-Cookie","X-Test-Header"));
       // configuration.setExposedHeaders(Arrays.asList("Set-Cookie"));
        source.registerCorsConfiguration("/**", configuration);
        return source;
    } 
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter();
    }
    
    
    /*
     
     request -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOriginPatterns(List.of("http://localhost:4200"));
           // configuration.addAllowedOriginPattern( "http://localhost:4200");
             configuration.addAllowedOrigin( "http://localhost:4200");
            configuration.addAllowedHeader("Set-Cookie");
             
            configuration.setAllowedHeaders(Arrays.asList("Set-Cookie"));
            configuration.setAllowedMethods(Arrays.asList("GET","POST", "OPTIONS", "PUT", "DELETE"));
            configuration.setExposedHeaders(Arrays.asList("Set-Cookie"));
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "*");

            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                response.setStatus(HttpServletResponse.SC_OK);
            }
            return configuration;
        	})
     
     */
     
}
