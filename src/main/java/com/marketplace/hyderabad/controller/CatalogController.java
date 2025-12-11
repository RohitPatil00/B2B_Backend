package com.marketplace.hyderabad.controller;

import com.marketplace.hyderabad.model.Category;
import com.marketplace.hyderabad.model.Product;
import com.marketplace.hyderabad.repository.CategoryRepository;
import com.marketplace.hyderabad.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catalog")
public class CatalogController {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CatalogController(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/categories")
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/products")
    public List<Product> getProducts(@RequestParam(required = false) Long category) {
        if (category != null) return productRepository.findByCategoryId(category);
        return productRepository.findAll();
    }

    @GetMapping("/products/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productRepository.findById(id).orElse(null);
    }
}
