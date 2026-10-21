package com.smartstock.service;

import com.smartstock.dao.ProductDAO;
import com.smartstock.dao.ProductDAOImpl;
import com.smartstock.exception.ValidationException;
import com.smartstock.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ProductServiceImpl implements ProductService {

    private final ProductDAO productDAO;

    /**
     * Default constructor.
     * Uses the real database DAO implementation.
     */
    public ProductServiceImpl() {
        this.productDAO = new ProductDAOImpl();
    }

    /**
     * Constructor used for dependency injection.
     * Useful for testing and future scalability.
     */
    public ProductServiceImpl(ProductDAO productDAO) {

        if (productDAO == null) {
            throw new IllegalArgumentException(
                    "ProductDAO cannot be null."
            );
        }

        this.productDAO = productDAO;
    }

    @Override
    public Product createProduct(Product product) {

        validateProduct(product);

        return productDAO.save(product);
    }

    @Override
    public Optional<Product> getProductById(int productId) {

        validateProductId(productId);

        return productDAO.findById(productId);
    }

    @Override
    public List<Product> getAllProducts() {

        return productDAO.findAll();
    }

    @Override
    public List<Product> getLowStockProducts() {

        return productDAO.findLowStockProducts();
    }

    @Override
    public boolean updateProduct(Product product) {

        validateProduct(product);

        if (product.getProductId() <= 0) {

            throw new ValidationException(
                    "A valid product ID is required for updating."
            );
        }

        return productDAO.update(product);
    }

    @Override
    public boolean deleteProduct(int productId) {

        validateProductId(productId);

        return productDAO.deleteById(productId);
    }

    /**
     * Validates all business rules for a product.
     */
    private void validateProduct(Product product) {

        if (product == null) {

            throw new ValidationException(
                    "Product cannot be null."
            );
        }

        /*
         * Product name validation
         */
        if (product.getProductName() == null ||
                product.getProductName().isBlank()) {

            throw new ValidationException(
                    "Product name is required."
            );
        }

        if (product.getProductName().length() > 100) {

            throw new ValidationException(
                    "Product name cannot exceed 100 characters."
            );
        }

        /*
         * Category validation
         */
        if (product.getCategory() == null ||
                product.getCategory().isBlank()) {

            throw new ValidationException(
                    "Product category is required."
            );
        }

        if (product.getCategory().length() > 50) {

            throw new ValidationException(
                    "Category cannot exceed 50 characters."
            );
        }

        /*
         * Price validation
         */
        BigDecimal price = product.getPrice();

        if (price == null) {

            throw new ValidationException(
                    "Product price is required."
            );
        }

        if (price.compareTo(BigDecimal.ZERO) < 0) {

            throw new ValidationException(
                    "Price cannot be negative."
            );
        }

        /*
         * Quantity validation
         */
        if (product.getQuantity() < 0) {

            throw new ValidationException(
                    "Quantity cannot be negative."
            );
        }

        /*
         * Reorder level validation
         */
        if (product.getReorderLevel() < 0) {

            throw new ValidationException(
                    "Reorder level cannot be negative."
            );
        }
    }

    /**
     * Validates product ID.
     */
    private void validateProductId(int productId) {

        if (productId <= 0) {

            throw new ValidationException(
                    "Product ID must be greater than zero."
            );
        }
    }
}