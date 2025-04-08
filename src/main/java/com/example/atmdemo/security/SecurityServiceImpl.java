package com.example.atmdemo.security;

import com.example.atmdemo.DTOs.RegisterUserDTO;
import com.example.atmdemo.controller.UserController;
import com.example.atmdemo.models.Role;
import com.example.atmdemo.models.User;
import com.example.atmdemo.repositories.RoleRepository;
import com.example.atmdemo.repositories.UserRepository;
import com.example.atmdemo.security.SecurityService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

	private static final Logger logger = LoggerFactory.getLogger(SecurityServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    
    public static class EmailInput {
        @NotBlank
        @Email
        private String email;

        public EmailInput(String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }
    }

    @Override
    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void registerUser(RegisterUserDTO registerUserDTO) {
        User user = new User();
        user.setFirstName(registerUserDTO.getFirstName());
        user.setLastName(registerUserDTO.getLastName());
        user.setEmail(registerUserDTO.getEmail());
        user.setVerificationToken(generateToken());
        user.setPassword(passwordEncoder.encode(registerUserDTO.getPassword()));
        
        
        EmailInput input = new EmailInput(user.getFirstName());

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<EmailInput>> violations = validator.validate(input);

         
        
        if (violations.isEmpty()) {
        	 logger.info("User First Name is a Valid email ✔️ seems register using Signin with option ");
        	 logger.info("Therefore nickname will be consider as First Name and not  ✔️");
        	 logger.info(" First Name -  Last Name  ✔️");
        	 logger.info(" This causes profile page  long listing of  email  ✔️");
        	 String email  = registerUserDTO.getFirstName(); 
        	 String stripped =email.contains("@") ? email.substring(0, email.indexOf('@')) : email;

        	 user.setNickname(generateNickname(stripped,""));
        }
        else { 
        user.setNickname(generateNickname(registerUserDTO.getFirstName(), registerUserDTO.getLastName()));
        } 
        Role roles = roleRepository.findByName("USER").get();
        user.setRoles(Collections.singletonList(roles));
        userRepository.save(user);
    }

    @Override
    public String generateToken() {
        UUID tokenUUID = UUID.randomUUID();
        return tokenUUID.toString();
    }

    @Override
    public String generateNickname(String firstName, String lastName) {
        firstName = cleanseInput(firstName);
        lastName = cleanseInput(lastName);

        if (userRepository.existsByNickname(firstName + "-" + lastName)) {
            int index = 1;
            while (userRepository.existsByNickname(firstName + "-" + lastName + index)) {
                index++;
            }
            return (firstName + "-" + lastName + index).toLowerCase();
        }
        return (firstName + "-" + lastName).toLowerCase();
    }

    @Override
    public String cleanseInput(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }
}
