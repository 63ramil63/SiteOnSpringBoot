package com.example.simplesite.controller;

import com.example.simplesite.model.Product;
import com.example.simplesite.model.Role;
import com.example.simplesite.service.impl.ProductServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@AllArgsConstructor
public class ProductController {

    private final ProductServiceImpl productService;

    private void setHeaderButtons(Authentication authentication, Model model) {
        if (authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            model.addAttribute("isAuth", true);
            model.addAttribute("username", authentication.getName());
            boolean isAdmin = authentication.getAuthorities().stream().anyMatch(stream -> stream.getAuthority().equals("ADMIN"));
            model.addAttribute("isAdmin", isAdmin);
        } else {
            model.addAttribute("isAuth", false);
            model.addAttribute("username", "Не авторизован");
            model.addAttribute("isAdmin", false);
        }
    }

    @GetMapping("/adminPanel")
    public String adminPanel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        setHeaderButtons(authentication, model);
        model.addAttribute("product", new Product());
        return "adminPanel";
    }

    @PostMapping("/addProduct")
    public String addProduct(@ModelAttribute(name = "product")Product product) {
        productService.addProduct(product);
        return "redirect:/adminPanel";
    }
}
