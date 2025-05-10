package com.example.simplesite.controller;

import com.example.simplesite.attributesetter.PageAttributeSetter;
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
public class ProductController implements PageAttributeSetter {

    private final ProductServiceImpl productService;

    @Override
    public boolean isAuth(Authentication authentication) {
        return authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
    }

    @Override
    public boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch(stream -> stream.getAuthority().equals("ADMIN"));
    }

    @Override
    public void setAttribute(Model model, Authentication authentication) {
        if (isAuth(authentication)) {
            model.addAttribute("isAuth", true);
            model.addAttribute("username", authentication.getName());
            model.addAttribute("isAdmin", isAdmin(authentication));
        } else {
            model.addAttribute("isAuth", false);
            model.addAttribute("username", "Не авторизован");
            model.addAttribute("isAdmin", false);
        }
        model.addAttribute("product", new Product());
    }


    @GetMapping("/adminPanel")
    public String adminPanel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        setAttribute(model, authentication);
        return "adminPanel";
    }

    @PostMapping("/addProduct")
    public String addProduct(@ModelAttribute(name = "product")Product product) {
        productService.addProduct(product);
        return "redirect:/adminPanel";
    }
}
