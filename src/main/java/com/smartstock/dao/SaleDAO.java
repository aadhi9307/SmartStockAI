package com.smartstock.dao;

import com.smartstock.model.ProductSalesSummary;
import com.smartstock.model.Sale;

import java.util.List;
import java.util.Optional;

public interface SaleDAO {

    Sale createSale(Sale sale);

    Optional<Sale> findById(int saleId);

    List<Sale> findAll();

    List<ProductSalesSummary> findProductSalesSummary();
}