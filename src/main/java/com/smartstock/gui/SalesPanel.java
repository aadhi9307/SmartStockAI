package com.smartstock.gui;

import com.smartstock.exception.ValidationException;
import com.smartstock.model.Product;
import com.smartstock.model.Sale;
import com.smartstock.model.SaleItem;
import com.smartstock.service.ProductService;
import com.smartstock.service.ProductServiceImpl;
import com.smartstock.service.SaleService;
import com.smartstock.service.SaleServiceImpl;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

public class SalesPanel extends JPanel {

    private final ProductService productService;
    private final SaleService saleService;

    private JComboBox<Product> productComboBox;
    private JSpinner quantitySpinner;

    private JLabel stockLabel;
    private JLabel priceLabel;
    private JLabel totalLabel;
    private JLabel statusLabel;

    private JTable salesTable;
    private DefaultTableModel salesTableModel;

    private JButton createSaleButton;
    private JButton clearButton;
    private JButton refreshButton;

    private List<Product> products;

    public SalesPanel() {

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

        loadProducts();

        loadSalesHistory();
    }

    // =========================================================
    // MAIN INTERFACE
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
                createHeaderPanel(),
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
                createSaleFormPanel(),
                BorderLayout.NORTH
        );

        content.add(
                createSalesHistoryPanel(),
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

    private JPanel createHeaderPanel() {

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
                        "Sales"
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
                        "Create sales and track transaction history."
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

        return header;
    }

    // =========================================================
    // SALE FORM
    // =========================================================

    private JPanel createSaleFormPanel() {

        JPanel card =
                createCardPanel();

        card.setLayout(
                new BorderLayout(
                        0,
                        18
                )
        );

        JLabel title =
                new JLabel(
                        "Create New Sale"
                );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        18
                )
        );

        title.setForeground(
                Theme.TEXT_PRIMARY
        );

        card.add(
                title,
                BorderLayout.NORTH
        );

        JPanel form =
                new JPanel(
                        new GridBagLayout()
                );

        form.setOpaque(false);

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        5,
                        5,
                        8,
                        16
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.anchor =
                GridBagConstraints.WEST;

        // -----------------------------------------------------
        // PRODUCT
        // -----------------------------------------------------

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;

        form.add(
                createFieldLabel(
                        "Product"
                ),
                gbc
        );

        productComboBox =
                new JComboBox<>();

        productComboBox.setPreferredSize(
                new Dimension(
                        320,
                        40
                )
        );

        productComboBox.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        productComboBox.setRenderer(
                new ProductComboBoxRenderer()
        );

        productComboBox.addActionListener(
                event ->
                        updateSelectedProduct()
        );

        gbc.gridx = 1;
        gbc.weightx = 1;

        form.add(
                productComboBox,
                gbc
        );

        // -----------------------------------------------------
        // AVAILABLE STOCK
        // -----------------------------------------------------

        gbc.gridx = 2;
        gbc.weightx = 0;

        form.add(
                createFieldLabel(
                        "Available Stock"
                ),
                gbc
        );

        stockLabel =
                createValueLabel(
                        "0"
                );

        gbc.gridx = 3;
        gbc.weightx = 0.3;

        form.add(
                stockLabel,
                gbc
        );

        // -----------------------------------------------------
        // QUANTITY
        // -----------------------------------------------------

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;

        form.add(
                createFieldLabel(
                        "Quantity"
                ),
                gbc
        );

        quantitySpinner =
                new JSpinner(
                        new SpinnerNumberModel(
                                1,
                                1,
                                999999,
                                1
                        )
                );

        quantitySpinner.setPreferredSize(
                new Dimension(
                        140,
                        40
                )
        );

        quantitySpinner.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        quantitySpinner.addChangeListener(
                event ->
                        updateTotal()
        );

        gbc.gridx = 1;
        gbc.weightx = 1;

        form.add(
                quantitySpinner,
                gbc
        );

        // -----------------------------------------------------
        // UNIT PRICE
        // -----------------------------------------------------

        gbc.gridx = 2;
        gbc.weightx = 0;

        form.add(
                createFieldLabel(
                        "Unit Price"
                ),
                gbc
        );

        priceLabel =
                createValueLabel(
                        "₹0.00"
                );

        gbc.gridx = 3;

        form.add(
                priceLabel,
                gbc
        );

        // -----------------------------------------------------
        // TOTAL
        // -----------------------------------------------------

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;

        form.add(
                createFieldLabel(
                        "Sale Total"
                ),
                gbc
        );

        totalLabel =
                new JLabel(
                        "₹0.00"
                );

        totalLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        21
                )
        );

        totalLabel.setForeground(
                Theme.PRIMARY
        );

        gbc.gridx = 1;
        gbc.weightx = 1;

        form.add(
                totalLabel,
                gbc
        );

        // -----------------------------------------------------
        // STATUS
        // -----------------------------------------------------

        statusLabel =
                new JLabel(
                        "Ready to create a sale."
                );

        statusLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        statusLabel.setForeground(
                Theme.TEXT_SECONDARY
        );

        gbc.gridx = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1;

        form.add(
                statusLabel,
                gbc
        );

        card.add(
                form,
                BorderLayout.CENTER
        );

        // -----------------------------------------------------
        // BUTTONS
        // -----------------------------------------------------

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        buttonPanel.setOpaque(false);

        clearButton =
                createSecondaryButton(
                        "Clear"
                );

        clearButton.setPreferredSize(
                new Dimension(
                        100,
                        42
                )
        );

        clearButton.addActionListener(
                event ->
                        clearSaleForm()
        );

        createSaleButton =
                createPrimaryButton(
                        "Create Sale"
                );

        createSaleButton.setPreferredSize(
                new Dimension(
                        140,
                        42
                )
        );

        createSaleButton.addActionListener(
                event ->
                        createSale()
        );

        buttonPanel.add(
                clearButton
        );

        buttonPanel.add(
                createSaleButton
        );

        card.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        return card;
    }

    // =========================================================
    // SALES HISTORY
    // =========================================================

    private JPanel createSalesHistoryPanel() {

        JPanel card =
                createCardPanel();

        card.setLayout(
                new BorderLayout(
                        0,
                        14
                )
        );

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setOpaque(false);

        JLabel title =
                new JLabel(
                        "Sales History"
                );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        18
                )
        );

        title.setForeground(
                Theme.TEXT_PRIMARY
        );

        header.add(
                title,
                BorderLayout.WEST
        );

        refreshButton =
                createSecondaryButton(
                        "Refresh"
                );

        refreshButton.setPreferredSize(
                new Dimension(
                        100,
                        38
                )
        );

        refreshButton.addActionListener(
                event ->
                        loadSalesHistory()
        );

        header.add(
                refreshButton,
                BorderLayout.EAST
        );

        card.add(
                header,
                BorderLayout.NORTH
        );

        salesTableModel =
                new DefaultTableModel(
                        new Object[]{
                                "SALE ID",
                                "DATE",
                                "TOTAL AMOUNT"
                        },
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        salesTable =
                new JTable(
                        salesTableModel
                );

        configureSalesTable();

        JScrollPane scrollPane =
                new JScrollPane(
                        salesTable
                );

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        Theme.BORDER
                )
        );

        scrollPane.getViewport()
                .setBackground(
                        Theme.SURFACE
                );

        scrollPane.getVerticalScrollBar()
                .setUnitIncrement(16);

        card.add(
                scrollPane,
                BorderLayout.CENTER
        );

        return card;
    }

    private void configureSalesTable() {

        salesTable.setRowHeight(
                42
        );

        salesTable.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        salesTable.setForeground(
                Theme.TEXT_PRIMARY
        );

        salesTable.setBackground(
                Theme.SURFACE
        );

        salesTable.setShowGrid(
                false
        );

        salesTable.setIntercellSpacing(
                new Dimension(
                        0,
                        0
                )
        );

        salesTable.setFillsViewportHeight(
                true
        );

        salesTable.setAutoCreateRowSorter(
                true
        );

        salesTable.setSelectionBackground(
                new Color(
                        238,
                        242,
                        255
                )
        );

        salesTable.setSelectionForeground(
                Theme.TEXT_PRIMARY
        );

        JTableHeader header =
                salesTable.getTableHeader();

        header.setPreferredSize(
                new Dimension(
                        0,
                        42
                )
        );

        header.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        10
                )
        );

        header.setForeground(
                Theme.TEXT_SECONDARY
        );

        header.setBackground(
                Theme.SURFACE
        );

        header.setReorderingAllowed(
                false
        );

        DefaultTableCellRenderer center =
                new DefaultTableCellRenderer();

        center.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        center.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        salesTable
                .getColumnModel()
                .getColumn(0)
                .setCellRenderer(
                        center
                );

        salesTable
                .getColumnModel()
                .getColumn(2)
                .setCellRenderer(
                        center
                );

        salesTable
                .getColumnModel()
                .getColumn(0)
                .setPreferredWidth(
                        100
                );

        salesTable
                .getColumnModel()
                .getColumn(1)
                .setPreferredWidth(
                        260
                );

        salesTable
                .getColumnModel()
                .getColumn(2)
                .setPreferredWidth(
                        220
                );
    }

    // =========================================================
    // CARD
    // =========================================================

    private JPanel createCardPanel() {

        JPanel panel =
                new JPanel();

        panel.setBackground(
                Theme.SURFACE
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                Theme.BORDER
                        ),
                        new EmptyBorder(
                                18,
                                20,
                                18,
                                20
                        )
                )
        );

        return panel;
    }

    // =========================================================
    // LABELS
    // =========================================================

    private JLabel createFieldLabel(
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
                        11
                )
        );

        label.setForeground(
                Theme.TEXT_SECONDARY
        );

        return label;
    }

    private JLabel createValueLabel(
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
                        14
                )
        );

        label.setForeground(
                Theme.TEXT_PRIMARY
        );

        return label;
    }

    // =========================================================
    // BUTTONS
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

    private JButton createSecondaryButton(
            String text
    ) {

        JButton button =
                new JButton(
                        text
                );

        button.setBackground(
                Theme.SURFACE
        );

        button.setForeground(
                Theme.TEXT_PRIMARY
        );

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        11
                )
        );

        button.setFocusPainted(
                false
        );

        button.setBorder(
                BorderFactory.createLineBorder(
                        Theme.BORDER
                )
        );

        button.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        return button;
    }

    // =========================================================
    // LOAD PRODUCTS
    // =========================================================

    private void loadProducts() {

        setFormEnabled(
                false
        );

        statusLabel.setText(
                "Loading products..."
        );

        statusLabel.setForeground(
                Theme.TEXT_SECONDARY
        );

        SwingWorker<List<Product>, Void> worker =
                new SwingWorker<>() {

                    @Override
                    protected List<Product> doInBackground()
                            throws Exception {

                        return productService
                                .getAllProducts();
                    }

                    @Override
                    protected void done() {

                        try {

                            products =
                                    get();

                            productComboBox
                                    .removeAllItems();

                            if (
                                    products == null
                                            ||
                                            products.isEmpty()
                            ) {

                                productComboBox
                                        .addItem(null);

                                stockLabel.setText(
                                        "0"
                                );

                                priceLabel.setText(
                                        "₹0.00"
                                );

                                totalLabel.setText(
                                        "₹0.00"
                                );

                                statusLabel.setText(
                                        "No products available."
                                );

                                statusLabel.setForeground(
                                        Theme.DANGER
                                );

                                setFormEnabled(
                                        false
                                );

                                return;
                            }

                            for (
                                    Product product :
                                    products
                            ) {

                                if (
                                        product != null
                                ) {

                                    productComboBox
                                            .addItem(
                                                    product
                                            );
                                }
                            }

                            if (
                                    productComboBox
                                            .getItemCount()
                                            > 0
                            ) {

                                productComboBox
                                        .setSelectedIndex(
                                                0
                                        );
                            }

                            setFormEnabled(
                                    true
                            );

                            statusLabel.setText(
                                    "Ready to create a sale."
                            );

                            statusLabel.setForeground(
                                    Theme.TEXT_SECONDARY
                            );

                            updateSelectedProduct();

                        } catch (Exception exception) {

                            statusLabel.setText(
                                    "Unable to load products."
                            );

                            statusLabel.setForeground(
                                    Theme.DANGER
                            );

                            setFormEnabled(
                                    false
                            );

                            showErrorDialog(
                                    "Could not load products.",
                                    exception
                            );
                        }
                    }
                };

        worker.execute();
    }

    // =========================================================
    // PRODUCT SELECTION
    // =========================================================

    private void updateSelectedProduct() {

        Product selectedProduct =
                (Product)
                        productComboBox
                                .getSelectedItem();

        if (
                selectedProduct == null
        ) {

            stockLabel.setText(
                    "0"
            );

            priceLabel.setText(
                    "₹0.00"
            );

            totalLabel.setText(
                    "₹0.00"
            );

            return;
        }

        stockLabel.setText(
                String.valueOf(
                        selectedProduct.getQuantity()
                )
        );

        priceLabel.setText(
                formatCurrency(
                        selectedProduct.getPrice()
                )
        );

        updateTotal();

        if (
                selectedProduct.getQuantity()
                        <= 0
        ) {

            statusLabel.setText(
                    "This product is out of stock."
            );

            statusLabel.setForeground(
                    Theme.DANGER
            );

        } else if (
                selectedProduct.isLowStock()
        ) {

            statusLabel.setText(
                    "Low stock — review inventory before selling."
            );

            statusLabel.setForeground(
                    Theme.WARNING
            );

        } else {

            statusLabel.setText(
                    "Ready to create a sale."
            );

            statusLabel.setForeground(
                    Theme.TEXT_SECONDARY
            );
        }
    }

    // =========================================================
    // TOTAL
    // =========================================================

    private void updateTotal() {

        Product selectedProduct =
                (Product)
                        productComboBox
                                .getSelectedItem();

        if (
                selectedProduct == null
        ) {

            totalLabel.setText(
                    "₹0.00"
            );

            return;
        }

        int quantity =
                (Integer)
                        quantitySpinner
                                .getValue();

        BigDecimal total =
                selectedProduct
                        .getPrice()
                        .multiply(
                                BigDecimal.valueOf(
                                        quantity
                                )
                        );

        totalLabel.setText(
                formatCurrency(
                        total
                )
        );
    }

    // =========================================================
    // CREATE SALE
    // =========================================================

    private void createSale() {

        Product selectedProduct =
                (Product)
                        productComboBox
                                .getSelectedItem();

        if (
                selectedProduct == null
        ) {

            showWarning(
                    "Please select a product."
            );

            return;
        }

        int quantity =
                (Integer)
                        quantitySpinner
                                .getValue();

        if (
                quantity <= 0
        ) {

            showWarning(
                    "Quantity must be greater than zero."
            );

            return;
        }

        int availableStock =
                selectedProduct.getQuantity();

        if (
                availableStock <= 0
        ) {

            showWarning(
                    "The selected product is out of stock."
            );

            return;
        }

        if (
                quantity > availableStock
        ) {

            showWarning(
                    "Insufficient stock.\n\n"
                            + "Available: "
                            + availableStock
                            + "\nRequested: "
                            + quantity
            );

            return;
        }

        BigDecimal unitPrice =
                selectedProduct.getPrice();

        SaleItem saleItem =
                new SaleItem(
                        selectedProduct.getProductId(),
                        quantity,
                        unitPrice
                );

        Sale sale =
                new Sale();

        sale.addItem(
                saleItem
        );

        setFormEnabled(
                false
        );

        statusLabel.setText(
                "Creating sale..."
        );

        statusLabel.setForeground(
                Theme.TEXT_SECONDARY
        );

        SwingWorker<Sale, Void> worker =
                new SwingWorker<>() {

                    @Override
                    protected Sale doInBackground()
                            throws Exception {

                        return saleService
                                .createSale(
                                        sale
                                );
                    }

                    @Override
                    protected void done() {

                        try {

                            Sale createdSale =
                                    get();

                            statusLabel.setText(
                                    "Sale #"
                                            + createdSale
                                            .getSaleId()
                                            + " created successfully."
                            );

                            statusLabel.setForeground(
                                    Theme.SUCCESS
                            );

                            quantitySpinner.setValue(
                                    1
                            );

                            showSuccessDialog(
                                    createdSale,
                                    selectedProduct,
                                    quantity
                            );

                            loadProducts();

                            loadSalesHistory();

                        } catch (Exception exception) {

                            Throwable cause =
                                    exception.getCause();

                            if (
                                    cause instanceof
                                            ValidationException
                            ) {

                                showWarning(
                                        cause.getMessage()
                                );

                            } else {

                                showErrorDialog(
                                        "Could not create the sale.",
                                        exception
                                );
                            }

                            setFormEnabled(
                                    true
                            );
                        }
                    }
                };

        worker.execute();
    }

    // =========================================================
    // SALES HISTORY
    // =========================================================

    private void loadSalesHistory() {

        if (
                salesTableModel == null
        ) {

            return;
        }

        refreshButton.setEnabled(
                false
        );

        SwingWorker<List<Sale>, Void> worker =
                new SwingWorker<>() {

                    @Override
                    protected List<Sale> doInBackground()
                            throws Exception {

                        return saleService
                                .getAllSales();
                    }

                    @Override
                    protected void done() {

                        try {

                            List<Sale> sales =
                                    get();

                            salesTableModel
                                    .setRowCount(
                                            0
                                    );

                            SimpleDateFormat
                                    dateFormat =
                                    new SimpleDateFormat(
                                            "dd MMM yyyy, hh:mm a"
                                    );

                            if (
                                    sales != null
                            ) {

                                for (
                                        Sale sale :
                                        sales
                                ) {

                                    if (
                                            sale == null
                                    ) {

                                        continue;
                                    }

                                    String dateText;

                                    if (
                                            sale.getSaleDate()
                                                    != null
                                    ) {

                                        dateText =
                                                dateFormat.format(
                                                        sale.getSaleDate()
                                                );

                                    } else {

                                        dateText =
                                                "Not available";
                                    }

                                    salesTableModel
                                            .addRow(
                                                    new Object[]{
                                                            sale.getSaleId(),
                                                            dateText,
                                                            formatCurrency(
                                                                    sale.getTotalAmount()
                                                            )
                                                    }
                                            );
                                }
                            }

                            salesTable.revalidate();
                            salesTable.repaint();

                            refreshButton.setEnabled(
                                    true
                            );

                        } catch (Exception exception) {

                            refreshButton.setEnabled(
                                    true
                            );

                            statusLabel.setText(
                                    "Unable to load sales history."
                            );

                            statusLabel.setForeground(
                                    Theme.DANGER
                            );
                        }
                    }
                };

        worker.execute();
    }

    // =========================================================
    // CLEAR
    // =========================================================

    private void clearSaleForm() {

        if (
                productComboBox.getItemCount()
                        > 0
        ) {

            productComboBox.setSelectedIndex(
                    0
            );
        }

        quantitySpinner.setValue(
                1
        );

        statusLabel.setText(
                "Ready to create a sale."
        );

        statusLabel.setForeground(
                Theme.TEXT_SECONDARY
        );

        updateSelectedProduct();
    }

    // =========================================================
    // FORM STATE
    // =========================================================

    private void setFormEnabled(
            boolean enabled
    ) {

        if (
                productComboBox != null
        ) {

            productComboBox.setEnabled(
                    enabled
            );
        }

        if (
                quantitySpinner != null
        ) {

            quantitySpinner.setEnabled(
                    enabled
            );
        }

        if (
                createSaleButton != null
        ) {

            createSaleButton.setEnabled(
                    enabled
            );
        }

        if (
                clearButton != null
        ) {

            clearButton.setEnabled(
                    enabled
            );
        }
    }

    // =========================================================
    // MESSAGES
    // =========================================================

    private void showWarning(
            String message
    ) {

        statusLabel.setText(
                message
        );

        statusLabel.setForeground(
                Theme.DANGER
        );

        JOptionPane.showMessageDialog(
                this,
                message,
                "Sales",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void showErrorDialog(
            String message,
            Exception exception
    ) {

        String detail =
                exception.getMessage();

        if (
                detail == null
                        ||
                        detail.isBlank()
        ) {

            detail =
                    "An unexpected error occurred.";
        }

        JOptionPane.showMessageDialog(
                this,
                message
                        + "\n\n"
                        + detail,
                "Sales",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void showSuccessDialog(
            Sale sale,
            Product product,
            int quantity
    ) {

        JOptionPane.showMessageDialog(
                this,
                "Sale created successfully.\n\n"
                        + "Sale ID: "
                        + sale.getSaleId()
                        + "\n"
                        + "Product: "
                        + product.getProductName()
                        + "\n"
                        + "Quantity: "
                        + quantity
                        + "\n"
                        + "Total: "
                        + formatCurrency(
                        sale.getTotalAmount()
                ),
                "Sale Completed",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =========================================================
    // CURRENCY
    // =========================================================

    private String formatCurrency(
            BigDecimal amount
    ) {

        if (
                amount == null
        ) {

            return "₹0.00";
        }

        return "₹"
                + String.format(
                "%.2f",
                amount.doubleValue()
        );
    }

    // =========================================================
    // PRODUCT COMBO RENDERER
    // =========================================================

    private static class ProductComboBoxRenderer
            extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(
                JList<?> list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus
        ) {

            super.getListCellRendererComponent(
                    list,
                    value,
                    index,
                    isSelected,
                    cellHasFocus
            );

            setBorder(
                    new EmptyBorder(
                            6,
                            8,
                            6,
                            8
                    )
            );

            if (
                    value instanceof Product product
            ) {

                setText(
                        product.getProductName()
                                + "  •  Stock: "
                                + product.getQuantity()
                );

            } else {

                setText(
                        "No product available"
                );
            }

            return this;
        }
    }
}