package com.kb.product.service;

import com.kb.product.model.Product;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    private final Map<Long, Product> productStore = new HashMap<>();
    private Long idCounter = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @Override
    public Product createProduct(Product product) {
        product.setId(idCounter++);
        productStore.put(product.getId(), product);
        return product;
    }

    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(productStore.values());
    }

    @Override
    public Product getProductById(Long id) {
        return productStore.get(id);
    }

    @Override
    public void deleteProduct(Long id) {
        productStore.remove(id);
    }
}
