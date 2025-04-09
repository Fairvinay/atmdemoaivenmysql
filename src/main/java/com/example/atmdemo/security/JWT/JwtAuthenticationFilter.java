package com.example.atmdemo.security.JWT;

import com.example.atmdemo.security.CustomUserDetailsService;
import com.example.atmdemo.security.SecurityConfig;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@NoArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class); 
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
	@Value("${spring.cors-origin}")
	  static String  corsOrigin  ;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = jwtUtils.getJwtFromCookies(request);
        String refreshToken = jwtUtils.getRefreshJwtFromCookies(request);
        String requestUsername = request.getParameter("username");
        requestUsername = requestUsername == null ? request.getParameter("email") : requestUsername ; 
        String requestUserpasswd = request.getParameter("password");
        
        request.getParameterMap().entrySet().forEach((p) -> 
        		 logger.info("key  "+p.getKey()+ " value "+p.getValue())
        		);
        
        
        if(refreshToken != null && token != null ) {
        	 response.setHeader("Access-Control-Allow-Headers", "Set-Cookie");
        	 response.setHeader("Access-Control-Allow-Headers", "X-Test-Header");
             if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
               //  response.setStatus(HttpServletResponse.SC_OK);
            	  response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
            	// not allowed in preflight 
            	  //  	  response.setHeader("Access-Control-Allow-Origin",  "http://localhost:4200,https://storenotify.in");
            	 response.setHeader("Access-Control-Allow-Headers", "Set-Cookie");
            	 
            	 response.setHeader("Access-Control-Allow-Headers", "X-Test-Header");
             } 
        }
        if(refreshToken == null && token == null && requestUsername!=null && requestUserpasswd !=null ) {
         try { 
        	 UserDetails userDetails = customUserDetailsService.loadUserByUsername(requestUsername);
             UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                     userDetails,
                     null,
                     userDetails.getAuthorities());
             authenticationToken.setDetails(
                     new WebAuthenticationDetailsSource().buildDetails(request));
             SecurityContextHolder.getContext().setAuthentication(authenticationToken);
             ResponseCookie jwtCookie =  jwtUtils.generateJwtCookie(authenticationToken);
             if(jwtCookie==null  ) {
            	 jwtCookie =  jwtUtils.generateJwtCookie(requestUsername);
            	 if(jwtCookie==null  ) {
            		 logger.info("Unable to create cookie for  "+requestUsername);
            	 }else {
            		 logger.info("Created cookie for  "+requestUsername+ "  ");
            		 String noHttp =   removeHttpOnlyFlag(  jwtCookie.toString());
                     logger.info("Cookie removed HttpOnly ... "+noHttp);
                     response.setHeader("Access-Control-Allow-Headers", "Set-Cookie");
                     response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
                   //  response.setHeader("Access-Control-Allow-Origin", "*");
                     response.setHeader("Access-Control-Allow-Origin",corsOrigin);
                    System.out.println("Access-Control-Allow-Origin *" );
                    System.out.println("spring.cors-origin  " +corsOrigin);
                     response.setHeader("Access-Control-Allow-Headers", "X-Test-Header");
                     logger.info("Access-Control-Allow-Headers Set-Cookie " );
                     response.addHeader("Set-Cookie",noHttp);
                    
                    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                      //  response.setStatus(HttpServletResponse.SC_OK);
                    	 logger.info("HTTP OPTIONS Access-Control-Allow-Headers Set-Cookie " );
                    	  response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
                    	// not allowed in preflight 
                    	  // response.setHeader("Access-Control-Allow-Origin",  "http://localhost:4200,https://storenotify.in");
                    	 response.setHeader("Access-Control-Allow-Headers", "Set-Cookie");
                    	 response.setHeader("Access-Control-Allow-Headers", "X-Test-Header");
                    } 
            		 
            	 }
            	 
             }
            } catch (Exception e) {  //CHECK for UserNotFoundException
               if( e instanceof UsernameNotFoundException) {
                logger.info("Either Registering User : {}", e.getMessage());
                if(request.getRequestURI().contains("register")) {
                    filterChain.doFilter(request, response);
                }
                else {
                  logger.info("User not trying to register : {}", e.getMessage());
                    response.sendError(HttpStatus.UNAUTHORIZED.value(), "User Not Found");
                }
               }
               else {
                   logger.info("Unable to login : {}", e.getMessage());
                   response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unable to login");
               }
             }
             //.getUserNameFromJwtToken(token);
        }
        
        
        if (token == null && refreshToken != null && jwtUtils.validateJwtToken(refreshToken)) {
            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(jwtUtils.getRefreshTokenValidateAndGenerateAccessToken(request));
             String noHttp =   removeHttpOnlyFlag(  jwtCookie.toString());
             logger.info("removed HttpOnly "+noHttp);
             logger.info("removed HttpOnly "+jwtCookie.toString().replace("HttpOnly", ""));
             response.setHeader("Access-Control-Allow-Headers", "*");
             response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
             response.setHeader("Access-Control-Allow-Headers", "X-Test-Header");
            response.addHeader("Set-Cookie",noHttp);
            response.addHeader("X-Test-Header",noHttp);
            
            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
              //  response.setStatus(HttpServletResponse.SC_OK);
            } 
           
        }

        if (token != null && jwtUtils.validateJwtToken(token)) {
            String username = jwtUtils.getUserNameFromJwtToken(token);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities());
            authenticationToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }

    private String removeHttpOnlyFlag(String setCookieHeader) {
	 
    	 if (setCookieHeader != null) {
             setCookieHeader = setCookieHeader.replace("HttpOnly", "");
            
             logger.info("removed HttpOnly "+setCookieHeader);
         }
    	 return setCookieHeader;
	}

	private void removeHttpOnlyFlag( HttpServletResponse res) {
       String  setCookieHeaderName = "Set-Cookie";
       String setCookieHeader = res.getHeader(setCookieHeaderName);

        if (setCookieHeader != null) {
            setCookieHeader = setCookieHeader.replace("HttpOnly", "");
            res.setHeader(setCookieHeaderName, setCookieHeader);
            logger.info("removed HttpOnly "+setCookieHeader);
        }
    }
	public JwtAuthenticationFilter(CorsConfiguration configuration) {
		
	}

	public JwtAuthenticationFilter(AuthenticationManager authManager) {
	}

 
}
