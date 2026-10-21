package com.smartstock.dao;

import com.smartstock.exception.DatabaseException;
import com.smartstock.model.Supplier;
import com.smartstock.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SupplierDAOImpl implements SupplierDAO {

    @Override
    public Supplier save(Supplier supplier) {

        String sql = """
                INSERT INTO suppliers
                (supplier_name, phone, email, address)
                VALUES (?, ?, ?, ?)
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            statement.setString(
                    1,
                    supplier.getSupplierName()
            );

            statement.setString(
                    2,
                    supplier.getPhone()
            );

            statement.setString(
                    3,
                    supplier.getEmail()
            );

            statement.setString(
                    4,
                    supplier.getAddress()
            );

            int affectedRows =
                    statement.executeUpdate();

            if (affectedRows == 0) {
                throw new DatabaseException(
                        "Creating supplier failed."
                );
            }

            try (
                    ResultSet keys =
                            statement.getGeneratedKeys()
            ) {

                if (keys.next()) {

                    supplier.setSupplierId(
                            keys.getInt(1)
                    );

                    return supplier;
                }

                throw new DatabaseException(
                        "Creating supplier failed: no generated ID returned."
                );
            }

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Failed to save supplier: "
                            + e.getMessage(),
                    e
            );
        }
    }

    @Override
    public Optional<Supplier> findById(
            int supplierId
    ) {

        String sql = """
                SELECT supplier_id,
                       supplier_name,
                       phone,
                       email,
                       address
                FROM suppliers
                WHERE supplier_id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    supplierId
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return Optional.of(
                            mapRow(resultSet)
                    );
                }

                return Optional.empty();
            }

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Failed to find supplier: "
                            + e.getMessage(),
                    e
            );
        }
    }

    @Override
    public List<Supplier> findAll() {

        String sql = """
                SELECT supplier_id,
                       supplier_name,
                       phone,
                       email,
                       address
                FROM suppliers
                ORDER BY supplier_name
                """;

        List<Supplier> suppliers =
                new ArrayList<>();

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                suppliers.add(
                        mapRow(resultSet)
                );
            }

            return suppliers;

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Failed to fetch suppliers: "
                            + e.getMessage(),
                    e
            );
        }
    }

    @Override
    public boolean update(
            Supplier supplier
    ) {

        String sql = """
                UPDATE suppliers
                SET supplier_name = ?,
                    phone = ?,
                    email = ?,
                    address = ?
                WHERE supplier_id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    supplier.getSupplierName()
            );

            statement.setString(
                    2,
                    supplier.getPhone()
            );

            statement.setString(
                    3,
                    supplier.getEmail()
            );

            statement.setString(
                    4,
                    supplier.getAddress()
            );

            statement.setInt(
                    5,
                    supplier.getSupplierId()
            );

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Failed to update supplier: "
                            + e.getMessage(),
                    e
            );
        }
    }

    @Override
    public boolean deleteById(
            int supplierId
    ) {

        String sql = """
                DELETE FROM suppliers
                WHERE supplier_id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    supplierId
            );

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Failed to delete supplier: "
                            + e.getMessage(),
                    e
            );
        }
    }

    private Supplier mapRow(
            ResultSet resultSet
    ) throws SQLException {

        return new Supplier(
                resultSet.getInt(
                        "supplier_id"
                ),

                resultSet.getString(
                        "supplier_name"
                ),

                resultSet.getString(
                        "phone"
                ),

                resultSet.getString(
                        "email"
                ),

                resultSet.getString(
                        "address"
                )
        );
    }
}