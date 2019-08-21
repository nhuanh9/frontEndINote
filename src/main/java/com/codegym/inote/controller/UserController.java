package com.codegym.inote.controller;

import com.codegym.inote.model.Note;
import com.codegym.inote.model.User;
import com.codegym.inote.service.NoteService;
import com.codegym.inote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private NoteService noteService;

    @GetMapping("/register")
    public ModelAndView showRegisterForm() {
        ModelAndView modelAndView = new ModelAndView("/user/register");
        modelAndView.addObject("user", new User());
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView registerNewUser(@Valid @ModelAttribute User user, BindingResult bindingResult) {


        if (bindingResult.hasFieldErrors()) {
            ModelAndView modelAndView = new ModelAndView("/user/register");
            return modelAndView;
        }
            User currentUser = new User();

            currentUser.setUsername(user.getUsername());
            currentUser.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.save(currentUser);

            ModelAndView modelAndView = new ModelAndView("/user/register");
            modelAndView.addObject("user", currentUser);
            modelAndView.addObject("message", "Success!");

        return modelAndView;
    }

    @GetMapping("/user/homepage")
    public ModelAndView home(Pageable pageable){
        Page<Note> notes = noteService.findAll(pageable);

        ModelAndView modelAndView = new ModelAndView("/user/homepage");
        modelAndView.addObject("notes", notes);
        return modelAndView;
    }
}
