package com.example.simplesite.controller;

import com.example.simplesite.model.User;
import com.example.simplesite.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
public class AuthController {

    private final UserServiceImpl userService;

    @GetMapping("/registerForm")
    public String registerForm(Model model) {
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
        model.addAttribute("user", new User());
        return "login";
    }
}
