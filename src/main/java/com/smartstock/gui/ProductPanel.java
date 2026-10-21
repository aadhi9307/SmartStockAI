package com.smartstock.gui;

import com.smartstock.model.Product;
import com.smartstock.service.ProductService;
import com.smartstock.service.ProductServiceImpl;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ProductPanel extends JPanel {

    private final ProductService productService;

    private final JTextField searchField =
            new JTextField();

    private final JComboBox<String> categoryFilter =
            new JComboBox<>();

    private final JComboBox<String> stockFilter =
            new JComboBox<>(
                    new String[]{
                            "All Stock",
                            "Available",
                            "Low Stock",
                            "Out of Stock"
                    }
            );

    private final DefaultTableModel tableModel;

    private final JTable productTable;

    private final JLabel resultLabel =
            new JLabel("Loading products...");

    private final JButton refreshButton =
            new JButton("Refresh");

    private final JButton addButton =
            new JButton("+ Add Product");

    private List<Product> allProducts;

    public ProductPanel() {

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

        /*
         * IMPORTANT:
         * Create the model and JTable BEFORE creating
         * the table/card components.
         */
        tableModel =
                new DefaultTableModel(
                        new Object[]{
                                "ID",
                                "PRODUCT",
                                "CATEGORY",
                                "PRICE",
                                "STOCK",
                                "REORDER",
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

        productTable =
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
                createMainCard(),
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

        loadProducts();
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
                        "Products"
                );

        title.setForeground(
                Theme.TEXT_PRIMARY
        );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        28
                )
        );

        JLabel subtitle =
                new JLabel(
                        "Manage products, pricing and stock levels."
                );

        subtitle.setForeground(
                Theme.TEXT_SECONDARY
        );

        subtitle.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
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

        stylePrimaryButton(
                addButton
        );

        addButton.setPreferredSize(
                new Dimension(
                        125,
                        54
                )
        );

        header.add(
                titlePanel,
                BorderLayout.WEST
        );

        header.add(
                addButton,
                BorderLayout.EAST
        );

        return header;
    }

    // =========================================================
    // MAIN CARD
    // =========================================================

    private JPanel createMainCard() {

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
                createFilterBar(),
                BorderLayout.NORTH
        );

        /*
         * The table scroll pane is directly inside the card.
         * No second outer JScrollPane and no BoxLayout.
         */
        JScrollPane tableScroll =
                new JScrollPane(
                        productTable
                );

        tableScroll.setBorder(
                BorderFactory.createEmptyBorder()
        );

        tableScroll.setBackground(
                Theme.SURFACE
        );

        tableScroll.getViewport()
                .setBackground(
                        Theme.SURFACE
                );

        tableScroll.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );

        tableScroll.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        tableScroll.getVerticalScrollBar()
                .setUnitIncrement(16);

        card.add(
                tableScroll,
                BorderLayout.CENTER
        );

        return card;
    }

    // =========================================================
    // FILTER BAR
    // =========================================================

    private JPanel createFilterBar() {

        JPanel filterBar =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                9,
                                0
                        )
                );

        filterBar.setBackground(
                Theme.SURFACE_ALT
        );

        filterBar.setBorder(
                new EmptyBorder(
                        12,
                        14,
                        12,
                        14
                )
        );

        JLabel filterLabel =
                new JLabel(
                        "Search"
                );

        filterLabel.setForeground(
                Theme.TEXT_SECONDARY
        );

        filterLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        11
                )
        );

        searchField.setPreferredSize(
                new Dimension(
                        280,
                        38
                )
        );

        searchField.setToolTipText(
                "Search by product name or category"
        );

        categoryFilter.setPreferredSize(
                new Dimension(
                        140,
                        38
                )
        );

        stockFilter.setPreferredSize(
                new Dimension(
                        130,
                        38
                )
        );

        filterBar.add(
                filterLabel
        );

        filterBar.add(
                searchField
        );

        filterBar.add(
                categoryFilter
        );

        filterBar.add(
                stockFilter
        );

        return filterBar;
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

        resultLabel.setForeground(
                Theme.TEXT_SECONDARY
        );

        resultLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        11
                )
        );

        styleSecondaryButton(
                refreshButton
        );

        refreshButton.setPreferredSize(
                new Dimension(
                        92,
                        38
                )
        );

        footer.add(
                resultLabel,
                BorderLayout.WEST
        );

        footer.add(
                refreshButton,
                BorderLayout.EAST
        );

        return footer;
    }

    // =========================================================
    // TABLE
    // =========================================================

    private void configureTable() {

        productTable.setRowHeight(
                48
        );

        productTable.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        productTable.setForeground(
                Theme.TEXT_PRIMARY
        );

        productTable.setBackground(
                Theme.SURFACE
        );

        productTable.setSelectionBackground(
                new Color(
                        238,
                        242,
                        255
                )
        );

        productTable.setSelectionForeground(
                Theme.TEXT_PRIMARY
        );

        productTable.setShowGrid(
                false
        );

        productTable.setIntercellSpacing(
                new Dimension(
                        0,
                        0
                )
        );

        productTable.setFillsViewportHeight(
                true
        );

        productTable.setAutoCreateRowSorter(
                true
        );

        JTableHeaderStyle();

        productTable.setDefaultRenderer(
                Object.class,
                new ProductCellRenderer()
        );

        productTable.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(55);

        productTable.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(240);

        productTable.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(170);

        productTable.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(120);

        productTable.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(100);

        productTable.getColumnModel()
                .getColumn(5)
                .setPreferredWidth(110);

        productTable.getColumnModel()
                .getColumn(6)
                .setPreferredWidth(130);

        productTable.getSelectionModel()
                .addListSelectionListener(
                        event -> {

                            if (
                                    event.getValueIsAdjusting()
                            ) {
                                return;
                            }

                            int selected =
                                    productTable
                                            .getSelectedRow();

                            if (selected < 0) {
                                return;
                            }

                            int modelRow =
                                    productTable
                                            .convertRowIndexToModel(
                                                    selected
                                            );

                            Object idValue =
                                    tableModel.getValueAt(
                                            modelRow,
                                            0
                                    );

                            if (
                                    idValue instanceof Integer
                            ) {

                                Product product =
                                        findProduct(
                                                (Integer) idValue
                                        );

                                if (product != null) {

                                    SwingUtilities.invokeLater(
                                            () ->
                                                    showProductDialog(
                                                            product
                                                    )
                                    );
                                }
                            }
                        }
                );
    }

    private void JTableHeaderStyle() {

        JTableHeader header =
                productTable.getTableHeader();

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

        header.setResizingAllowed(
                true
        );
    }

    // =========================================================
    // LISTENERS
    // =========================================================

    private void installListeners() {

        refreshButton.addActionListener(
                event ->
                        loadProducts()
        );

        addButton.addActionListener(
                event ->
                        showProductDialog(null)
        );

        searchField
                .getDocument()
                .addDocumentListener(
                        new DocumentListener() {

                            @Override
                            public void insertUpdate(
                                    DocumentEvent event
                            ) {
                                applyFilters();
                            }

                            @Override
                            public void removeUpdate(
                                    DocumentEvent event
                            ) {
                                applyFilters();
                            }

                            @Override
                            public void changedUpdate(
                                    DocumentEvent event
                            ) {
                                applyFilters();
                            }
                        }
                );

        categoryFilter.addActionListener(
                event ->
                        applyFilters()
        );

        stockFilter.addActionListener(
                event ->
                        applyFilters()
        );
    }

    // =========================================================
    // LOAD PRODUCTS
    // =========================================================

    private void loadProducts() {

        refreshButton.setEnabled(
                false
        );

        resultLabel.setText(
                "Loading products..."
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

                            populateCategories();

                            applyFilters();

                        } catch (Exception exception) {

                            resultLabel.setText(
                                    "Unable to load products."
                            );

                            JOptionPane.showMessageDialog(
                                    ProductPanel.this,
                                    extractMessage(
                                            exception
                                    ),
                                    "Product Loading Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                };

        worker.execute();
    }

    // =========================================================
    // CATEGORIES
    // =========================================================

    private void populateCategories() {

        String previous =
                categoryFilter.getSelectedItem() == null
                        ? "All Categories"
                        : categoryFilter
                        .getSelectedItem()
                        .toString();

        categoryFilter.removeAllItems();

        categoryFilter.addItem(
                "All Categories"
        );

        if (allProducts == null) {
            return;
        }

        Set<String> categories =
                new TreeSet<>(
                        String.CASE_INSENSITIVE_ORDER
                );

        for (Product product : allProducts) {

            if (
                    product != null
                            &&
                            product.getCategory() != null
                            &&
                            !product.getCategory()
                                    .isBlank()
            ) {

                categories.add(
                        product.getCategory()
                );
            }
        }

        for (String category : categories) {

            categoryFilter.addItem(
                    category
            );
        }

        /*
         * Always reset to All Categories unless
         * the previous selection still exists.
         */
        if (
                previous != null
                        &&
                        containsCategory(
                                previous
                        )
        ) {

            categoryFilter.setSelectedItem(
                    previous
            );

        } else {

            categoryFilter.setSelectedIndex(
                    0
            );
        }
    }

    private boolean containsCategory(
            String category
    ) {

        for (
                int i = 0;
                i < categoryFilter.getItemCount();
                i++
        ) {

            if (
                    category.equalsIgnoreCase(
                            categoryFilter.getItemAt(
                                    i
                            )
                    )
            ) {

                return true;
            }
        }

        return false;
    }

    // =========================================================
    // FILTER
    // =========================================================

    private void applyFilters() {

        if (allProducts == null) {

            tableModel.setRowCount(
                    0
            );

            resultLabel.setText(
                    "0 products found."
            );

            return;
        }

        String search =
                searchField
                        .getText()
                        .trim()
                        .toLowerCase();

        String category =
                categoryFilter.getSelectedItem() == null
                        ? "All Categories"
                        : categoryFilter
                        .getSelectedItem()
                        .toString();

        String stock =
                stockFilter.getSelectedItem() == null
                        ? "All Stock"
                        : stockFilter
                        .getSelectedItem()
                        .toString();

        tableModel.setRowCount(
                0
        );

        int count = 0;

        for (Product product : allProducts) {

            if (product == null) {
                continue;
            }

            String name =
                    product.getProductName() == null
                            ? ""
                            : product.getProductName()
                            .toLowerCase();

            String productCategory =
                    product.getCategory() == null
                            ? ""
                            : product.getCategory()
                            .toLowerCase();

            if (
                    !search.isBlank()
                            &&
                            !name.contains(search)
                            &&
                            !productCategory.contains(search)
            ) {

                continue;
            }

            if (
                    !category.equalsIgnoreCase(
                            "All Categories"
                    )
                            &&
                            (
                                    product.getCategory() == null
                                            ||
                                            !product.getCategory()
                                                    .equalsIgnoreCase(
                                                            category
                                                    )
                            )
            ) {

                continue;
            }

            if (
                    !matchesStockFilter(
                            product,
                            stock
                    )
            ) {

                continue;
            }

            tableModel.addRow(
                    new Object[]{
                            product.getProductId(),
                            product.getProductName(),
                            product.getCategory(),
                            formatCurrency(
                                    product.getPrice()
                            ),
                            product.getQuantity(),
                            product.getReorderLevel(),
                            getStatus(
                                    product
                            )
                    }
            );

            count++;
        }

        /*
         * Force Swing to recalculate the visible table
         * after changing the model.
         */
        tableModel.fireTableDataChanged();

        productTable.revalidate();
        productTable.repaint();

        resultLabel.setText(
                count
                        + (
                        count == 1
                                ? " product found."
                                : " products found."
                )
        );
    }

    private boolean matchesStockFilter(
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
    // PRODUCT DIALOG
    // =========================================================

    private void showProductDialog(
            Product existing
    ) {

        boolean editing =
                existing != null;

        JTextField nameField =
                new JTextField(
                        editing
                                ? existing.getProductName()
                                : ""
                );

        JTextField categoryField =
                new JTextField(
                        editing
                                ? existing.getCategory()
                                : ""
                );

        JTextField priceField =
                new JTextField(
                        editing
                                ? String.valueOf(
                                existing.getPrice()
                        )
                                : ""
                );

        JTextField quantityField =
                new JTextField(
                        editing
                                ? String.valueOf(
                                existing.getQuantity()
                        )
                                : ""
                );

        JTextField reorderField =
                new JTextField(
                        editing
                                ? String.valueOf(
                                existing.getReorderLevel()
                        )
                                : ""
                );

        JPanel form =
                new JPanel(
                        new GridLayout(
                                5,
                                2,
                                12,
                                12
                        )
                );

        form.setBackground(
                Theme.SURFACE
        );

        form.setBorder(
                new EmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        form.add(
                createFormLabel(
                        "Product Name"
                )
        );

        form.add(
                nameField
        );

        form.add(
                createFormLabel(
                        "Category"
                )
        );

        form.add(
                categoryField
        );

        form.add(
                createFormLabel(
                        "Unit Price"
                )
        );

        form.add(
                priceField
        );

        form.add(
                createFormLabel(
                        "Quantity"
                )
        );

        form.add(
                quantityField
        );

        form.add(
                createFormLabel(
                        "Reorder Level"
                )
        );

        form.add(
                reorderField
        );

        JButton saveButton =
                new JButton(
                        editing
                                ? "Save Changes"
                                : "Add Product"
                );

        stylePrimaryButton(
                saveButton
        );

        JButton deleteButton =
                new JButton(
                        "Delete"
                );

        styleDangerButton(
                deleteButton
        );

        JPanel buttons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        buttons.setOpaque(false);

        if (editing) {

            buttons.add(
                    deleteButton
            );
        }

        buttons.add(
                saveButton
        );

        JPanel wrapper =
                new JPanel(
                        new BorderLayout(
                                0,
                                14
                        )
                );

        wrapper.setBackground(
                Theme.SURFACE
        );

        wrapper.setBorder(
                new EmptyBorder(
                        8,
                        8,
                        8,
                        8
                )
        );

        wrapper.add(
                form,
                BorderLayout.CENTER
        );

        wrapper.add(
                buttons,
                BorderLayout.SOUTH
        );

        Window owner =
                SwingUtilities.getWindowAncestor(
                        this
                );

        JDialog dialog =
                new JDialog(
                        owner,
                        editing
                                ? "Edit Product"
                                : "Add Product",
                        Dialog.ModalityType.APPLICATION_MODAL
                );

        dialog.setContentPane(
                wrapper
        );

        dialog.setSize(
                540,
                390
        );

        dialog.setResizable(
                false
        );

        dialog.setLocationRelativeTo(
                this
        );

        saveButton.addActionListener(
                event -> {

                    try {

                        String name =
                                nameField
                                        .getText()
                                        .trim();

                        String category =
                                categoryField
                                        .getText()
                                        .trim();

                        if (
                                name.isBlank()
                                        ||
                                        category.isBlank()
                        ) {

                            throw new IllegalArgumentException(
                                    "Product name and category are required."
                            );
                        }

                        BigDecimal price =
                                new BigDecimal(
                                        priceField
                                                .getText()
                                                .trim()
                                );

                        int quantity =
                                Integer.parseInt(
                                        quantityField
                                                .getText()
                                                .trim()
                                );

                        int reorderLevel =
                                Integer.parseInt(
                                        reorderField
                                                .getText()
                                                .trim()
                                );

                        if (
                                price.compareTo(
                                        BigDecimal.ZERO
                                ) < 0
                        ) {

                            throw new IllegalArgumentException(
                                    "Price cannot be negative."
                            );
                        }

                        if (quantity < 0) {

                            throw new IllegalArgumentException(
                                    "Quantity cannot be negative."
                            );
                        }

                        if (reorderLevel < 0) {

                            throw new IllegalArgumentException(
                                    "Reorder level cannot be negative."
                            );
                        }

                        if (editing) {

                            Product updated =
                                    new Product(
                                            existing.getProductId(),
                                            name,
                                            category,
                                            price,
                                            quantity,
                                            reorderLevel,
                                            existing.getSupplierId()
                                    );

                            productService.updateProduct(
                                    updated
                            );

                        } else {

                            Product product =
                                    new Product(
                                            0,
                                            name,
                                            category,
                                            price,
                                            quantity,
                                            reorderLevel,
                                            null
                                    );

                            productService.createProduct(
                                    product
                            );
                        }

                        dialog.dispose();

                        loadProducts();

                    } catch (Exception exception) {

                        JOptionPane.showMessageDialog(
                                dialog,
                                extractMessage(
                                        exception
                                ),
                                "Product Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
        );

        deleteButton.addActionListener(
                event -> {

                    int result =
                            JOptionPane.showConfirmDialog(
                                    dialog,
                                    "Delete "
                                            + existing.getProductName()
                                            + "?",
                                    "Confirm Delete",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.WARNING_MESSAGE
                            );

                    if (
                            result
                                    ==
                                    JOptionPane.YES_OPTION
                    ) {

                        try {

                            productService.deleteProduct(
                                    existing.getProductId()
                            );

                            dialog.dispose();

                            loadProducts();

                        } catch (Exception exception) {

                            JOptionPane.showMessageDialog(
                                    dialog,
                                    extractMessage(
                                            exception
                                    ),
                                    "Delete Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                }
        );

        dialog.setVisible(
                true
        );
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

        label.setForeground(
                Theme.TEXT_SECONDARY
        );

        label.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        return label;
    }

    private Product findProduct(
            int id
    ) {

        if (allProducts == null) {
            return null;
        }

        for (Product product : allProducts) {

            if (
                    product != null
                            &&
                            product.getProductId() == id
            ) {

                return product;
            }
        }

        return null;
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

    private void styleDangerButton(
            JButton button
    ) {

        button.setBackground(
                new Color(
                        254,
                        226,
                        226
                )
        );

        button.setForeground(
                Theme.DANGER
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

    private static class ProductCellRenderer
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
                            12,
                            0,
                            12
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
                            column == 4
                            ||
                            column == 5
            ) {

                label.setHorizontalAlignment(
                        SwingConstants.CENTER
                );

            } else if (
                    column == 3
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
                    column == 6
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