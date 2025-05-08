package com.example.simplesite.controller;

import com.example.simplesite.model.User;
import com.example.simplesite.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.Authentication;

@Controller
@AllArgsConstructor
public class AuthController {

    private final UserServiceImpl userService;

    private void setHeaderButtons(Authentication authentication, Model model) {
        if (authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            model.addAttribute("isAuth", true);
        } else {
            model.addAttribute("isAuth", false);
        }
    }

    @GetMapping("/registerForm")
    public String registerForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        setHeaderButtons(authentication, model);
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute(name = "user") User user) {
        if (user.getPassword().equals(user.getConfirmPassword())) {
            userService.registerUser(user);
            return "redirect:/market";
        }
        return "login";
    }

    @GetMapping("/login")
    public String login(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        setHeaderButtons(authentication, model);
        model.addAttribute("user", new User());
        return "login";
    }
}
