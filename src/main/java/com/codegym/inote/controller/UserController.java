package com.codegym.inote.controller;

import com.codegym.inote.model.User;
import com.codegym.inote.model.UsersRoles;
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

    public static final String ERROR_404 = "/error-404";
    public static final String MESSAGE = "message";

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        modelAndView.addObject(MESSAGE, "Success!");

        return modelAndView;
    }

    @GetMapping("/homepage")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("/user/homepage");
        modelAndView.addObject("user", userService.getCurrentUser());
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user != null) {
            ModelAndView modelAndView = new ModelAndView("/user/edit");
            modelAndView.addObject("user", user);
            return modelAndView;
        }
        return new ModelAndView(ERROR_404);
    }

    @PostMapping("/edit")
    public ModelAndView editNoteType(@ModelAttribute User user) {
        userService.save(user);

        ModelAndView modelAndView = new ModelAndView("/user/edit");
        modelAndView.addObject("user", user);
        modelAndView.addObject(MESSAGE, "Updated!");
        return modelAndView;
    }

    @GetMapping("/view/{id}")
    public ModelAndView viewUser(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user == null) {
            return new ModelAndView(ERROR_404);
        }

        ModelAndView modelAndView = new ModelAndView("/user/view");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView loginForm() {
        return new ModelAndView("/login");
    }

    @PostMapping("/login")
    public ModelAndView login(@ModelAttribute User user) {
        if (userService.checkLogin(user)) {
            return new ModelAndView("/user/homepage");
        }
        ModelAndView modelAndView = new ModelAndView("/login");
        modelAndView.addObject(MESSAGE, "username or password incorrect");
        return modelAndView;
    }
}
