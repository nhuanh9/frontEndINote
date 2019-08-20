package com.codegym.inote.service;

import com.codegym.inote.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void save(User user);

    Iterable<User> findAll();
}
