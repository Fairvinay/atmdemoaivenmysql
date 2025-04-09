package com.example.atmdemo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
//@Table(name = "userone") // ths only for railway app 
public class User {
    @Id
    @GeneratedValue(strategy =GenerationType.AUTO)  // GenerationType.AUTO SEQUENCE
    private Long id;
    private String firstName;
    private String lastName;
    private String nickname;
    private String email;
    private String password;
    private UUID uuid;
    private String verificationToken;

    
    /* USED for select u.users_id,r.id,r.name from users_roles u join role r 
     *           on r.id=u.roles_id where u.users_id=?
                 DefaultHandlerExceptionResolver : Ignoring exception, response committed already:
            Could not write JSON: Infinite recursion (StackOverflowError)    
     *
     */
    @JsonManagedReference 
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}) // CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH}
    @JoinTable(
          name = "users_roles",schema = "atmdemo",
            joinColumns = {@JoinColumn(name = "users_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "roles_id", referencedColumnName = "id")})
    private List<Role> roles;

//    @OneToMany(mappedBy = "user")
//    private List<UserAccount> accounts;
}
