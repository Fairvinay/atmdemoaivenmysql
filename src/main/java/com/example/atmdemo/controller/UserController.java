package com.example.atmdemo.controller;

import com.example.atmdemo.DTOs.*;
import com.example.atmdemo.models.User;
import com.example.atmdemo.security.JWT.JwtUtils;
import com.example.atmdemo.security.SecurityService;
import com.example.atmdemo.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.http.ResponseEntity.HeadersBuilder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

class Cross {
	//@Value("${spring.cors-origin}")
	  static String  corsOrigin  ;
	
	
}

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/user")
//@CrossOrigin(origins = { "http://localhost:4200"}, maxAge = 3600, allowedHeaders = {"Set-Cookie"})
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class); 
 
    //@Value("${spring.cors-origin}")
	  static String ourDomainPage;
    static String  localSite=  "localhost:4200";
    static String [] localSiteStrings =  { localSite,"localhost:8888","localhost:8080"};
    static  Pattern rx1 = Pattern.compile(localSiteStrings[0] );

	static Pattern rx2 = Pattern.compile(localSiteStrings[1]);
	static Pattern rx3 = Pattern.compile(localSiteStrings[2]);
	static Pattern rx4 = Pattern.compile(localSiteStrings[1]);
     static List<Pattern> regex =  Arrays.asList(  rx1, rx2 ,rx3,rx4) ;
	
   
    //static String regex =  Pattern.compile(localSiteStrings.join( "|" ), "i");
    
    
    private final UserService userService;
    private final SecurityService securityService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterUserDTO registerUserDTO) {
        try { 
        System.out.println("register user: {}"+  registerUserDTO.toString());
        logger.info("register user: {}", registerUserDTO.toString());
        if (securityService.userExistsByEmail(registerUserDTO.getEmail())) {
            logger.info("register user: {} already registered ", registerUserDTO.getEmail() );
            return ResponseEntity.badRequest().header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body("{ \"error\": \"Email already registered\" }");
        }
        if(userService.userExistsByNickname(registerUserDTO.getUsername())) {
            logger.info("register user: {} already registered ", registerUserDTO.getUsername() );
            return ResponseEntity.badRequest().header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body("{ \"error\": \"Nickname already registered\" }");
        }
        securityService.registerUser(registerUserDTO);
        return ResponseEntity.ok() 
        		//.header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:4200,https://storenotify.in")
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Set-Cookie")
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST, PUT, GET, OPTIONS, DELETE").body("Registration completed successfully");

         } catch (Exception e) {
            logger.info("register user: {}   ", e.getMessage() );
            return ResponseEntity.badRequest().header(HttpHeaders.CONTENT_TYPE, "application/json")
            .body("{ \"error\": \"Server Error\" }");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUserDTO loginUserDTO , HttpServletRequest request) {
        
        loginUserDTO.setEmail(loginUserDTO.getUsername());
        System.out.println("login user: {}"+  loginUserDTO.toString());
         logger.info("login user: {}", loginUserDTO.toString());
         
        try {
            
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUserDTO.getEmail(), loginUserDTO.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(authentication);
            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(authentication);
            logger.info("Authenticated user: {}", authentication.getName());
            logger.info("Authenticated principal: {}", authentication.getPrincipal());
           System.out.println("Authenticated principal: {}"+ authentication.getPrincipal());
           System.out.println("Authenticated user: {}"+  authentication.getName());
           
           
           String  callingSite  = request.getParameter("Referer");
           if( callingSite !=null && !callingSite.isEmpty()) {
	           if( callingSite.indexOf(ourDomainPage) > -1 ){
	        	   logger.info("Other domain found:");
	           }else { 
	        	   boolean found  = false;
	        	   for (Pattern rx : regex)  
	        	   {  if (rx.matcher(callingSite).matches()) {
	        		      found = true;
	        		      ourDomainPage =callingSite;
	        		      break;
	        		   } 
	        		}
	               logger.info("local found:");
	            }
           }
           else {
        	   ourDomainPage = "http://localhost:4200";
           }
            /*	HttpHeaders headers = new HttpHeaders();
           		headers.setLocation(URI.create(ourDomainPage+"/app?jwt_token="+jwtCookie.getValue()));
           		headers.add( "X-Test-Header",  jwtCookie.getValue());
           	 logger.info("redirecting  : "+URI.create(ourDomainPage+"/app?jwt_token="+jwtCookie.getValue()).toString());	
           		
            LoginResponseDTO foundUser = 	new LoginResponseDTO(userService.findByEmail(authentication.getName())
                        .orElseThrow(() -> new IllegalStateException("User not found"))
                        .getNickname());
            logger.info("user  found: "+foundUser.toString());
           return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY) ;
           */
           return ResponseEntity.ok()
                       // .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:4200,https://storenotify.in")
                        .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Set-Cookie")
                        .header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST, PUT, GET, OPTIONS, DELETE")
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .header( "X-Test-Header",  jwtCookie.getValue()) 
                    .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                    .body(new LoginResponseDTO(userService.findByEmail(authentication.getName())
                            .orElseThrow(() -> new IllegalStateException("User not found"))
                            .getNickname()));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid login credentials");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
        ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();
       
        logger.info("logout user: {}");
        return ResponseEntity.ok()
       // .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:4200,https://storenotify.in")
        .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Set-Cookie")
        .header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST, PUT, GET, OPTIONS, DELETE")
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
                .header( "X-Test-Header",  jwtCookie.getValue()) 
                .build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        logger.info("refresh user: {}");
        if (jwtUtils.getRefreshTokenValidateAndGenerateAccessToken(request) != null) {
            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(jwtUtils.getRefreshTokenValidateAndGenerateAccessToken(request));
            return ResponseEntity.ok()
            //.header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:4200,https://storenotify.in")
            .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Set-Cookie")
            .header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST, PUT, GET, OPTIONS, DELETE")
            .header( "X-Test-Header",  jwtCookie.getValue()) 
            .header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).build();
        }
        return ResponseEntity.badRequest().body("Refresh token is expired");
    }
    
    @PostMapping("/password-change")
    public ResponseEntity<?> changePassword(@RequestBody PasswordDTO passwordDTO, HttpServletRequest request) {
        logger.info("password-change user: {}");
        User user = jwtUtils.getUserFromRequest(request);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        String newPassword = passwordDTO.getNewPassword();
        int minimumPasswordLength = 6;

        if (newPassword.length() < minimumPasswordLength) {
            String errorMessage = "Password must be at least " + minimumPasswordLength + " characters long";
            return ResponseEntity.badRequest().body(errorMessage);
        }

        userService.changePassword(user, newPassword);
        return ResponseEntity.ok() 
        		//.header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:4200,https://storenotify.in")
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Set-Cookie")
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST, PUT, GET, OPTIONS, DELETE").build();
    }

    @GetMapping("/person")
    public ResponseEntity<?> getUser(HttpServletRequest request) {
        logger.info("person user: {}");
        User user = jwtUtils.getUserFromRequest(request);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        UserProfileDTO userDTO = new UserProfileDTO(user);
        return ((HeadersBuilder<BodyBuilder>) ResponseEntity.ok(userDTO)). header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, Cross.corsOrigin)
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Set-Cookie")
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST, PUT, GET, OPTIONS, DELETE").build();
    }

    @GetMapping("/people")
    public ResponseEntity<?> getAllPeople() {
        logger.info("people user: {}");
        List<User> people = userService.getAllUsers();
         // this satement give class cast exception issue 
        /*
      Hibernate: select u1_0.id,u1_0.email,u1_0.first_name,u1_0.last_name,u1_0.nickname,u1_0.password,u1_0.uuid,u1_0.verification_token from userone u1_0
2025-04-06T12:34:52.629+05:30 ERROR 12272 --- [nio-8080-exec-2] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed: java.lang.ClassCastException: class org.springframework.http.ResponseEntity cannot be cast to class org.springframework.http.ResponseEntity$HeadersBuilder (org.springframework.http.ResponseEntity and org.springframework.http.ResponseEntity$HeadersBuilder are in unnamed module of loader org.springframework.boot.loader.launch.LaunchedClassLoader @37f8bb67)] with root cause

java.lang.ClassCastException: class org.springframework.http.ResponseEntity cannot be cast to class org.springframework.http.ResponseEntity$HeadersBuilder (org.springframework.http.ResponseEntity and org.springframework.http.ResponseEntity$HeadersBuilder are in unnamed module of loader org.springframework.boot.loader.launch.LaunchedClassLoader @37f8bb67)
        at com.example.atmdemo.controller.UserController.getAllPeople(UserController.java:232) ~[!/:0.0.1-SNAPSHOT]
        at    
         */
      //  return ((HeadersBuilder<BodyBuilder>) ResponseEntity.ok(people))
        // header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:4200,https://storenotify.in")
       // .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Set-Cookie")
       // .header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST, PUT, GET, OPTIONS, DELETE").build();
        return ResponseEntity.ok(people); // 👈 this is correct
    }
    
    

    @PatchMapping("/person")
    public ResponseEntity<?> updateUserByNickname(HttpServletRequest request, @RequestBody UserUpdateDTO userUpdateDTO) {
        logger.info("person patch user: {}"); 
        User requestUser = jwtUtils.getUserFromRequest(request);
        if (requestUser == null || userUpdateDTO == null) {
            return ResponseEntity.badRequest().build();
        }
        userService.updateUser(requestUser, userUpdateDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/person")
    public ResponseEntity<?> deletePerson(HttpServletRequest request) {
        logger.info("person delete user: {}"); 
        User user = jwtUtils.getUserFromRequest(request);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        userService.deleteUser(user);
        return ResponseEntity.noContent()
        		//.header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:4200,https://storenotify.in")
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Set-Cookie")
             
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST, PUT, GET, OPTIONS, DELETE").build();
    }
}
