    package com.example.simplesite.controller.addon;

    import com.example.simplesite.attributesetter.PageAttributeSetter;
    import com.example.simplesite.dto.ProductFeatureTemplateDTO;
    import com.example.simplesite.model.addon.ProductFeatureTemplate;
    import com.example.simplesite.model.main.User;
    import com.example.simplesite.service.main.ProductFeatureTemplateService;
    import com.example.simplesite.service.main.impl.ProductFeatureTemplateServiceImpl;
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

    import java.util.ArrayList;
    import java.util.Comparator;
    import java.util.List;

    @Controller
    @AllArgsConstructor
    public class ProductFeatureTemplateController implements PageAttributeSetter {

        private UserServiceImpl userService;
        private ProductFeatureTemplateServiceImpl productFeatureTemplateService;
        private ProductServiceImpl productService;

        @Override
        public boolean isAuth(Authentication authentication) {
            return authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
        }

        @Override
        public boolean isAdmin(Authentication authentication) {
            return authentication.getAuthorities().stream().anyMatch(element -> element.getAuthority().equals("ADMIN"));
        }

        private int getCache(Authentication authentication) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                String email = ((User) principal).getEmail();
                User user = userService.findUser(email);
                return user.getCache();
            }
            return 0;
        }

        private void setHeaderAttribute(Model model, Authentication authentication) {
            if (isAuth(authentication)) {
                model.addAttribute("isAuth", true);
                model.addAttribute("isAdmin", isAdmin(authentication));
                model.addAttribute("username", authentication.getName());
                model.addAttribute("cache", getCache(authentication));
            }
        }

        private void setBodyAttribute(Model model) {
            model.addAttribute("types", productFeatureTemplateService.findDistinctTypes());
        }

        @Override
        public void setAttribute(Model model, Authentication authentication) {
            setHeaderAttribute(model, authentication);
            setBodyAttribute(model);
        }

        private List<String> setParams(String type) {
            return productFeatureTemplateService.findParamsByProductType(type);
        }

        @GetMapping("/api/productFeatureTemplate")
        public String productFeatureTemplate(Model model, @RequestParam(name = "type", required = false) String type) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            setAttribute(model, authentication);
            model.addAttribute("typeName", type != null ? type : "");
            model.addAttribute("params", type != null ? setParams(type): new ArrayList<>());
            return "productFeatureTemplateSetter";
        }

        @PostMapping("/api/setProductFeatureTemplate")
        public String setProductFeatureTemplate(@ModelAttribute(name = "productFeatureTemplateDTO") ProductFeatureTemplateDTO form) {
            //получение данных с формы
            String type = form.getType();
            List<String> params = form.getParams();
            params.sort(Comparator.naturalOrder());
            //создание нового объекта с данными из формы
            ProductFeatureTemplate productFeatureTemplate = new ProductFeatureTemplate();
            productFeatureTemplate.setType(type);
            productFeatureTemplate.setParams(params);
            productFeatureTemplateService.save(productFeatureTemplate);
            //перенаправление обратно на страницу установки параметров
            return "redirect:/api/productFeatureTemplate?type=" + type;
        }
    }
