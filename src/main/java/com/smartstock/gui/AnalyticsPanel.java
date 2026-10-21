package com.smartstock.gui;

import com.smartstock.model.Product;
import com.smartstock.model.Sale;
import com.smartstock.service.ProductService;
import com.smartstock.service.ProductServiceImpl;
import com.smartstock.service.SaleService;
import com.smartstock.service.SaleServiceImpl;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

public class AnalyticsPanel extends JPanel {

    private final ProductService productService;
    private final SaleService saleService;

    private JLabel totalProductsValue;
    private JLabel totalSalesValue;
    private JLabel totalRevenueValue;
    private JLabel inventoryValueValue;
    private JLabel lowStockValue;
    private JLabel outOfStockValue;

    private RevenueChart revenueChart;
    private JButton refreshButton;
    private JLabel statusLabel;

    private final DecimalFormat decimalFormat =
            new DecimalFormat("#,##0.00");

    public AnalyticsPanel() {

        productService =
                new ProductServiceImpl();

        saleService =
                new SaleServiceImpl();

        setLayout(
                new BorderLayout()
        );

        setBackground(
                Theme.BACKGROUND
        );

        setBorder(
                new EmptyBorder(
                        28,
                        30,
                        22,
                        30
                )
        );

        createInterface();

        loadAnalytics();
    }

    // =========================================================
    // INTERFACE
    // =========================================================

    private void createInterface() {

        JPanel root =
                new JPanel(
                        new BorderLayout(
                                0,
                                18
                        )
                );

        root.setOpaque(false);

        root.add(
                createHeader(),
                BorderLayout.NORTH
        );

        JPanel content =
                new JPanel(
                        new BorderLayout(
                                0,
                                18
                        )
                );

        content.setOpaque(false);

        content.add(
                createMetricCards(),
                BorderLayout.NORTH
        );

        revenueChart =
                new RevenueChart();

        content.add(
                revenueChart,
                BorderLayout.CENTER
        );

        root.add(
                content,
                BorderLayout.CENTER
        );

        add(
                root,
                BorderLayout.CENTER
        );
    }

    // =========================================================
    // HEADER
    // =========================================================

