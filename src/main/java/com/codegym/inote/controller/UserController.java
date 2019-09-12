package com.codegym.inote.controller;

import com.codegym.inote.model.ConfirmationToken;
import com.codegym.inote.model.Role;
import com.codegym.inote.model.User;
import com.codegym.inote.service.ConfirmationTokenService;
import com.codegym.inote.service.EmailService;
import com.codegym.inote.service.RoleService;
import com.codegym.inote.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Controller
public class UserController {

    private static final String USER_REGISTER = "/user/register";

    private static final String ERROR_404 = "/error-404";
    private static final String MESSAGE = "message";
    private static final String DEFAULT_ROLE = "ROLE_USER";

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

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
        if (userService.isRegister(user)) {
            ModelAndView modelAndView = new ModelAndView(USER_REGISTER);
            modelAndView.addObject(MESSAGE, "username or email is already registered");
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("user/successfulRegister");
            Role role = roleService.findRoleByName(DEFAULT_ROLE);
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            User currentUser = new User();
            currentUser.setUsername(user.getUsername());
            currentUser.setPassword(passwordEncoder.encode(user.getPassword()));
            currentUser.setConfirmPassword(passwordEncoder.encode(user.getConfirmPassword()));
            currentUser.setEmail(user.getEmail());
            currentUser.setRoles(roles);
            userService.save(currentUser);

            ConfirmationToken confirmationToken = new ConfirmationToken(currentUser);

            confirmationTokenService.save(confirmationToken);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("chand312902@gmail.com");
            mailMessage.setText("To confirm your account, please click here : "
                    + "http://localhost:8080/confirm-account?token=" + confirmationToken.getConfirmationToken());

            emailService.sendEmail(mailMessage);


            modelAndView.addObject("user", currentUser);
            modelAndView.addObject("email",currentUser.getEmail());
            modelAndView.addObject(MESSAGE, "Success!");
            return modelAndView;
        }
    }

    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView confirmUserAccount(@RequestParam("token")String confirmationToken)
    {
        ModelAndView modelAndView;
        ConfirmationToken token = confirmationTokenService.findByConfirmationToken(confirmationToken);

        if(token != null)
        {
            User user = userService.findByEmail(token.getUser().getEmail());
            user.setEnabled(true);
            userService.save(user);
            modelAndView = new ModelAndView("/user/accountVerified");
            return modelAndView;
        }
        else
        {
            modelAndView = new ModelAndView("/user/error");
            modelAndView.addObject(MESSAGE,"The link is invalid or broken!");
            return modelAndView;
        }

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

    @PostMapping("/doLogin")
    public ModelAndView login(User user) {
        ModelAndView modelAndView;
        if (userService.checkLogin(user)) {
            modelAndView = new ModelAndView("/user/homepage");
            modelAndView.addObject("user", user);
            return modelAndView;
        }
        modelAndView = new ModelAndView("/login");
        modelAndView.addObject(MESSAGE, "username or password incorrect");
        return modelAndView;
    }
}
