package com.smartstock.report;

import com.smartstock.model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InventoryReport extends Report {

    private final List<Product> products;

    public InventoryReport(List<Product> products) {
        super("Inventory Report");

        this.products =
                products != null
                        ? new ArrayList<>(products)
                        : new ArrayList<>();
    }

    public int getTotalProducts() {

        return products.size();
    }

    public int getLowStockCount() {

        int count = 0;

        for (Product product : products) {

            if (product != null
                    && !product.isOutOfStock()
                    && product.isLowStock()) {

                count++;
            }
        }

        return count;
    }

    public int getOutOfStockCount() {

        int count = 0;

        for (Product product : products) {

            if (product != null
                    && product.isOutOfStock()) {

                count++;
            }
        }

        return count;
    }

    public BigDecimal getInventoryValue() {

        BigDecimal value =
                BigDecimal.ZERO;

        for (Product product : products) {

            if (product != null
                    && product.getPrice() != null) {

                value =
                        value.add(
                                product.getPrice()
                                        .multiply(
                                                BigDecimal.valueOf(
                                                        product.getQuantity()
                                                )
                                        )
                        );
            }
        }

        return value;
    }

    @Override
    public String generate() {

        return getTitle()
                + ": "
                + getTotalProducts()
                + " products, "
                + getLowStockCount()
                + " low stock, "
                + getOutOfStockCount()
                + " out of stock, inventory value ₹"
                + getInventoryValue();
    }
}