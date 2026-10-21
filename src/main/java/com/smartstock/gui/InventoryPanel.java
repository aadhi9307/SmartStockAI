package com.smartstock.gui;

import com.smartstock.model.Product;
import com.smartstock.service.ProductService;
import com.smartstock.service.ProductServiceImpl;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class InventoryPanel extends JPanel {

    private final ProductService productService;

    private final DefaultTableModel tableModel;

    private final JTable inventoryTable;

    private final JTextField searchField =
            new JTextField();

    private final JComboBox<String> statusFilter =
            new JComboBox<>(
                    new String[]{
                            "All Status",
                            "Available",
                            "Low Stock",
                            "Out of Stock"
                    }
            );

    private final JLabel productsValue =
            new JLabel("0");

    private final JLabel lowStockValue =
            new JLabel("0");

    private final JLabel outOfStockValue =
            new JLabel("0");

    private final JLabel stockValue =
            new JLabel("₹0.00");

    private final JLabel resultLabel =
            new JLabel();

    private final JButton refreshButton =
            new JButton("Refresh");

    private final JButton adjustButton =
            new JButton("Adjust Stock");

    private List<Product> allProducts;

    public InventoryPanel() {

        productService =
                new ProductServiceImpl();

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

        tableModel =
                new DefaultTableModel(
                        new Object[]{
                                "ID",
                                "PRODUCT",
                                "CATEGORY",
                                "CURRENT STOCK",
                                "REORDER LEVEL",
                                "UNIT PRICE",
                                "STOCK VALUE",
                                "STATUS"
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

        inventoryTable =
                new JTable(
                        tableModel
                );

        configureTable();

        JPanel root =
                new JPanel(
                        new BorderLayout(
                                0,
                                18
                        )
                );

        root.setBackground(
                Theme.BACKGROUND
        );

        root.add(
                createHeader(),
                BorderLayout.NORTH
        );

        root.add(
                createInventoryCard(),
                BorderLayout.CENTER
        );

        root.add(
                createFooter(),
                BorderLayout.SOUTH
        );

        add(
                root,
                BorderLayout.CENTER
        );

        installListeners();

        loadInventory();
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
                        "Inventory"
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
                        "Monitor stock levels and manage inventory movement."
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
    // INVENTORY CARD
    // =========================================================

    private JPanel createInventoryCard() {

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBackground(
                Theme.SURFACE
        );

        card.setBorder(
                BorderFactory.createLineBorder(
                        Theme.BORDER
                )
        );

        card.add(
                createSummaryBar(),
                BorderLayout.NORTH
        );

        JScrollPane scrollPane =
                new JScrollPane(
                        inventoryTable
                );

        scrollPane.setBorder(
                BorderFactory.createEmptyBorder()
        );

        scrollPane.getViewport()
                .setBackground(
                        Theme.SURFACE
                );

        scrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        scrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scrollPane.getVerticalScrollBar()
                .setUnitIncrement(16);

        card.add(
                scrollPane,
                BorderLayout.CENTER
        );

        return card;
    }

    // =========================================================
    // SUMMARY
    // =========================================================

    private JPanel createSummaryBar() {

        JPanel bar =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                1,
                                0
                        )
                );

        bar.setBackground(
                Theme.BORDER
        );

        bar.add(
                createSummaryCard(
                        "PRODUCTS",
                        productsValue,
                        Theme.PRIMARY
                )
        );

        bar.add(
                createSummaryCard(
                        "LOW STOCK",
                        lowStockValue,
                        Theme.WARNING
                )
        );

        bar.add(
                createSummaryCard(
                        "OUT OF STOCK",
                        outOfStockValue,
                        Theme.DANGER
                )
        );

        bar.add(
                createSummaryCard(
                        "STOCK VALUE",
                        stockValue,
                        Theme.SUCCESS
                )
        );

        return bar;
    }

    private JPanel createSummaryCard(
            String title,
            JLabel value,
            Color accent
    ) {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(
                Theme.SURFACE
        );

        panel.setBorder(
                new EmptyBorder(
                        10,
                        12,
                        10,
                        12
                )
        );

        JPanel accentPanel =
                new JPanel();

        accentPanel.setBackground(
                accent
        );

        accentPanel.setPreferredSize(
                new Dimension(
                        4,
                        28
                )
        );

        JPanel left =
                new JPanel(
                        new BorderLayout(
                                8,
                                0
                        )
                );

        left.setOpaque(false);

        JPanel text =
                new JPanel();

        text.setLayout(
                new BoxLayout(
                        text,
                        BoxLayout.Y_AXIS
                )
        );

        text.setOpaque(false);

        JLabel titleLabel =
                new JLabel(
                        title
                );

        titleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        9
                )
        );

        titleLabel.setForeground(
                Theme.TEXT_SECONDARY
        );

        value.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        18
                )
        );

        value.setForeground(
                Theme.TEXT_PRIMARY
        );

        text.add(
                titleLabel
        );

        text.add(
                Box.createVerticalStrut(4)
        );

        text.add(
                value
        );

        left.add(
                accentPanel,
                BorderLayout.WEST
        );

        left.add(
                text,
                BorderLayout.CENTER
        );

        panel.add(
                left,
                BorderLayout.CENTER
        );

        return panel;
    }

    // =========================================================
    // FOOTER
    // =========================================================

    private JPanel createFooter() {

        JPanel footer =
                new JPanel(
                        new BorderLayout()
                );

        footer.setOpaque(false);

        JPanel left =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                8,
                                0
                        )
                );

        left.setOpaque(false);

        JLabel filterLabel =
                new JLabel(
                        "Filter"
                );

        filterLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        11
                )
        );

        filterLabel.setForeground(
                Theme.TEXT_SECONDARY
        );

        searchField.setPreferredSize(
                new Dimension(
                        240,
                        38
                )
        );

        searchField.setToolTipText(
                "Search inventory"
        );

        statusFilter.setPreferredSize(
                new Dimension(
                        130,
                        38
                )
        );

        left.add(
                filterLabel
        );

        left.add(
                searchField
        );

        left.add(
                statusFilter
        );

        JPanel right =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        right.setOpaque(false);

        stylePrimaryButton(
                adjustButton
        );

        styleSecondaryButton(
                refreshButton
        );

        adjustButton.setPreferredSize(
                new Dimension(
                        110,
                        38
                )
        );

        refreshButton.setPreferredSize(
                new Dimension(
                        90,
                        38
                )
        );

        right.add(
                adjustButton
        );

        right.add(
                refreshButton
        );

        JPanel top =
                new JPanel(
                        new BorderLayout()
                );

        top.setOpaque(false);

        top.add(
                left,
                BorderLayout.WEST
        );

        top.add(
                right,
                BorderLayout.EAST
        );

        JPanel status =
                new JPanel(
                        new BorderLayout()
                );

        status.setOpaque(false);

        resultLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        11
                )
        );

        resultLabel.setForeground(
                Theme.TEXT_SECONDARY
        );

        status.add(
                resultLabel,
                BorderLayout.WEST
        );

        JPanel wrapper =
                new JPanel(
                        new BorderLayout(
                                0,
                                5
                        )
                );

        wrapper.setOpaque(false);

        wrapper.add(
                top,
                BorderLayout.NORTH
        );

        wrapper.add(
                status,
                BorderLayout.SOUTH
        );

        return wrapper;
    }

    // =========================================================
    // TABLE
    // =========================================================

    private void configureTable() {

        inventoryTable.setRowHeight(
                48
        );

        inventoryTable.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        inventoryTable.setForeground(
                Theme.TEXT_PRIMARY
        );

        inventoryTable.setBackground(
                Theme.SURFACE
        );

        inventoryTable.setSelectionBackground(
                new Color(
                        238,
                        242,
                        255
                )
        );

        inventoryTable.setSelectionForeground(
                Theme.TEXT_PRIMARY
        );

        inventoryTable.setShowGrid(
                false
        );

        inventoryTable.setIntercellSpacing(
                new Dimension(
                        0,
                        0
                )
        );

        inventoryTable.setFillsViewportHeight(
                true
        );

        inventoryTable.setAutoCreateRowSorter(
                true
        );

        JTableHeader header =
                inventoryTable.getTableHeader();

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
                Theme.SURFACE_ALT
        );

        header.setReorderingAllowed(
                false
        );

        inventoryTable.setDefaultRenderer(
                Object.class,
                new InventoryCellRenderer()
        );

        int[] widths = {
                55,
                190,
                150,
                125,
                125,
                120,
                125,
                120
        };

        for (
                int i = 0;
                i < widths.length;
                i++
        ) {

            inventoryTable
                    .getColumnModel()
                    .getColumn(i)
                    .setPreferredWidth(
                            widths[i]
                    );
        }
    }

    // =========================================================
    // LISTENERS
    // =========================================================

    private void installListeners() {

        refreshButton.addActionListener(
                event ->
                        loadInventory()
        );

        adjustButton.addActionListener(
                event ->
                        showAdjustStockDialog()
        );

        searchField
                .getDocument()
                .addDocumentListener(
                        new javax.swing.event.DocumentListener() {

                            @Override
                            public void insertUpdate(
                                    javax.swing.event.DocumentEvent event
                            ) {
                                applyFilters();
                            }

                            @Override
                            public void removeUpdate(
                                    javax.swing.event.DocumentEvent event
                            ) {
                                applyFilters();
                            }

                            @Override
                            public void changedUpdate(
                                    javax.swing.event.DocumentEvent event
                            ) {
                                applyFilters();
                            }
                        }
                );

        statusFilter.addActionListener(
                event ->
                        applyFilters()
        );
    }

    // =========================================================
    // LOAD
    // =========================================================

    private void loadInventory() {

        refreshButton.setEnabled(
                false
        );

        resultLabel.setText(
                "Loading inventory..."
        );

        SwingWorker<List<Product>, Void> worker =
                new SwingWorker<>() {

                    @Override
                    protected List<Product> doInBackground() {

                        return productService
                                .getAllProducts();
                    }

                    @Override
                    protected void done() {

                        refreshButton.setEnabled(
                                true
                        );

                        try {

                            allProducts =
                                    get();

                            updateSummary();

                            applyFilters();

                        } catch (Exception exception) {

                            resultLabel.setText(
                                    "Unable to load inventory."
                            );

                            JOptionPane.showMessageDialog(
                                    InventoryPanel.this,
                                    extractMessage(
                                            exception
                                    ),
                                    "Inventory Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                };

        worker.execute();
    }

    // =========================================================
    // SUMMARY
    // =========================================================

    private void updateSummary() {

        if (allProducts == null) {

            productsValue.setText("0");
            lowStockValue.setText("0");
            outOfStockValue.setText("0");
            stockValue.setText("₹0.00");

            return;
        }

        int total =
                allProducts.size();

        int low =
                0;

        int out =
                0;

        BigDecimal value =
                BigDecimal.ZERO;

        for (Product product : allProducts) {

            if (product == null) {
                continue;
            }

            if (
                    product.isOutOfStock()
            ) {

                out++;

            } else if (
                    product.isLowStock()
            ) {

                low++;
            }

            BigDecimal price =
                    product.getPrice();

            if (price != null) {

                value =
                        value.add(
                                price.multiply(
                                        BigDecimal.valueOf(
                                                product.getQuantity()
                                        )
                                )
                        );
            }
        }

        productsValue.setText(
                String.valueOf(total)
        );

        lowStockValue.setText(
                String.valueOf(low)
        );

        outOfStockValue.setText(
                String.valueOf(out)
        );

        stockValue.setText(
                formatCurrency(
                        value
                )
        );
    }

    // =========================================================
    // FILTERS
    // =========================================================

    private void applyFilters() {

        tableModel.setRowCount(
                0
        );

        if (allProducts == null) {

            resultLabel.setText(
                    "0 products in inventory."
            );

            return;
        }

        String search =
                searchField
                        .getText()
                        .trim()
                        .toLowerCase();

        String filter =
                statusFilter
                        .getSelectedItem() == null
                        ? "All Status"
                        : statusFilter
                        .getSelectedItem()
                        .toString();

        int count =
                0;

        for (Product product : allProducts) {

            if (product == null) {
                continue;
            }

            String name =
                    product.getProductName() == null
                            ? ""
                            : product.getProductName()
                            .toLowerCase();

            String category =
                    product.getCategory() == null
                            ? ""
                            : product.getCategory()
                            .toLowerCase();

            if (
                    !search.isBlank()
                            &&
                            !name.contains(search)
                            &&
                            !category.contains(search)
            ) {

                continue;
            }

            if (
                    !matchesStatus(
                            product,
                            filter
                    )
            ) {

                continue;
            }

            BigDecimal stockValue =
                    product.getPrice() == null
                            ? BigDecimal.ZERO
                            : product.getPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            product.getQuantity()
                                    )
                            );

            tableModel.addRow(
                    new Object[]{
                            product.getProductId(),
                            product.getProductName(),
                            product.getCategory(),
                            product.getQuantity(),
                            product.getReorderLevel(),
                            formatCurrency(
                                    product.getPrice()
                            ),
                            formatCurrency(
                                    stockValue
                            ),
                            getStatus(
                                    product
                            )
                    }
            );

            count++;
        }

        tableModel.fireTableDataChanged();

        inventoryTable.revalidate();
        inventoryTable.repaint();

        resultLabel.setText(
                count
                        + (
                        count == 1
                                ? " product in inventory."
                                : " products in inventory."
                )
        );
    }

    private boolean matchesStatus(
            Product product,
            String filter
    ) {

        return switch (filter) {

            case "Available" ->
                    !product.isLowStock()
                            &&
                            !product.isOutOfStock();

            case "Low Stock" ->
                    product.isLowStock()
                            &&
                            !product.isOutOfStock();

            case "Out of Stock" ->
                    product.isOutOfStock();

            default ->
                    true;
        };
    }

    // =========================================================
    // ADJUST STOCK
    // =========================================================

    private void showAdjustStockDialog() {

        if (
                allProducts == null
                        ||
                        allProducts.isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "There are no products available.",
                    "Adjust Stock",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        JComboBox<Product> productCombo =
                new JComboBox<>();

        for (Product product : allProducts) {

            productCombo.addItem(
                    product
            );
        }

        JTextField quantityField =
                new JTextField(
                        "1"
                );

        JComboBox<String> actionCombo =
                new JComboBox<>(
                        new String[]{
                                "Add Stock",
                                "Remove Stock"
                        }
                );

        JPanel form =
                new JPanel(
                        new GridLayout(
                                3,
                                2,
                                12,
                                12
                        )
                );

        form.setBorder(
                new EmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        form.setBackground(
                Theme.SURFACE
        );

        form.add(
                createFormLabel(
                        "Product"
                )
        );

        form.add(
                productCombo
        );

        form.add(
                createFormLabel(
                        "Action"
                )
        );

        form.add(
                actionCombo
        );

        form.add(
                createFormLabel(
                        "Quantity"
                )
        );

        form.add(
                quantityField
        );

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        form,
                        "Adjust Stock",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

        if (
                result
                        !=
                        JOptionPane.OK_OPTION
        ) {

            return;
        }

        try {

            Product selected =
                    (Product)
                            productCombo
                                    .getSelectedItem();

            if (selected == null) {
                return;
            }

            int amount =
                    Integer.parseInt(
                            quantityField
                                    .getText()
                                    .trim()
                    );

            if (amount <= 0) {

                throw new IllegalArgumentException(
                        "Quantity must be greater than zero."
                );
            }

            boolean add =
                    actionCombo
                            .getSelectedItem()
                            .equals(
                                    "Add Stock"
                            );

            int newQuantity;

            if (add) {

                newQuantity =
                        selected.getQuantity()
                                + amount;

            } else {

                newQuantity =
                        selected.getQuantity()
                                - amount;

                if (newQuantity < 0) {

                    throw new IllegalArgumentException(
                            "Stock cannot become negative."
                    );
                }
            }

            selected.setQuantity(
                    newQuantity
            );

            productService.updateProduct(
                    selected
            );

            loadInventory();

            JOptionPane.showMessageDialog(
                    this,
                    "Stock updated successfully.",
                    "Inventory",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception exception) {

            JOptionPane.showMessageDialog(
                    this,
                    extractMessage(
                            exception
                    ),
                    "Stock Adjustment Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // HELPERS
    // =========================================================

    private JLabel createFormLabel(
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
                        12
                )
        );

        label.setForeground(
                Theme.TEXT_SECONDARY
        );

        return label;
    }

    private String getStatus(
            Product product
    ) {

        if (product.isOutOfStock()) {
            return "OUT OF STOCK";
        }

        if (product.isLowStock()) {
            return "LOW STOCK";
        }

        return "AVAILABLE";
    }

    private String formatCurrency(
            BigDecimal value
    ) {

        if (value == null) {
            return "₹0.00";
        }

        return "₹"
                + value.setScale(
                2,
                RoundingMode.HALF_UP
        ).toPlainString();
    }

    private String extractMessage(
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

        return "Unknown error.";
    }

    // =========================================================
    // BUTTON STYLES
    // =========================================================

    private void stylePrimaryButton(
            JButton button
    ) {

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
    }

    private void styleSecondaryButton(
            JButton button
    ) {

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
                        12
                )
        );

        button.setFocusPainted(
                false
        );

        button.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );
    }

    // =========================================================
    // TABLE RENDERER
    // =========================================================

    private static class InventoryCellRenderer
            extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean selected,
                boolean focus,
                int row,
                int column
        ) {

            JLabel label =
                    (JLabel)
                            super.getTableCellRendererComponent(
                                    table,
                                    value,
                                    selected,
                                    focus,
                                    row,
                                    column
                            );

            label.setOpaque(
                    true
            );

            label.setBorder(
                    new EmptyBorder(
                            0,
                            10,
                            0,
                            10
                    )
            );

            label.setFont(
                    new Font(
                            "SansSerif",
                            Font.PLAIN,
                            12
                    )
            );

            if (selected) {

                label.setBackground(
                        new Color(
                                238,
                                242,
                                255
                        )
                );

                label.setForeground(
                        Theme.TEXT_PRIMARY
                );

            } else {

                label.setBackground(
                        Theme.SURFACE
                );

                label.setForeground(
                        Theme.TEXT_PRIMARY
                );
            }

            if (
                    column == 0
                            ||
                            column == 3
                            ||
                            column == 4
            ) {

                label.setHorizontalAlignment(
                        SwingConstants.CENTER
                );

            } else if (
                    column == 5
                            ||
                            column == 6
            ) {

                label.setHorizontalAlignment(
                        SwingConstants.RIGHT
                );

            } else {

                label.setHorizontalAlignment(
                        SwingConstants.LEFT
                );
            }

            if (
                    column == 7
                            &&
                            value != null
            ) {

                String status =
                        value.toString();

                label.setFont(
                        new Font(
                                "SansSerif",
                                Font.BOLD,
                                10
                        )
                );

                if (
                        status.equals(
                                "OUT OF STOCK"
                        )
                ) {

                    label.setForeground(
                            Theme.DANGER
                    );

                } else if (
                        status.equals(
                                "LOW STOCK"
                        )
                ) {

                    label.setForeground(
                            Theme.WARNING
                    );

                } else {

                    label.setForeground(
                            Theme.SUCCESS
                    );
                }
            }

            return label;
        }
    }
}