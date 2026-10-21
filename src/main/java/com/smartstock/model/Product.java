package com.smartstock.model;

import java.math.BigDecimal;

public class Product {

    private int productId;
    private String productName;
    private String category;
    private BigDecimal price;
    private int quantity;
    private int reorderLevel;
    private Integer supplierId;

    // Default constructor
    public Product() {
    }

    // Constructor for creating a new product
    public Product(String productName,
                   String category,
                   BigDecimal price,
                   int quantity,
                   int reorderLevel,
                   Integer supplierId) {

        this.productName = productName;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.reorderLevel = reorderLevel;
        this.supplierId = supplierId;
    }

    // Full constructor
    public Product(int productId,
                   String productName,
                   String category,
                   BigDecimal price,
                   int quantity,
                   int reorderLevel,
                   Integer supplierId) {

        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.reorderLevel = reorderLevel;
        this.supplierId = supplierId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    // Business logic
    public boolean isLowStock() {
        return quantity <= reorderLevel;
    }

    public boolean isOutOfStock() {
        return quantity == 0;
    }

    public void increaseStock(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Stock increase amount must be greater than zero."
            );
        }

        quantity += amount;
    }

    public void decreaseStock(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Stock decrease amount must be greater than zero."
            );
        }

        if (amount > quantity) {
            throw new IllegalArgumentException(
                    "Insufficient stock."
            );
        }

        quantity -= amount;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", reorderLevel=" + reorderLevel +
                ", supplierId=" + supplierId +
                '}';
    }
}