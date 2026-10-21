package com.smartstock.service;

import com.smartstock.model.Product;
import com.smartstock.model.Sale;
import com.smartstock.report.InventoryReport;
import com.smartstock.report.Report;
import com.smartstock.report.SalesReport;

import java.util.List;

public interface ReportService {

    String generateReport(Report report);

    String generateInventoryReport(List<Product> products);

    String generateSalesReport(List<Sale> sales);

    Report createInventoryReport(List<Product> products);

    Report createSalesReport(List<Sale> sales);
}