    private JPanel createHeader() {

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setOpaque(false);

        JPanel titlePanel =
                new JPanel();

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        titlePanel.setOpaque(false);

        JLabel title =
                new JLabel(
                        "Analytics"
                );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        28
                )
        );

        title.setForeground(
                Theme.TEXT_PRIMARY
        );

        JLabel subtitle =
                new JLabel(
                        "Monitor sales performance and inventory health."
                );

        subtitle.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        subtitle.setForeground(
                Theme.TEXT_SECONDARY
        );

        titlePanel.add(
                title
        );

        titlePanel.add(
                Box.createVerticalStrut(5)
        );

        titlePanel.add(
                subtitle
        );

        header.add(
                titlePanel,
                BorderLayout.WEST
        );

        JPanel rightPanel =
                new JPanel(
                        new BorderLayout()
                );

        rightPanel.setOpaque(false);

        refreshButton =
                createPrimaryButton(
                        "Refresh"
                );

        refreshButton.setPreferredSize(
                new Dimension(
                        105,
                        42
                )
        );

        refreshButton.addActionListener(
                event ->
                        loadAnalytics()
        );

        rightPanel.add(
                refreshButton,
                BorderLayout.NORTH
        );

        header.add(
                rightPanel,
                BorderLayout.EAST
        );

        return header;
    }

    // =========================================================
    // METRIC CARDS
    // =========================================================

    private JPanel createMetricCards() {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                2,
                                3,
                                14,
                                14
                        )
                );

        panel.setOpaque(false);

        totalProductsValue =
                createMetricValue(
                        "0"
                );

        totalSalesValue =
                createMetricValue(
                        "0"
                );

        totalRevenueValue =
                createMetricValue(
                        "₹0.00"
                );

        inventoryValueValue =
                createMetricValue(
                        "₹0.00"
                );

        lowStockValue =
                createMetricValue(
                        "0"
                );

        outOfStockValue =
                createMetricValue(
                        "0"
                );

        panel.add(
                createMetricCard(
                        "TOTAL PRODUCTS",
                        totalProductsValue,
                        Theme.PRIMARY
                )
        );

        panel.add(
                createMetricCard(
                        "TOTAL SALES",
                        totalSalesValue,
                        new Color(
                                37,
                                99,
                                235
                        )
                )
        );

        panel.add(
                createMetricCard(
                        "TOTAL REVENUE",
                        totalRevenueValue,
                        new Color(
                                16,
                                185,
                                129
                        )
                )
        );

        panel.add(
                createMetricCard(
                        "INVENTORY VALUE",
                        inventoryValueValue,
                        new Color(
                                124,
                                58,
                                237
                        )
                )
        );

        panel.add(
                createMetricCard(
                        "LOW STOCK",
                        lowStockValue,
                        Theme.WARNING
                )
        );

        panel.add(
                createMetricCard(
                        "OUT OF STOCK",
                        outOfStockValue,
                        Theme.DANGER
                )
        );

        return panel;
    }

    private JPanel createMetricCard(
            String title,
            JLabel value,
            Color accent
    ) {

        JPanel card =
                new JPanel(
                        new BorderLayout(
                                0,
                                8
                        )
                );

        card.setBackground(
                Theme.SURFACE
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                Theme.BORDER
                        ),
                        new EmptyBorder(
                                14,
                                18,
                                14,
                                18
                        )
                )
        );

        JPanel top =
                new JPanel(
                        new BorderLayout()
                );

        top.setOpaque(false);

        JLabel titleLabel =
                new JLabel(
                        title
                );

        titleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        10
                )
        );

        titleLabel.setForeground(
                Theme.TEXT_SECONDARY
        );

        JPanel accentPanel =
                new JPanel();

        accentPanel.setBackground(
                accent
        );

        accentPanel.setPreferredSize(
                new Dimension(
                        5,
                        22
                )
        );

        top.add(
                titleLabel,
                BorderLayout.WEST
        );

        top.add(
                accentPanel,
                BorderLayout.EAST
        );

        card.add(
                top,
                BorderLayout.NORTH
        );

        value.setForeground(
                Theme.TEXT_PRIMARY
        );

        card.add(
                value,
                BorderLayout.CENTER
        );

        return card;
    }

    private JLabel createMetricValue(
            String text
    ) {

        JLabel label =
                new JLabel(
                        text
                );

        label.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        22
                )
        );

        label.setForeground(
                Theme.TEXT_PRIMARY
        );

        return label;
    }

    // =========================================================
    // BUTTON
    // =========================================================

    private JButton createPrimaryButton(
            String text
    ) {

        JButton button =
                new JButton(
                        text
                );

        button.setBackground(
                Theme.PRIMARY
        );

        button.setForeground(
                Color.WHITE
        );

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        button.setFocusPainted(
                false
        );

        button.setBorderPainted(
                false
        );

        button.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        return button;
    }

    // =========================================================
    // LOAD ANALYTICS
    // =========================================================

    private void loadAnalytics() {

        refreshButton.setEnabled(
                false
        );

        refreshButton.setText(
                "Loading..."
        );

        SwingWorker<AnalyticsData, Void> worker =
                new SwingWorker<>() {

                    @Override
                    protected AnalyticsData doInBackground()
                            throws Exception {

                        List<Product> products =
                                productService
                                        .getAllProducts();

                        List<Sale> sales =
                                saleService
                                        .getAllSales();

                        return calculateAnalytics(
                                products,
                                sales
                        );
                    }

                    @Override
                    protected void done() {

                        try {

                            AnalyticsData data =
                                    get();

                            updateAnalytics(
                                    data
                            );

                            if (
                                    statusLabel != null
                            ) {

                                statusLabel.setText(
                                        "Analytics updated successfully."
                                );
                            }

                        } catch (Exception exception) {

                            if (
                                    statusLabel != null
                            ) {

                                statusLabel.setText(
                                        "Unable to load analytics."
                                );
                            }

                            JOptionPane.showMessageDialog(
                                    AnalyticsPanel.this,
                                    "Could not load analytics.\n\n"
                                            + getErrorMessage(
                                            exception
                                    ),
                                    "Analytics",
                                    JOptionPane.ERROR_MESSAGE
                            );

                        } finally {

                            refreshButton.setEnabled(
                                    true
                            );

                            refreshButton.setText(
                                    "Refresh"
                            );
                        }
                    }
                };

        worker.execute();
    }

    // =========================================================
    // CALCULATE
    // =========================================================

    private AnalyticsData calculateAnalytics(
            List<Product> products,
            List<Sale> sales
    ) {

        int totalProducts =
                products == null
                        ? 0
                        : products.size();

        int lowStock =
                0;

        int outOfStock =
                0;

        BigDecimal inventoryValue =
                BigDecimal.ZERO;

        if (
                products != null
        ) {

            for (
                    Product product :
                    products
            ) {

                if (
                        product == null
                ) {
                    continue;
                }

                if (
                        product.isOutOfStock()
                ) {

                    outOfStock++;

                } else if (
                        product.isLowStock()
                ) {

                    lowStock++;
                }

                if (
                        product.getPrice() != null
                ) {

                    inventoryValue =
                            inventoryValue.add(
                                    product.getPrice()
                                            .multiply(
                                                    BigDecimal.valueOf(
                                                            product.getQuantity()
                                                    )
                                            )
                            );
                }
            }
        }

        int totalSales =
                sales == null
                        ? 0
                        : sales.size();

        BigDecimal totalRevenue =
                BigDecimal.ZERO;

        if (
                sales != null
        ) {

            for (
                    Sale sale :
                    sales
            ) {

                if (
                        sale == null
                                ||
                                sale.getTotalAmount() == null
                ) {

                    continue;
                }

                totalRevenue =
                        totalRevenue.add(
                                sale.getTotalAmount()
                        );
            }
        }

        return new AnalyticsData(
                totalProducts,
                totalSales,
                totalRevenue,
                inventoryValue,
                lowStock,
                outOfStock,
                sales
        );
    }

    // =========================================================
    // UPDATE UI
    // =========================================================

    private void updateAnalytics(
            AnalyticsData data
    ) {

        totalProductsValue.setText(
                String.valueOf(
                        data.totalProducts
                )
        );

        totalSalesValue.setText(
                String.valueOf(
                        data.totalSales
                )
        );

        totalRevenueValue.setText(
                formatCurrency(
                        data.totalRevenue
                )
        );

        inventoryValueValue.setText(
                formatCurrency(
                        data.inventoryValue
                )
        );

        lowStockValue.setText(
                String.valueOf(
                        data.lowStock
                )
        );

        outOfStockValue.setText(
                String.valueOf(
                        data.outOfStock
                )
        );

        revenueChart.setSales(
                data.sales
        );

        revenueChart.repaint();
    }

    // =========================================================
    // CURRENCY
    // =========================================================

    private String formatCurrency(
            BigDecimal value
    ) {

        if (
                value == null
        ) {

            return "₹0.00";
        }

        return "₹"
                + decimalFormat.format(
                value
        );
    }

    // =========================================================
    // ERROR MESSAGE
    // =========================================================

    private String getErrorMessage(
            Exception exception
    ) {

        Throwable cause =
                exception.getCause();

        if (
                cause != null
                        &&
                        cause.getMessage() != null
        ) {

            return cause.getMessage();
        }

        if (
                exception.getMessage() != null
        ) {

            return exception.getMessage();
        }

        return "An unexpected error occurred.";
    }

    // =========================================================
    // DATA HOLDER
    // =========================================================

    private static class AnalyticsData {

        private final int totalProducts;
        private final int totalSales;
        private final BigDecimal totalRevenue;
        private final BigDecimal inventoryValue;
        private final int lowStock;
        private final int outOfStock;
        private final List<Sale> sales;

        private AnalyticsData(
                int totalProducts,
                int totalSales,
                BigDecimal totalRevenue,
                BigDecimal inventoryValue,
                int lowStock,
                int outOfStock,
                List<Sale> sales
        ) {

            this.totalProducts =
                    totalProducts;

            this.totalSales =
                    totalSales;

            this.totalRevenue =
                    totalRevenue;

            this.inventoryValue =
                    inventoryValue;

            this.lowStock =
                    lowStock;

            this.outOfStock =
                    outOfStock;

            this.sales =
                    sales;
        }
    }

    // =========================================================
    // REVENUE CHART
    // =========================================================

    private class RevenueChart
            extends JPanel {

        private List<Sale> sales;

        private final DecimalFormat chartFormat =
                new DecimalFormat(
                        "#,##0.00"
                );

        RevenueChart() {

            setBackground(
                    Theme.SURFACE
            );

            setBorder(
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(
                                    Theme.BORDER
                            ),
                            new EmptyBorder(
                                    18,
                                    20,
                                    16,
                                    20
                            )
                    )
            );

            setPreferredSize(
                    new Dimension(
                            700,
                            330
                    )
            );
        }

        void setSales(
                List<Sale> sales
        ) {

            this.sales =
                    sales;

            repaint();
        }

        @Override
        protected void paintComponent(
                Graphics graphics
        ) {

            super.paintComponent(
                    graphics
            );

            Graphics2D g =
                    (Graphics2D)
                            graphics.create();

            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g.setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON
            );

            // -------------------------------------------------
            // TITLE
            // -------------------------------------------------

            g.setColor(
                    Theme.TEXT_PRIMARY
            );

            g.setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            17
                    )
            );

            g.drawString(
                    "Sales Revenue",
                    20,
                    28
            );

            // -------------------------------------------------
            // NO DATA
            // -------------------------------------------------

            if (
                    sales == null
                            ||
                            sales.isEmpty()
            ) {

                g.setColor(
                        Theme.TEXT_SECONDARY
                );

                g.setFont(
                        new Font(
                                "SansSerif",
                                Font.PLAIN,
                                13
                        )
                );

                g.drawString(
                        "No sales data available yet.",
                        20,
                        62
                );

                g.dispose();

                return;
            }

            // -------------------------------------------------
            // FILTER VALID SALES
            // -------------------------------------------------

            int validCount =
                    0;

            BigDecimal maxRevenue =
                    BigDecimal.ZERO;

            for (
                    Sale sale :
                    sales
            ) {

                if (
                        sale == null
                                ||
                                sale.getTotalAmount() == null
                ) {

                    continue;
                }

                validCount++;

                if (
                        sale.getTotalAmount()
                                .compareTo(
                                        maxRevenue
                                ) > 0
                ) {

                    maxRevenue =
                            sale.getTotalAmount();
                }
            }

            if (
                    validCount == 0
                            ||
                            maxRevenue.compareTo(
                                    BigDecimal.ZERO
                            ) <= 0
            ) {

                g.setColor(
                        Theme.TEXT_SECONDARY
                );

                g.setFont(
                        new Font(
                                "SansSerif",
                                Font.PLAIN,
                                13
                        )
                );

                g.drawString(
                        "No revenue data available yet.",
                        20,
                        62
                );

                g.dispose();

                return;
            }

            // -------------------------------------------------
            // DIMENSIONS
            // -------------------------------------------------

            int width =
                    getWidth();

            int height =
                    getHeight();

            int left =
                    58;

            int right =
                    26;

            int top =
                    62;

            int bottom =
                    50;

            int chartWidth =
                    Math.max(
                            1,
                            width -
                                    left -
                                    right
                    );

            int chartHeight =
                    Math.max(
                            1,
                            height -
                                    top -
                                    bottom
                    );

            // -------------------------------------------------
            // BASELINE
            // -------------------------------------------------

            int baseline =
                    top +
                            chartHeight;

            g.setColor(
                    Theme.BORDER
            );

            g.drawLine(
                    left,
                    baseline,
                    width - right,
                    baseline
            );

            // -------------------------------------------------
            // BARS
            // -------------------------------------------------

            int barGap =
                    validCount <= 8
                            ? 18
                            : 10;

            int barWidth =
                    Math.max(
                            22,
                            (
                                    chartWidth -
                                            (
                                                    (validCount - 1)
                                                            * barGap
                                            )
                            )
                                    /
                                    validCount
                    );

            barWidth =
                    Math.min(
                            barWidth,
                            80
                    );

            int index =
                    0;

            for (
                    Sale sale :
                    sales
            ) {

                if (
                        sale == null
                                ||
                                sale.getTotalAmount() == null
                ) {

                    continue;
                }

                BigDecimal amount =
                        sale.getTotalAmount();

                double ratio =
                        amount.doubleValue()
                                /
                                maxRevenue.doubleValue();

                int barHeight =
                        (int)
                                (
                                        ratio *
                                                chartHeight
                                );

                barHeight =
                        Math.max(
                                barHeight,
                                2
                        );

                int x =
                        left +
                                index *
                                        (
                                                barWidth +
                                                        barGap
                                        );

                int y =
                        baseline -
                                barHeight;

                // BAR

                g.setColor(
                        Theme.PRIMARY
                );

                g.fillRoundRect(
                        x,
                        y,
                        barWidth,
                        barHeight,
                        8,
                        8
                );

                // AMOUNT

                g.setColor(
                        Theme.TEXT_SECONDARY
                );

                g.setFont(
                        new Font(
                                "SansSerif",
                                Font.PLAIN,
                                9
                        )
                );

                String amountText =
                        "₹"
                                +
                                chartFormat.format(
                                        amount
                                );

                int amountWidth =
                        g.getFontMetrics()
                                .stringWidth(
                                        amountText
                                );

                g.drawString(
                        amountText,
                        x +
                                (
                                        barWidth -
                                                amountWidth
                                )
                                        /
                                        2,
                        Math.max(
                                top - 5,
                                y - 8
                        )
                );

                // SALE LABEL

                String saleText =
                        "Sale "
                                +
                                sale.getSaleId();

                int saleWidth =
                        g.getFontMetrics()
                                .stringWidth(
                                        saleText
                                );

                g.drawString(
                        saleText,
                        x +
                                (
                                        barWidth -
                                                saleWidth
                                )
                                        /
                                        2,
                        baseline + 22
                );

                index++;
            }

            g.dispose();
        }
    }

    // =========================================================
    // STATUS
    // =========================================================

    {
        statusLabel =
                new JLabel(
                        "Analytics ready."
                );

        statusLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        11
                )
        );

        statusLabel.setForeground(
                Theme.TEXT_SECONDARY
        );
    }
}