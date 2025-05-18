package com.example.simplesite.controller;

import com.example.simplesite.attributesetter.PageAttributeSetter;
import com.example.simplesite.dto.ProductFeatureDTO;
import com.example.simplesite.model.main.Product;
import com.example.simplesite.model.main.ProductFeature;
import com.example.simplesite.model.main.User;
import com.example.simplesite.service.main.impl.ProductFeatureServiceImpl;
import com.example.simplesite.service.main.impl.ProductServiceImpl;
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

import java.util.*;

@Controller
@AllArgsConstructor
public class ProductFeatureController implements PageAttributeSetter {

    private ProductServiceImpl productService;
    private ProductFeatureServiceImpl productFeatureService;
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

    private void setHeaderAttribute(Model model, Authentication authentication) {
        if (isAdmin(authentication)) {
            model.addAttribute("isAuth", true);
            model.addAttribute("isAdmin", isAdmin(authentication));
            model.addAttribute("username", authentication.getName());
            model.addAttribute("cache", getCache(authentication));
        }
    }

    private void setBodyAttributes(Model model, Long id) {
        List<ProductFeature> productFeatures = productFeatureService.findAllByProductId(id);
        model.addAttribute("productFeatures", productFeatures);
        model.addAttribute("productId", id);
    }

    @Override
    public void setAttribute(Model model, Authentication authentication) {
        setHeaderAttribute(model, authentication);
    }

    @GetMapping("/api/productFeature")
    public String productFeature(Model model, @RequestParam(name = "id") Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        setAttribute(model, authentication);
        setBodyAttributes(model, productId);
        return "productFeatureSetter";
    }

    @PostMapping("/api/setFeature")
    public String editProductFeature(@ModelAttribute ProductFeatureDTO form) {
        //получаем из формы данные
        Long productId = form.getId();
        List<ProductFeature> features = form.getFeatures();
        Optional<Product> optProduct = productService.findById(productId);
        if (optProduct.isPresent()) {
            //получаем продукт
            Product product = optProduct.get();
            //проверяем не пустое ли значение доп атрибутов
            product.getFeatures().clear();
            if (features != null) {
                for (ProductFeature feature : features) {
                    feature.setProduct(product);
                    product.getFeatures().add(feature);
                    System.err.println(feature.getParam());
                }
            }
            productService.saveProduct(product);
        }
        return "redirect:/api/productFeature?id=" + productId;
    }
}
