package com.smartstock.dao;

import com.smartstock.exception.DatabaseException;
import com.smartstock.model.ProductSalesSummary;
import com.smartstock.model.Sale;
import com.smartstock.model.SaleItem;
import com.smartstock.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SaleDAOImpl implements SaleDAO {

    @Override
    public Sale createSale(Sale sale) {

        if (sale == null) {
            throw new DatabaseException(
                    "Sale cannot be null."
            );
        }

        if (sale.getItems() == null ||
                sale.getItems().isEmpty()) {

            throw new DatabaseException(
                    "A sale must contain at least one item."
            );
        }

        sale.calculateTotal();

        Connection connection = null;

        try {
            connection =
                    DatabaseConnection.getConnection();

            connection.setAutoCommit(false);

            int saleId = insertSale(
                    connection,
                    sale
            );

            for (SaleItem item : sale.getItems()) {

                insertSaleItem(
                        connection,
                        saleId,
                        item
                );

                decreaseProductStock(
                        connection,
                        item
                );
            }

            connection.commit();

            sale.setSaleId(saleId);

            return sale;

        } catch (SQLException e) {

            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ignored) {
                }
            }

            throw new DatabaseException(
                    "Failed to create sale: "
                            + e.getMessage(),
                    e
            );

        } finally {

            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    private int insertSale(
            Connection connection,
            Sale sale
    ) throws SQLException {

        String sql = """
                INSERT INTO sales
                (total_amount)
                VALUES (?)
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS
                     )) {

            statement.setBigDecimal(
                    1,
                    sale.getTotalAmount()
            );

            int affectedRows =
                    statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException(
                        "Creating sale failed."
                );
            }

            try (ResultSet keys =
                         statement.getGeneratedKeys()) {

                if (keys.next()) {
                    return keys.getInt(1);
                }

                throw new SQLException(
                        "No sale ID was generated."
                );
            }
        }
    }

    private void insertSaleItem(
            Connection connection,
            int saleId,
            SaleItem item
    ) throws SQLException {

        String sql = """
                INSERT INTO sale_items
                (sale_id, product_id, quantity, unit_price)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    saleId
            );

            statement.setInt(
                    2,
                    item.getProductId()
            );

            statement.setInt(
                    3,
                    item.getQuantity()
            );

            statement.setBigDecimal(
                    4,
                    item.getUnitPrice()
            );

            statement.executeUpdate();
        }
    }

    private void decreaseProductStock(
            Connection connection,
            SaleItem item
    ) throws SQLException {

        String checkSql = """
                SELECT quantity
                FROM products
                WHERE product_id = ?
                FOR UPDATE
                """;

        int currentStock;

        try (PreparedStatement statement =
                     connection.prepareStatement(checkSql)) {

            statement.setInt(
                    1,
                    item.getProductId()
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (!resultSet.next()) {

                    throw new SQLException(
                            "Product with ID "
                                    + item.getProductId()
                                    + " does not exist."
                    );
                }

                currentStock =
                        resultSet.getInt("quantity");
            }
        }

        if (item.getQuantity() <= 0) {

            throw new SQLException(
                    "Sale quantity must be greater than zero."
            );
        }

        if (item.getQuantity() > currentStock) {

            throw new SQLException(
                    "Insufficient stock for product ID "
                            + item.getProductId()
                            + ". Available: "
                            + currentStock
                            + ", requested: "
                            + item.getQuantity()
            );
        }

        String updateSql = """
                UPDATE products
                SET quantity = quantity - ?
                WHERE product_id = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(updateSql)) {

            statement.setInt(
                    1,
                    item.getQuantity()
            );

            statement.setInt(
                    2,
                    item.getProductId()
            );

            int affectedRows =
                    statement.executeUpdate();

            if (affectedRows == 0) {

                throw new SQLException(
                        "Failed to update product stock."
                );
            }
        }
    }

    @Override
    public Optional<Sale> findById(int saleId) {

        String sql = """
                SELECT sale_id,
                       sale_date,
                       total_amount
                FROM sales
                WHERE sale_id = ?
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    saleId
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (!resultSet.next()) {
                    return Optional.empty();
                }

                Sale sale = new Sale(
                        resultSet.getInt("sale_id"),
                        resultSet.getTimestamp("sale_date"),
                        resultSet.getBigDecimal(
                                "total_amount"
                        )
                );

                loadSaleItems(
                        connection,
                        sale
                );

                return Optional.of(sale);
            }

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Failed to find sale: "
                            + e.getMessage(),
                    e
            );
        }
    }

    @Override
    public List<Sale> findAll() {

        String sql = """
                SELECT sale_id,
                       sale_date,
                       total_amount
                FROM sales
                ORDER BY sale_date DESC
                """;

        List<Sale> sales =
                new ArrayList<>();

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                Sale sale = new Sale(
                        resultSet.getInt("sale_id"),
                        resultSet.getTimestamp("sale_date"),
                        resultSet.getBigDecimal(
                                "total_amount"
                        )
                );

                sales.add(sale);
            }

            return sales;

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Failed to fetch sales: "
                            + e.getMessage(),
                    e
            );
        }
    }

    @Override
    public List<ProductSalesSummary> findProductSalesSummary() {

        String sql = """
                SELECT
                    p.product_id,
                    p.product_name,
                    p.category,
                    p.quantity AS current_stock,
                    p.reorder_level,

                    COALESCE(
                        SUM(si.quantity),
                        0
                    ) AS units_sold,

                    COUNT(
                        DISTINCT si.sale_item_id
                    ) AS transaction_count,

                    COALESCE(
                        SUM(
                            si.quantity * si.unit_price
                        ),
                        0
                    ) AS revenue,

                    MIN(
                        s.sale_date
                    ) AS first_sale_date,

                    MAX(
                        s.sale_date
                    ) AS last_sale_date

                FROM products p

                LEFT JOIN sale_items si
                    ON p.product_id = si.product_id

                LEFT JOIN sales s
                    ON si.sale_id = s.sale_id

                GROUP BY
                    p.product_id,
                    p.product_name,
                    p.category,
                    p.quantity,
                    p.reorder_level

                ORDER BY
                    units_sold DESC,
                    revenue DESC,
                    p.product_name
                """;

        List<ProductSalesSummary> summaries =
                new ArrayList<>();

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                ProductSalesSummary summary =
                        new ProductSalesSummary(
                                resultSet.getInt(
                                        "product_id"
                                ),

                                resultSet.getString(
                                        "product_name"
                                ),

                                resultSet.getString(
                                        "category"
                                ),

                                resultSet.getInt(
                                        "current_stock"
                                ),

                                resultSet.getInt(
                                        "reorder_level"
                                ),

                                resultSet.getInt(
                                        "units_sold"
                                ),

                                resultSet.getInt(
                                        "transaction_count"
                                ),

                                resultSet.getBigDecimal(
                                        "revenue"
                                ),

                                resultSet.getTimestamp(
                                        "first_sale_date"
                                ),

                                resultSet.getTimestamp(
                                        "last_sale_date"
                                )
                        );

                summaries.add(summary);
            }

            return summaries;

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Failed to fetch product sales summary: "
                            + e.getMessage(),
                    e
            );
        }
    }

    private void loadSaleItems(
            Connection connection,
            Sale sale
    ) throws SQLException {

        String sql = """
                SELECT sale_item_id,
                       sale_id,
                       product_id,
                       quantity,
                       unit_price
                FROM sale_items
                WHERE sale_id = ?
                ORDER BY sale_item_id
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    sale.getSaleId()
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    SaleItem item =
                            new SaleItem(
                                    resultSet.getInt(
                                            "sale_item_id"
                                    ),

                                    resultSet.getInt(
                                            "sale_id"
                                    ),

                                    resultSet.getInt(
                                            "product_id"
                                    ),

                                    resultSet.getInt(
                                            "quantity"
                                    ),

                                    resultSet.getBigDecimal(
                                            "unit_price"
                                    )
                            );

                    sale.addItem(item);
                }
            }
        }
    }
}