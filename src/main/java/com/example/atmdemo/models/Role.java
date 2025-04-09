package com.example.atmdemo.models;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq")
    @SequenceGenerator(name = "role_seq", sequenceName = "role_seq", allocationSize = 1) // Auto-increment  GenerationType.AUTO  GenerationType.IDENTITY this local gives problem 
    private Long id;
    private String name;
    
    
    
    /* USED for select u.users_id,r.id,r.name from users_roles u join role r 
     *           on r.id=u.roles_id where u.users_id=?
                 DefaultHandlerExceptionResolver : Ignoring exception, response committed already:
            Could not write JSON: Infinite recursion (StackOverflowError)    
     *
     */
    @JsonBackReference
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
    
    public Role(String name) {
        this.name = name;
    }

    public static List<Role> getRoleList() {
        return List.of(
                new Role("USER"),
                new Role("ADMIN")
        );
    }
    
    
    
}
