package com.codegym.inote.service.impl;

import com.codegym.inote.model.User;
import com.codegym.inote.repository.UserRepository;
import com.codegym.inote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;


public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        return new org.springframework.security.core.userdetails.User(username, user.getPassword(),
                enabled, accountNonExpired, credentialsNonExpired,
                accountNonLocked, user.getAuthorities());
    }


    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User getCurrentUser() {
        User user;
        String userName;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        user = this.findByUsername(userName);
        return user;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findOne(id);
    }

    @Override
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findOne(id);
        if (user == null) {
            throw new NullPointerException();
        }
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                enabled, accountNonExpired, credentialsNonExpired,
                accountNonLocked, user.getAuthorities());
    }

    @Override
    public boolean checkLogin(User user) {
        Iterable<User> users = this.findAll();
        for (User currentUser : users) {
            if (currentUser.getUsername().equals(user.getUsername())
                    && passwordEncoder.matches(user.getPassword(), currentUser.getPassword())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isRegister(User user) {
        boolean isRegister = false;
        Iterable<User> users = this.findAll();
        for (User currentUser : users) {
            if (user.getUsername().equals(currentUser.getUsername())) {
                isRegister = true;
                break;
            }
        }
        return isRegister;
    }

}
