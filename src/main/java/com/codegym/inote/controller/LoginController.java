package com.codegym.inote.controller;

import com.codegym.inote.model.User;
import com.codegym.inote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public ModelAndView loginForm() {
        return new ModelAndView("/login");
    }

    @PostMapping("/login")
    public ModelAndView login(@RequestParam("ssoId") String username, @RequestParam("password") String password) {
        ArrayList<User> users = (ArrayList<User>) userService.findAll();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return new ModelAndView("user/homepage");
            }
        }
        ModelAndView modelAndView = new ModelAndView("/login");
        modelAndView.addObject("message", "username or password incorrect");
        return modelAndView;
    }


}
