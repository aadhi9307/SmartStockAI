package com.smartstock.dao;

import com.smartstock.model.Supplier;

import java.util.List;
import java.util.Optional;

public interface SupplierDAO {

    Supplier save(Supplier supplier);

    Optional<Supplier> findById(int supplierId);

    List<Supplier> findAll();

    boolean update(Supplier supplier);

    boolean deleteById(int supplierId);
}