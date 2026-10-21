package com.smartstock.service;

import com.smartstock.model.ProductSalesSummary;
import com.smartstock.model.Sale;

import java.util.List;
import java.util.Optional;

public interface SaleService {

    Sale createSale(Sale sale);

    Optional<Sale> getSaleById(int saleId);

    List<Sale> getAllSales();

    List<ProductSalesSummary> getProductSalesSummary();
}