package com.example.simplesite.controller;

import com.example.simplesite.attributesetter.PageAttributeSetter;
import com.example.simplesite.model.User;
import com.example.simplesite.service.impl.UserServiceImpl;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class BalanceController implements PageAttributeSetter {
    private UserServiceImpl userService;

    public int getCache(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            String email = ((User) principal).getEmail();
            User user = userService.findUser(email);
            return user.getCache();
        }
        return 0;
    }

    @Override
    public boolean isAuth(Authentication authentication) {
        return authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
    }

    @Override
    public boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch(element -> element.getAuthority().equals("ADMIN"));
    }

    @Override
    public void setAttribute(Model model, Authentication authentication) {
        if (isAuth(authentication)) {
            model.addAttribute("isAuth", true);
            model.addAttribute("isAdmin", isAdmin(authentication));
            model.addAttribute("username", authentication.getName());
            model.addAttribute("cache", getCache(authentication));
        }
    }

    @GetMapping("/balance")
    public String balance(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        setAttribute(model, authentication);
        return "balanceTopUp";
    }

    private String getEmail(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            return ((User) principal).getEmail();
        }
        return "Not found";
    }

    @PostMapping("/fillBalance")
    @Transactional
    public String fillBalance(@RequestParam(name = "sum") int sum) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = getEmail(authentication);
        User user = userService.findUser(email);
        user.setCache(getCache(authentication) + sum);
        return "redirect:/orders";
    }
}
