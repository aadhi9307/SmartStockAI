package com.smartstock.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;

public class ProductSalesSummary {

    private final int productId;
    private final String productName;
    private final String category;
    private final int currentStock;
    private final int reorderLevel;
    private final int unitsSold;
    private final int transactionCount;
    private final BigDecimal revenue;
    private final Timestamp firstSaleDate;
    private final Timestamp lastSaleDate;

    public ProductSalesSummary(
            int productId,
            String productName,
            String category,
            int currentStock,
            int reorderLevel,
            int unitsSold,
            int transactionCount,
            BigDecimal revenue,
            Timestamp firstSaleDate,
            Timestamp lastSaleDate
    ) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.currentStock = currentStock;
        this.reorderLevel = reorderLevel;
        this.unitsSold = unitsSold;
        this.transactionCount = transactionCount;
        this.revenue = revenue != null
                ? revenue
                : BigDecimal.ZERO;
        this.firstSaleDate = firstSaleDate;
        this.lastSaleDate = lastSaleDate;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getCategory() {
        return category;
    }

    public int getCurrentStock() {
        return currentStock;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public int getUnitsSold() {
        return unitsSold;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public Timestamp getFirstSaleDate() {
        return firstSaleDate;
    }

    public Timestamp getLastSaleDate() {
        return lastSaleDate;
    }

    public boolean isBelowReorderLevel() {
        return currentStock <= reorderLevel;
    }

    public boolean isOutOfStock() {
        return currentStock == 0;
    }

    public long getObservedDays() {

        if (firstSaleDate == null ||
                lastSaleDate == null) {

            return 0;
        }

        LocalDateTime first =
                firstSaleDate.toLocalDateTime();

        LocalDateTime last =
                lastSaleDate.toLocalDateTime();

        long days =
                Duration.between(
                        first,
                        last
                ).toDays();

        return Math.max(
                1,
                days + 1
        );
    }

    public double getObservedUnitsPerDay() {

        long days =
                getObservedDays();

        if (days <= 0) {
            return 0.0;
        }

        return (double) unitsSold / days;
    }

    @Override
    public String toString() {

        return "ProductSalesSummary{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", category='" + category + '\'' +
                ", currentStock=" + currentStock +
                ", reorderLevel=" + reorderLevel +
                ", unitsSold=" + unitsSold +
                ", transactionCount=" + transactionCount +
                ", revenue=" + revenue +
                ", firstSaleDate=" + firstSaleDate +
                ", lastSaleDate=" + lastSaleDate +
                '}';
    }
}