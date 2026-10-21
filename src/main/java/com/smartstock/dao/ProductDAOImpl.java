package com.smartstock.dao;

import com.smartstock.exception.DatabaseException;
import com.smartstock.model.Product;
import com.smartstock.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDAOImpl implements ProductDAO {

    @Override
    public Product save(Product product) {

        String sql = """
                INSERT INTO products
                (product_name, category, price, quantity, reorder_level, supplier_id)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, product.getProductName());
            statement.setString(2, product.getCategory());
            statement.setBigDecimal(3, product.getPrice());
            statement.setInt(4, product.getQuantity());
            statement.setInt(5, product.getReorderLevel());

            if (product.getSupplierId() == null) {
                statement.setNull(6, Types.INTEGER);
            } else {
                statement.setInt(6, product.getSupplierId());
            }

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new DatabaseException("Creating product failed.");
            }

            try (ResultSet keys = statement.getGeneratedKeys()) {

                if (keys.next()) {
                    product.setProductId(keys.getInt(1));
                    return product;
                }

                throw new DatabaseException(
                        "Creating product failed: no generated ID returned."
                );
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to save product: " + e.getMessage(), e
            );
        }
    }

    @Override
    public Optional<Product> findById(int productId) {

        String sql = """
                SELECT product_id,
                       product_name,
                       category,
                       price,
                       quantity,
                       reorder_level,
                       supplier_id
                FROM products
                WHERE product_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, productId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }

                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to find product: " + e.getMessage(), e
            );
        }
    }

    @Override
    public List<Product> findAll() {

        String sql = """
                SELECT product_id,
                       product_name,
                       category,
                       price,
                       quantity,
                       reorder_level,
                       supplier_id
                FROM products
                ORDER BY product_name
                """;

        List<Product> products = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                products.add(mapRow(resultSet));
            }

            return products;

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to fetch products: " + e.getMessage(), e
            );
        }
    }

    @Override
    public boolean update(Product product) {

        String sql = """
                UPDATE products
                SET product_name = ?,
                    category = ?,
                    price = ?,
                    quantity = ?,
                    reorder_level = ?,
                    supplier_id = ?
                WHERE product_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, product.getProductName());
            statement.setString(2, product.getCategory());
            statement.setBigDecimal(3, product.getPrice());
            statement.setInt(4, product.getQuantity());
            statement.setInt(5, product.getReorderLevel());

            if (product.getSupplierId() == null) {
                statement.setNull(6, Types.INTEGER);
            } else {
                statement.setInt(6, product.getSupplierId());
            }

            statement.setInt(7, product.getProductId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to update product: " + e.getMessage(), e
            );
        }
    }

    @Override
    public boolean deleteById(int productId) {

        String sql = """
                DELETE FROM products
                WHERE product_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, productId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to delete product: " + e.getMessage(), e
            );
        }
    }

    @Override
    public List<Product> findLowStockProducts() {

        String sql = """
                SELECT product_id,
                       product_name,
                       category,
                       price,
                       quantity,
                       reorder_level,
                       supplier_id
                FROM products
                WHERE quantity <= reorder_level
                ORDER BY quantity ASC
                """;

        List<Product> products = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                products.add(mapRow(resultSet));
            }

            return products;

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to fetch low-stock products: " + e.getMessage(), e
            );
        }
    }

    private Product mapRow(ResultSet resultSet) throws SQLException {

        int supplierId = resultSet.getInt("supplier_id");

        Integer nullableSupplierId =
                resultSet.wasNull() ? null : supplierId;

        return new Product(
                resultSet.getInt("product_id"),
                resultSet.getString("product_name"),
                resultSet.getString("category"),
                resultSet.getBigDecimal("price"),
                resultSet.getInt("quantity"),
                resultSet.getInt("reorder_level"),
                nullableSupplierId
        );
    }
}