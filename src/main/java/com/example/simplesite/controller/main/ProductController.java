package com.example.simplesite.controller.main;

import com.example.simplesite.attributesetter.PageAttributeSetter;
import com.example.simplesite.model.addon.ProductFeature;
import com.example.simplesite.model.main.Product;
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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
@AllArgsConstructor
public class ProductController implements PageAttributeSetter {

    private final ProductServiceImpl productService;
    private final ProductFeatureServiceImpl productFeatureService;
    private final UserServiceImpl userService;

    private int getCache(Authentication authentication) {
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
        return authentication.getAuthorities().stream().anyMatch(stream -> stream.getAuthority().equals("ADMIN"));
    }

    @Override
    public void setAttribute(Model model, Authentication authentication) {
        if (isAuth(authentication)) {
            model.addAttribute("isAuth", true);
            model.addAttribute("username", authentication.getName());
            model.addAttribute("isAdmin", isAdmin(authentication));
            model.addAttribute("cache", getCache(authentication));
        } else {
            model.addAttribute("isAuth", false);
            model.addAttribute("isAdmin", false);
        }
    }


    @GetMapping("/adminPanel")
    public String adminPanel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        setAttribute(model, authentication);
        model.addAttribute("product", new Product());
        return "adminPanel";
    }

    @PostMapping("/addProduct")
    public String addProduct(@ModelAttribute(name = "product")Product product) {
        productService.saveProduct(product);
        return "redirect:/adminPanel";
    }

    private Product getProduct(Long id) {
        Optional<Product> optProduct = productService.findById(id);
        //возвращает продукт либо null
        return optProduct.orElse(null);
    }

    private List<ProductFeature> getFeatures(Long productId) {
        List<ProductFeature> features = productFeatureService.findAllByProductId(productId);
        if (features != null) {
            return features;
        }
        return new ArrayList<>();
    }

    private void setRecommendedProducts(Model model, Product product) {
        List<Product> rProducts = productService.getFilteredProducts(product.getId(), product.getCompanyName(), product.getType());
        model.addAttribute("rProducts", (rProducts != null) ? rProducts : new ArrayList<>());
    }

    @GetMapping("/productPage")
    public String productPage(Model model, @RequestParam(name = "productId") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        setAttribute(model, authentication);
        Product product = getProduct(id);
        if (product != null) {
            model.addAttribute("product", product);
            model.addAttribute("features", getFeatures(id));
            setRecommendedProducts(model, product);
            return "productPage";
        }
        return "redirect:/market";
    }
}
