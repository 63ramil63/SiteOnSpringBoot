package com.example.simplesite.controller;

import com.example.simplesite.attributesetter.PageAttributeSetter;
import com.example.simplesite.model.Order;
import com.example.simplesite.model.User;
import com.example.simplesite.service.impl.OrderServiceImpl;
import com.example.simplesite.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
public class OrderController implements PageAttributeSetter {

    private OrderServiceImpl orderService;
    private UserServiceImpl userService;

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

    private void setHeaderAttribute(Model model, Authentication authentication) {
        if (isAuth(authentication)) {
            model.addAttribute("username", authentication.getName());
            model.addAttribute("isAdmin", isAdmin(authentication));
            model.addAttribute("isAuth", true);
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                String email = ((User) principal).getEmail();
                User user = userService.findUser(email);
                int cache = user.getCache();
                model.addAttribute("cache", cache);
            } else {
                model.addAttribute("cache", "error");
            }
        } else {
            model.addAttribute("username", "Не авторизован");
            model.addAttribute("isAuth", false);
            model.addAttribute("isAdmin", false);
        }
    }

    private void setBodyAttribute(Model model, Authentication authentication) {
        String email = getUserEmail(authentication);
        List<Order> orders = orderService.getOrdersByEmail(email);
        int count = orders.size();
        int sum = orders.stream().mapToInt(order -> order.getProduct().getPrice()).sum();
        model.addAttribute("orders", orders);
        model.addAttribute("count", count);
        model.addAttribute("sum", sum);
    }

    @Override
    public void setAttribute(Model model, Authentication authentication) {
        setHeaderAttribute(model, authentication);
        setBodyAttribute(model, authentication);
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

    @PostMapping("/deleteOrder")
    @Transactional
    public String deleteOrder(@RequestParam(name = "orderId") Long orderId) {
        orderService.deleteOrder(orderId);
        return "redirect:/orders";
    }
}
