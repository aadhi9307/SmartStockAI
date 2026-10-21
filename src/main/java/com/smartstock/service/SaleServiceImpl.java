package com.smartstock.service;

import com.smartstock.dao.SaleDAO;
import com.smartstock.dao.SaleDAOImpl;
import com.smartstock.exception.ValidationException;
import com.smartstock.model.ProductSalesSummary;
import com.smartstock.model.Sale;
import com.smartstock.model.SaleItem;

import java.util.List;
import java.util.Optional;

public class SaleServiceImpl implements SaleService {

    private final SaleDAO saleDAO;

    public SaleServiceImpl() {
        this.saleDAO = new SaleDAOImpl();
    }

    public SaleServiceImpl(SaleDAO saleDAO) {

        if (saleDAO == null) {
            throw new IllegalArgumentException(
                    "SaleDAO cannot be null."
            );
        }

        this.saleDAO = saleDAO;
    }

    @Override
    public Sale createSale(Sale sale) {

        validateSale(sale);

        return saleDAO.createSale(sale);
    }

    @Override
    public Optional<Sale> getSaleById(int saleId) {

        if (saleId <= 0) {
            throw new ValidationException(
                    "Sale ID must be greater than zero."
            );
        }

        return saleDAO.findById(saleId);
    }

    @Override
    public List<Sale> getAllSales() {

        return saleDAO.findAll();
    }

    @Override
    public List<ProductSalesSummary> getProductSalesSummary() {

        return saleDAO.findProductSalesSummary();
    }

    private void validateSale(Sale sale) {

        if (sale == null) {
            throw new ValidationException(
                    "Sale cannot be null."
            );
        }

        if (sale.getItems() == null ||
                sale.getItems().isEmpty()) {

            throw new ValidationException(
                    "A sale must contain at least one item."
            );
        }

        for (SaleItem item : sale.getItems()) {

            if (item == null) {
                throw new ValidationException(
                        "Sale contains an invalid item."
                );
            }

            if (item.getProductId() <= 0) {
                throw new ValidationException(
                        "Product ID must be greater than zero."
                );
            }

            if (item.getQuantity() <= 0) {
                throw new ValidationException(
                        "Sale quantity must be greater than zero."
                );
            }

            if (item.getUnitPrice() == null) {
                throw new ValidationException(
                        "Unit price cannot be null."
                );
            }

            if (item.getUnitPrice().signum() < 0) {
                throw new ValidationException(
                        "Unit price cannot be negative."
                );
            }
        }

        sale.calculateTotal();

        if (sale.getTotalAmount() == null ||
                sale.getTotalAmount().signum() <= 0) {

            throw new ValidationException(
                    "Sale total must be greater than zero."
            );
        }
    }
}