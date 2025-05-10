package com.example.simplesite.controller;

import com.example.simplesite.attributesetter.PageAttributeSetter;
import com.example.simplesite.model.User;
import com.example.simplesite.service.impl.OrderServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
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
public class OrderController implements PageAttributeSetter {

    private OrderServiceImpl orderService;

    @Override
    public boolean isAuth(Authentication authentication) {
        return authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
    }

    @Override
    public boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch(element -> element.getAuthority().equals("ADMIN"));
    }

    private String getUserEmail(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            return ((User) principal).getEmail();
        }
        return "Email not found";
    }

    @Override
    public void setAttribute(Model model, Authentication authentication) {
        if (isAuth(authentication)) {
            model.addAttribute("username", authentication.getName());
            model.addAttribute("isAdmin", isAdmin(authentication));
            model.addAttribute("isAuth", true);
        } else {
            model.addAttribute("username", "Не авторизован");
            model.addAttribute("isAuth", false);
            model.addAttribute("isAdmin", false);
        }
        String email = getUserEmail(authentication);
        model.addAttribute("orders", orderService.getOrdersByEmail(email));
    }

    @PostMapping("/order")
    public String order(@RequestParam(name = "productId") String productId, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = getUserEmail(authentication);
        if (email.equals("Email not found")) {
            //todo
        }
        orderService.addOrder(email, Long.parseLong(productId));
        // получает значение заголовка Referer, который указывает на URL-адрес страницы, с которой был сделан текущий запрос
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("/orders")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        setAttribute(model, authentication);
        return "orders";
    }


}
