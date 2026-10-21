package com.smartstock.report;

import com.smartstock.model.Sale;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SalesReport extends Report {

    private final List<Sale> sales;

    public SalesReport(List<Sale> sales) {

        super("Sales Report");

        this.sales =
                sales != null
                        ? new ArrayList<>(sales)
                        : new ArrayList<>();
    }

    public int getTotalSales() {

        return sales.size();
    }

    public BigDecimal getTotalRevenue() {

        BigDecimal revenue =
                BigDecimal.ZERO;

        for (Sale sale : sales) {

            if (sale != null
                    && sale.getTotalAmount() != null) {

                revenue =
                        revenue.add(
                                sale.getTotalAmount()
                        );
            }
        }

        return revenue;
    }

    @Override
    public String generate() {

        return getTitle()
                + ": "
                + getTotalSales()
                + " sales, total revenue ₹"
                + getTotalRevenue();
    }
}