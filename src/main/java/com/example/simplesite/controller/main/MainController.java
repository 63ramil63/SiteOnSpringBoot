package com.example.simplesite.controller.main;

import com.example.simplesite.attributesetter.PageAttributeSetter;
import com.example.simplesite.model.main.Product;
import com.example.simplesite.model.main.User;
import com.example.simplesite.service.main.impl.ProductServiceImpl;
import com.example.simplesite.service.main.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class MainController implements PageAttributeSetter {

    private final UserServiceImpl userService;
    private final ProductServiceImpl productService;

    public boolean isAuth(Authentication authentication) {
        return authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
    }

    public boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch(element -> element.getAuthority().equals("ADMIN"));
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

    @Override
    public void setAttribute(Model model, Authentication authentication) {
        //установка аттрибутов
        if (isAuth(authentication)) {
            model.addAttribute("isAuth", true);
            model.addAttribute("username", authentication.getName());
            model.addAttribute("isAdmin", isAdmin(authentication));
            model.addAttribute("cache", getCache(authentication));
        } else {
            model.addAttribute("username", "Не авторизован");
            model.addAttribute("isAuth", false);
            model.addAttribute("isAdmin", false);
        }
        setFilterForm(model);
    }

    //установка аттрибутов для формы фильтрации
    private void setFilterForm(Model model) {
        model.addAttribute("types", productService.findDistinctTypes());
        model.addAttribute("companyNames", productService.findDistinctCompanyNames());
    }

    //получение отсортированных продуктов
    private List<Product> setProducts(List<String> companyNames, List<String> types, String sort) {
        return productService.getFilteredProducts(companyNames, types, sort);
    }

    //передаем значение сортировки на страницу для настройки списка
    private void setSorts(Model model, String selectedSort) {
        model.addAttribute("selectedSort", selectedSort);
    }

    //установка значений для сортировки продуктов
    private void setCheckedFilters (Model model, List<String> selectedCompanyNames, List<String> selectedTypes) {
        model.addAttribute("selectedCompanyNames", selectedCompanyNames != null ? selectedCompanyNames: new ArrayList<>());
        model.addAttribute("selectedTypes", selectedTypes != null ? selectedTypes : new ArrayList<>());
    }

    @GetMapping("/market")
    public String market(Model model, @RequestParam(name = "type", required = false) List<String> selectedTypes,
                         @RequestParam(name = "companyName", required = false) List<String> selectedCompanyNames,
                         @RequestParam(name = "sort", required = false) String selectedSort) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        setAttribute(model, authentication);
        setSorts(model, selectedSort);
        setCheckedFilters(model, selectedCompanyNames, selectedTypes);
        model.addAttribute("products", setProducts(selectedCompanyNames, selectedTypes, selectedSort));
        return "main";
    }

    @GetMapping("/")
    public RedirectView home() {
        return new RedirectView("/market");
    }
}
