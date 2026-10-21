package com.smartstock.service;

import com.smartstock.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Product createProduct(Product product);

    Optional<Product> getProductById(int productId);

    List<Product> getAllProducts();

    List<Product> getLowStockProducts();

    boolean updateProduct(Product product);

    boolean deleteProduct(int productId);
}