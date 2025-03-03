package com.example.atmdemo.DTOs;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RegisterUserDTO implements java.io.Serializable {
    private static final long serialVersionUID = -1234567890123456789L;

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
  

   
	@Override
	public String toString() {
         ObjectMapper mapper = new ObjectMapper();
        String json ="";
        try { 
            json =  mapper.writeValueAsString(this);
            System.out.println(json);
        } catch (Exception e) { 
            json =   "{  \"username\" : " + username +  ", \"password\" :"
            + password + " , \"email\" :" + email + ", \" firstName\" :" + firstName + ", \"lastName\":" + lastName + "}";

           }


		return json;
	}
}
