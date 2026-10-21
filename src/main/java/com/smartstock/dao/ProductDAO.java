package com.smartstock.dao;

import com.smartstock.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDAO {

    Product save(Product product);

    Optional<Product> findById(int productId);

    List<Product> findAll();

    boolean update(Product product);

    boolean deleteById(int productId);

    List<Product> findLowStockProducts();
}