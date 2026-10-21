package com.smartstock.service;

import com.smartstock.model.Supplier;

import java.util.List;
import java.util.Optional;

public interface SupplierService {

    Supplier createSupplier(
            Supplier supplier
    );

    Optional<Supplier> getSupplierById(
            int supplierId
    );

    List<Supplier> getAllSuppliers();

    boolean updateSupplier(
            Supplier supplier
    );

    boolean deleteSupplier(
            int supplierId
    );
}