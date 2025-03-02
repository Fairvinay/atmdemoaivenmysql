package com.example.atmdemo.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserDTO {
    private String email;
    private String username;
    private String password;
    
	@Override
	public String toString() {
		return "LoginUserDTO [email=" + email +  ", username=" + username + ", password=" + password + "]";
	}
    
}
/*
ailed: org.springframework.http.converter.HttpMessageConversionException: 
Type definition error: [simple type, class com.example.atmdemo.DTOs.LoginUserDTO]] with root cause

com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Cannot construct instance of
 `com.example.atmdemo.DTOs.LoginUserDTO` (no Creators, like default constructor, exist): 
cannot deserialize from Object value (no delegate- or property-based Creator)
*/