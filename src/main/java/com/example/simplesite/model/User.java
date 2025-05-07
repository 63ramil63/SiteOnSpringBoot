package com.example.simplesite.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Users")
public class User {
    @Id
    @Column(unique = true)
    private String email;
    private String username;
    private String password;
    @Transient
    private String confirmPassword;
}
