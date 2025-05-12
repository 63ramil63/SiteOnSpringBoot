package com.example.simplesite.controller;

import com.example.simplesite.attributesetter.PageAttributeSetter;
import com.example.simplesite.model.User;
import com.example.simplesite.service.impl.ProductServiceImpl;
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
public class MainController implements PageAttributeSetter {

    private final UserServiceImpl userService;
    private final ProductServiceImpl productService;

    public boolean isAuth(Authentication authentication) {
        return authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
    };

    public boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch(element -> element.getAuthority().equals("ADMIN"));
    }

    @Override
    public void setAttribute(Model model, Authentication authentication) {
        if (isAuth(authentication)) {
            model.addAttribute("isAuth", true);
            model.addAttribute("username", authentication.getName());
            model.addAttribute("isAdmin", isAdmin(authentication));
            //получаем информацию об авторизированном пользователе
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                String email = ((User) principal).getEmail();
                User user = userService.findUser(email);
                model.addAttribute("cache", user.getCache());
            } else {
                model.addAttribute("cache", "error");
            }
        } else {
            model.addAttribute("username", "Не авторизован");
            model.addAttribute("isAuth", false);
            model.addAttribute("isAdmin", false);
        }
        model.addAttribute("products", productService.getAllProducts());
    }


    @GetMapping("/market")
    public String market(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        setAttribute(model, authentication);
        return "main";
    }

    @GetMapping("/")
    public RedirectView home() {
        return new RedirectView("/market");
    }
}
