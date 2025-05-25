package com.example.simplesite.service.main.impl;

import com.example.simplesite.model.main.Product;
import com.example.simplesite.repository.main.ProductRepository;
import com.example.simplesite.repository.main.addon.ProductSpecification;
import com.example.simplesite.service.main.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> findById(Long Id) {
        return productRepository.findById(Id);
    }

    public List<Product> getFilteredProducts(List<String> companyNames, List<String> types, String sortDirection) {
        Specification<Product> spec = Specification.where(ProductSpecification.filterByCompanyName(companyNames)
                .and(ProductSpecification.filterByType(types)));
        Sort sort = Sort.by("price");
        if (sortDirection != null) {
            if ("byPriceDesc".equalsIgnoreCase(sortDirection)) {
                sort = sort.descending();
            } else {
                sort = sort.ascending();
            }
            return productRepository.findAll(spec, sort);
        }
        return productRepository.findAll(spec);
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
