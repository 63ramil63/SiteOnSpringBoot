package com.example.simplesite.controller;

import com.example.simplesite.attributesetter.PageAttributeSetter;
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
public class AuthController implements PageAttributeSetter {

    private final UserServiceImpl userService;

    private boolean isAuth(Authentication authentication) {
        return authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch(element -> element.getAuthority().equals("ADMIN"));
    }

    @Override
    public void setHeaderAttribute(Model model, Authentication authentication) {
        model.addAttribute("isAuth", isAuth(authentication));
    }

    @Override
    public void setBodyAttribute(Model model) {
        model.addAttribute("user", new User());
    }

    @GetMapping("/registerForm")
    public String registerForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        setHeaderAttribute(model, authentication);
        setBodyAttribute(model);
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute(name = "user") User user) {
        if (user.getPassword().equals(user.getConfirmPassword())) {
            userService.registerUser(user);
            return "redirect:/login";
        }
        return "redirect:/registerForm";
    }

    @GetMapping("/login")
    public String login(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        setHeaderAttribute(model, authentication);
        setBodyAttribute(model);
        return "login";
    }
}
