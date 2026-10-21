package com.smartstock.service;

import com.smartstock.model.Product;
import com.smartstock.model.Sale;
import com.smartstock.report.InventoryReport;
import com.smartstock.report.Report;
import com.smartstock.report.SalesReport;

import java.util.List;

public class ReportServiceImpl implements ReportService {

    @Override
    public String generateReport(Report report) {

        if (report == null) {
            throw new IllegalArgumentException(
                    "Report cannot be null."
            );
        }

        return report.generate();
    }

    @Override
    public String generateInventoryReport(
            List<Product> products) {

        Report report =
                createInventoryReport(products);

        return generateReport(report);
    }

    @Override
    public String generateSalesReport(
            List<Sale> sales) {

        Report report =
                createSalesReport(sales);

        return generateReport(report);
    }

    @Override
    public Report createInventoryReport(
            List<Product> products) {

        return new InventoryReport(products);
    }

    @Override
    public Report createSalesReport(
            List<Sale> sales) {

        return new SalesReport(sales);
    }
}