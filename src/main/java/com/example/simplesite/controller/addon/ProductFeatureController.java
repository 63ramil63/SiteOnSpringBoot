package com.example.simplesite.controller.addon;

import com.example.simplesite.attributesetter.PageAttributeSetter;
import com.example.simplesite.dto.ProductDefaultInfoDTO;
import com.example.simplesite.dto.ProductFeatureDTO;
import com.example.simplesite.model.main.Product;
import com.example.simplesite.model.addon.ProductFeature;
import com.example.simplesite.model.main.User;
import com.example.simplesite.service.main.ProductFeatureTemplateService;
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
    private ProductFeatureTemplateService productFeatureTemplateService;
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

    private Product getProduct(Long id) {
        Optional<Product> optProduct = productService.findById(id);
        if (optProduct.isPresent()) {
            return optProduct.get();
        }
        return null;
    }

    private void setBodyAttributes(Model model, Long id, String type) {
        //добавляем примеры шаблонов и продукт на страницу для редактирования основных настроек
        model.addAttribute("types", productFeatureTemplateService.findDistinctTypes());

        //добавление массива доп инфы о товаре, если выбран определенный шаблон
        if (type != null) {
            List<String> features = productFeatureTemplateService.findParamsByProductType(type);
            List<ProductFeature> productFeatures = new ArrayList<>();
            //добавляем поля названия и пустые поля значения для доп инфы
            for (String feature: features) {
                ProductFeature pf = new ProductFeature();
                pf.setParam(feature);
                pf.setValue("");
                productFeatures.add(pf);
            }
            model.addAttribute("productFeatures", productFeatures);
        } else {
            //ищем уже существующие у этого продукта доп инфу
            List<ProductFeature> productFeatures = productFeatureService.findAllByProductId(id);
            model.addAttribute("productFeatures", productFeatures);
        }

        Product product = getProduct(id);
        if (product != null) {
            model.addAttribute("product", getProduct(id));
        }
    }

    @Override
    public void setAttribute(Model model, Authentication authentication) {
        setHeaderAttribute(model, authentication);
    }

    @GetMapping("/api/productFeature")
    public String productFeature(Model model, @RequestParam(name = "id") Long productId,
                                 @RequestParam(name = "type", required = false) String type) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        setAttribute(model, authentication);
        setBodyAttributes(model, productId, type);
        return "productFeatureSetter";
    }

    private void addFeatures(Product product, List<ProductFeature> features) {
        //проверяем не пустое ли значение доп атрибутов
        product.getFeatures().clear();
        if (features != null) {
            for (ProductFeature feature : features) {
                feature.setProduct(product);
                product.getFeatures().add(feature);
            }
            //сортируем по алфавиту
            product.getFeatures().sort(Comparator.comparing(ProductFeature::getParam));
        }
    }

    @PostMapping("/api/setFeature")
    public String editProductFeature(@ModelAttribute ProductFeatureDTO form) {
        //получаем из формы данные
        Long productId = form.getId();
        List<ProductFeature> features = form.getFeatures();
        Product product = getProduct(productId);
        if (product != null) {
            addFeatures(product, features);
            productService.saveProduct(product);
        }
        return "redirect:/api/productFeature?id=" + productId;
    }

    private void setProductInfo(ProductDefaultInfoDTO form, Product product) {
        product.setId(form.getId());
        product.setName(form.getName());
        product.setImgSrc(form.getImgSrc());
        product.setPrice(form.getPrice());
        product.setType(form.getType());
        product.setCompanyName(form.getCompanyName());
    }

    @PostMapping("/api/setProductDefaultInfo")
    public String setProductDefaultInfo(@ModelAttribute(name = "defaultInfoForm")ProductDefaultInfoDTO form) {
        Optional<Product> optProduct = productService.findById(form.getId());
        if (optProduct.isPresent()) {
            Product product = optProduct.get();
            setProductInfo(form, product);
            productService.saveProduct(product);
            return "redirect:/api/productFeature?id=" + form.getId();
        }
        return "redirect:/api/productFeature?id=" + form.getId();
    }
}
