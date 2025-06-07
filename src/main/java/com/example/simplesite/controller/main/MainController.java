package com.example.simplesite.controller.main;

import com.example.simplesite.attributesetter.PageAttributeSetter;
import com.example.simplesite.config.ParamsConfig;
import com.example.simplesite.model.main.Product;
import com.example.simplesite.model.main.User;
import com.example.simplesite.service.main.impl.ProductServiceImpl;
import com.example.simplesite.service.main.impl.UserServiceImpl;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.util.*;

@Controller
@AllArgsConstructor
public class MainController implements PageAttributeSetter {

    private final UserServiceImpl userService;
    private final ProductServiceImpl productService;
    private final ParamsConfig paramsConfig;

    public static int productsPerPage;

    //установка значения после внедрения зависимостей
    @PostConstruct
    public void init() {
        productsPerPage = paramsConfig.getProductsPerPage();
    }

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

    //установка аттрибутов для формы фильтрации
    private void setFilterForm(Model model) {
        model.addAttribute("types", productService.findDistinctTypes());
        model.addAttribute("companyNames", productService.findDistinctCompanyNames());
    }

    private void setPageButtons(Model model, List<String> companyNames, List<String> types, int pageNumber) {
        long productCount = productService.getFilteredProductsCount(companyNames, types);
        long totalPages;
        if (productCount % productsPerPage == 0) {
            totalPages = productCount / productsPerPage;
        } else {
            totalPages = (productCount / productsPerPage) + 1;
        }

        Set<Integer> pageNumbers = new LinkedHashSet<>();

        //добавляем первую страницу
        pageNumbers.add(1);

        int start = Math.max(2, pageNumber - 2);
        int end = Math.min((int) totalPages - 1, pageNumber + 2);

        for (int i = start; i <= end; i++) {
            pageNumbers.add(i);
        }

        if (totalPages > 1) {
            pageNumbers.add((int) totalPages);
        }

        List<Integer> sortedPageNumbers = new ArrayList<>(pageNumbers);
        Collections.sort(sortedPageNumbers);
        model.addAttribute("pageNumbers", sortedPageNumbers);
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
            model.addAttribute("isAuth", false);
            model.addAttribute("isAdmin", false);
        }
        setFilterForm(model);
    }

    //получение отсортированных продуктов
    private List<Product> setProducts(String name, List<String> companyNames, List<String> types, String sort, int pageNumber) {
        return productService.getFilteredProducts(name, companyNames, types, sort, pageNumber);
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

    private void setSearchValue(Model model, String value) {
        model.addAttribute("searchInput", value != null ? value : "");
    }

    @GetMapping("/market")
    public String market(Model model, @RequestParam(name = "type", required = false) List<String> selectedTypes,
                         @RequestParam(name = "companyName", required = false) List<String> selectedCompanyNames,
                         @RequestParam(name = "sort", required = false) String selectedSort,
                         @RequestParam(name = "page", required = false, defaultValue = "1") Integer pageNumber,
                         @RequestParam(name = "search", required = false) String value) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        setAttribute(model, authentication);
        //устанавливаем, какой option будет выбран у select изначально
        setSorts(model, selectedSort);
        //устанавливаем, какие checkbox будут уже отмеченными
        setCheckedFilters(model, selectedCompanyNames, selectedTypes);
        //передаем значение поиска
        setSearchValue(model, value);
        setPageButtons(model, selectedCompanyNames, selectedTypes, pageNumber);
        //устанавливаем параметры для сортировки продукта и получаем продукты
        model.addAttribute("products", setProducts(value, selectedCompanyNames, selectedTypes, selectedSort, pageNumber));
        return "main";
    }

    @GetMapping("/")
    public RedirectView home() {
        return new RedirectView("/market");
    }
}
