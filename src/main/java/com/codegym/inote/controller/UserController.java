package com.codegym.inote.controller;

import com.codegym.inote.model.User;
import com.codegym.inote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class UserController {

    public static final String USER_REGISTER = "/user/register";
    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;


    @GetMapping("/register")
    public ModelAndView showRegisterForm() {
        ModelAndView modelAndView = new ModelAndView(USER_REGISTER);
        modelAndView.addObject("user", new User());
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView registerNewUser(@Valid @ModelAttribute User user, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return new ModelAndView(USER_REGISTER);
        }
        User currentUser = new User();

        currentUser.setUsername(user.getUsername());
        currentUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(currentUser);

        ModelAndView modelAndView = new ModelAndView(USER_REGISTER);
        modelAndView.addObject("user", currentUser);
        modelAndView.addObject("message", "Success!");

        return modelAndView;
    }

    @GetMapping("/homepage")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("/user/homepage");
        modelAndView.addObject("user", userService.getCurrentUser());
        return modelAndView;
    }

    @GetMapping("/user/homepage")
    public ModelAndView goHomePage() {
        ModelAndView modelAndView = new ModelAndView("/user/homepage");
        modelAndView.addObject("user",userService.getCurrentUser());
        return modelAndView;
    }


}
