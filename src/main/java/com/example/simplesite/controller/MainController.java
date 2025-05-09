package com.example.simplesite.controller;

import com.example.simplesite.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@AllArgsConstructor
public class MainController {

    private final UserServiceImpl userService;

    @GetMapping("/market")
    public String market(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            model.addAttribute("username", authentication.getName());
            model.addAttribute("isAuth", true);
            boolean isAdmin = authentication.getAuthorities().stream().anyMatch(stream -> stream.getAuthority().equals("ADMIN"));
            model.addAttribute("isAdmin", isAdmin);
        } else {
            model.addAttribute("username", "Не авторизован");
            model.addAttribute("isAuth", false);
            model.addAttribute("isAdmin", false);
        }
        return "main";
    }

    @GetMapping("/")
    public RedirectView home() {
        return new RedirectView("/market");
    }

}
