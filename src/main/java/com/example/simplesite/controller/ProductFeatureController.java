package com.example.simplesite.controller;

import com.example.simplesite.attributesetter.PageAttributeSetter;
import com.example.simplesite.model.main.ProductFeature;
import com.example.simplesite.model.main.User;
import com.example.simplesite.service.main.ProductFeatureService;
import com.example.simplesite.service.main.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
public class ProductFeatureController implements PageAttributeSetter {

    private ProductFeatureService productFeatureService;
    private UserServiceImpl userService;

    public int getCache(Authentication authentication) {
        //получаем информацию об авторизированном пользователе
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
        if (isAdmin(authentication)) {
            model.addAttribute("isAuth", true);
            model.addAttribute("isAdmin", isAdmin(authentication));
            model.addAttribute("username", authentication.getName());
            model.addAttribute("cache", getCache(authentication));
        }
    }

//    @PostMapping("/api/setFeature")
//    public String editProductFeature(Model model, @RequestParam(name = "id") Long productId,
//                                     @ModelAttribute("features")List<ProductFeature> features) {
//
//    }

    @GetMapping("/api/productFeature")
    public String productFeature(Model model, @RequestParam(name = "id") Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        setAttribute(model, authentication);
        return "productFeatureSetter";
    }

}
