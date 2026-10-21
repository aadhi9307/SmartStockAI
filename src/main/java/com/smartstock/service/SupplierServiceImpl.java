package com.smartstock.service;

import com.smartstock.dao.SupplierDAO;
import com.smartstock.dao.SupplierDAOImpl;
import com.smartstock.exception.ValidationException;
import com.smartstock.model.Supplier;

import java.util.List;
import java.util.Optional;

public class SupplierServiceImpl
        implements SupplierService {

    private final SupplierDAO supplierDAO;

    public SupplierServiceImpl() {

        this.supplierDAO =
                new SupplierDAOImpl();
    }

    public SupplierServiceImpl(
            SupplierDAO supplierDAO
    ) {

        if (supplierDAO == null) {

            throw new IllegalArgumentException(
                    "SupplierDAO cannot be null."
            );
        }

        this.supplierDAO =
                supplierDAO;
    }

    @Override
    public Supplier createSupplier(
            Supplier supplier
    ) {

        validateSupplier(
                supplier
        );

        return supplierDAO.save(
                supplier
        );
    }

    @Override
    public Optional<Supplier> getSupplierById(
            int supplierId
    ) {

        validateSupplierId(
                supplierId
        );

        return supplierDAO.findById(
                supplierId
        );
    }

    @Override
    public List<Supplier> getAllSuppliers() {

        return supplierDAO.findAll();
    }

    @Override
    public boolean updateSupplier(
            Supplier supplier
    ) {

        validateSupplier(
                supplier
        );

        if (supplier.getSupplierId() <= 0) {

            throw new ValidationException(
                    "A valid supplier ID is required for updating."
            );
        }

        return supplierDAO.update(
                supplier
        );
    }

    @Override
    public boolean deleteSupplier(
            int supplierId
    ) {

        validateSupplierId(
                supplierId
        );

        return supplierDAO.deleteById(
                supplierId
        );
    }

    private void validateSupplier(
            Supplier supplier
    ) {

        if (supplier == null) {

            throw new ValidationException(
                    "Supplier cannot be null."
            );
        }

        if (
                supplier.getSupplierName() == null
                        ||
                        supplier.getSupplierName().isBlank()
        ) {

            throw new ValidationException(
                    "Supplier name is required."
            );
        }

        if (
                supplier.getSupplierName().length()
                        > 100
        ) {

            throw new ValidationException(
                    "Supplier name cannot exceed 100 characters."
            );
        }

        if (
                supplier.getPhone() != null
                        &&
                        supplier.getPhone().length() > 20
        ) {

            throw new ValidationException(
                    "Phone number cannot exceed 20 characters."
            );
        }

        if (
                supplier.getEmail() != null
                        &&
                        supplier.getEmail().length() > 100
        ) {

            throw new ValidationException(
                    "Email cannot exceed 100 characters."
            );
        }

        if (
                supplier.getEmail() != null
                        &&
                        !supplier.getEmail().isBlank()
                        &&
                        !supplier.getEmail().matches(
                                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
                        )
        ) {

            throw new ValidationException(
                    "Please enter a valid email address."
            );
        }

        if (
                supplier.getAddress() != null
                        &&
                        supplier.getAddress().length() > 255
        ) {

            throw new ValidationException(
                    "Address cannot exceed 255 characters."
            );
        }
    }

    private void validateSupplierId(
            int supplierId
    ) {

        if (supplierId <= 0) {

            throw new ValidationException(
                    "Supplier ID must be greater than zero."
            );
        }
    }
}