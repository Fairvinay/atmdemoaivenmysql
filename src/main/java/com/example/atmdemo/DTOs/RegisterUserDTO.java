package com.example.atmdemo.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String username;
    
	@Override
	public String toString() {
		return "RegisterUserDTO [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", password="
				+ password + "username="+ username + "]";
	}
}
