package com.smartstock.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Sale {

    private int saleId;
    private Timestamp saleDate;
    private BigDecimal totalAmount;
    private List<SaleItem> items;

    public Sale() {
        this.items = new ArrayList<>();
    }

    public Sale(List<SaleItem> items) {
        this.items = items != null
                ? new ArrayList<>(items)
                : new ArrayList<>();

        calculateTotal();
    }

    public Sale(
            int saleId,
            Timestamp saleDate,
            BigDecimal totalAmount
    ) {
        this.saleId = saleId;
        this.saleDate = saleDate;
        this.totalAmount = totalAmount;
        this.items = new ArrayList<>();
    }

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public Timestamp getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Timestamp saleDate) {
        this.saleDate = saleDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<SaleItem> getItems() {
        return items;
    }

    public void setItems(List<SaleItem> items) {
        this.items = items != null
                ? new ArrayList<>(items)
                : new ArrayList<>();

        calculateTotal();
    }

    public void addItem(SaleItem item) {
        if (item == null) {
            throw new IllegalArgumentException(
                    "Sale item cannot be null."
            );
        }

        items.add(item);
        calculateTotal();
    }

    public void calculateTotal() {
        BigDecimal total = BigDecimal.ZERO;

        for (SaleItem item : items) {
            if (item != null) {
                total = total.add(
                        item.getLineTotal()
                );
            }
        }

        totalAmount = total;
    }
}