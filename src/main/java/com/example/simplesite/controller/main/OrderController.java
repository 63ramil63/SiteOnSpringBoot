package com.example.simplesite.controller.main;

import com.example.simplesite.attributesetter.PageAttributeSetter;
import com.example.simplesite.model.main.Order;
import com.example.simplesite.model.main.User;
import com.example.simplesite.service.main.impl.OrderServiceImpl;
import com.example.simplesite.service.main.impl.UserServiceImpl;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

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

    private void setHeaderAttribute(Model model, Authentication authentication) {
        //установка аттрибутов на странице в header
        if (isAuth(authentication)) {
            model.addAttribute("username", authentication.getName());
            model.addAttribute("isAdmin", isAdmin(authentication));
            model.addAttribute("isAuth", true);
            model.addAttribute("cache", getCache(authentication));
        } else {
            model.addAttribute("isAuth", false);
            model.addAttribute("isAdmin", false);
        }
    }

    private void setBodyAttribute(Model model, Authentication authentication) {
        //установка аттрибутов на странице в body
        String email = getUserEmail(authentication);
        List<Order> orders = orderService.getOrdersByEmail(email);
        int count = orders.size();
        int sum = orders.stream().mapToInt(order -> order.getProduct().getPrice()).sum();
        if (orders.isEmpty()) {
            model.addAttribute("hasOrder", false);
        } else {
            model.addAttribute("hasOrder", true);
            model.addAttribute("orders", orders);
        }
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
        if (referer != null) {
            //добавляем аттрибут added
            String redirectURL = UriComponentsBuilder.fromUriString(referer)
                    .replaceQueryParam("added", "true")
                    .build()
                    .toUriString();
            return "redirect:" + redirectURL;
        }
        return "redirect:" + referer;
    }



    @GetMapping("/orders")
    public String home(Model model, @RequestParam(required = false, name = "notEnoughCache") String notEnoughCache) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        setAttribute(model, authentication);
        if (notEnoughCache != null && notEnoughCache.equals("true")) {
            model.addAttribute("notEnoughCache", true);
        }
        return "orders";
    }

    @PostMapping("/buyOrder")
    @Transactional
    public String buyOrder(@RequestParam(name = "orderId") Long productId) {
        //получаем пользователя по его email
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = getUserEmail(authentication);
        User user = userService.findUser(email);

        //получаем заказ по ID и если optionalService содержит объект, то выполняется код в If
        Optional<Order> optionalOrder = orderService.getOrderById(productId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            int price = order.getProduct().getPrice();
            //если на балансе достаточно средств
            if (user.getCache() >= price) {
                user.setCache(user.getCache() - price);
                orderService.deleteOrder(order.getId());
                return "redirect:/orders?buyOrder=true";
            }
        }
        return "redirect:/orders?notEnoughCache=true";
    }

    @PostMapping("/deleteOrder")
    @Transactional
        public String deleteOrder(@RequestParam(name = "orderId") Long orderId) {
        orderService.deleteOrder(orderId);
        return "redirect:/orders";
    }
}
