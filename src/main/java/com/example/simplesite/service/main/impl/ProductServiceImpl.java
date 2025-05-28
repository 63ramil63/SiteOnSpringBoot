package com.example.simplesite.service.main.impl;

import com.example.simplesite.config.ParamsConfig;
import com.example.simplesite.model.main.Product;
import com.example.simplesite.repository.main.ProductRepository;
import com.example.simplesite.repository.main.addon.ProductSpecification;
import com.example.simplesite.service.main.ProductService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ParamsConfig paramsConfig;
    private static int productsPerPage;

    @PostConstruct
    public void init() {
        productsPerPage = paramsConfig.getProductsPerPage();
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> findById(Long Id) {
        return productRepository.findById(Id);
    }

    public long getFilteredProductsCount(List<String> companyNames, List<String> types) {
        Specification<Product> spec = Specification.where(ProductSpecification.filterByCompanyName(companyNames)
                .and(ProductSpecification.filterByType(types)));
        return productRepository.count(spec);
    }

    public List<Product> getFilteredProducts(String name,List<String> companyNames, List<String> types, String sortDirection, int pageNumber) {
        //отсчет идет с 0
        pageNumber = pageNumber - 1;
        Specification<Product> spec = Specification.where(ProductSpecification.filterByCompanyName(companyNames)
                .and(ProductSpecification.filterByType(types)).and(ProductSpecification.filterByName(name)));
        Sort sort = Sort.by("price");
        if (sortDirection != null) {
            if ("byPriceDesc".equalsIgnoreCase(sortDirection)) {
                sort = sort.descending();
            } else {
                sort = sort.ascending();
            }
            Pageable pageable = PageRequest.of(pageNumber, productsPerPage, sort);
            return productRepository.findAll(spec, pageable).getContent();
        }
        Pageable pageable = PageRequest.of(pageNumber, productsPerPage);
        return productRepository.findAll(spec, pageable).getContent();
    }

    @Override
    public List<String> findDistinctTypes() {
        return productRepository.findDistinctTypes();
    }

    @Override
    public List<String> findDistinctCompanyNames() {
        return productRepository.findDistinctCompanyNames();
    }


    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
