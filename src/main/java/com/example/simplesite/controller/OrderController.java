package com.example.simplesite.controller;

import com.example.simplesite.model.User;
import com.example.simplesite.service.impl.OrderServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class OrderController {
    private OrderServiceImpl orderService;

    @PostMapping("/order")
    public String order(@RequestParam(name = "productId") String productId, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = null;

        //получаем инфу о текущей сессии (мэйл)
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            email = ((User) principal).getEmail();
        }
        System.out.println(productId);
        orderService.addOrder(email, Long.parseLong(productId));
        // получает значение заголовка Referer, который указывает на URL-адрес страницы, с которой был сделан текущий запрос
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
}
