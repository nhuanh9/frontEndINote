package com.codegym.inote.model;

import lombok.Data;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Transactional
@Table(name = "user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotEmpty(message = "username must be fill")
    @Size(min = 6, max = 15, message = "username length must be between 6 and 15")
    private String username;

    @NotEmpty(message = "password must be fill")
    @Size(min = 6, message = "password length must be at least 6 characters")
    private String password;

    public User() {
    }

    public User(String username,String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
