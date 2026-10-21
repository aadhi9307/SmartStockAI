package com.smartstock.gui;

import com.smartstock.exception.ValidationException;
import com.smartstock.model.Supplier;
import com.smartstock.service.SupplierService;
import com.smartstock.service.SupplierServiceImpl;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class SupplierPanel extends JPanel {

    private final SupplierService supplierService;

    private final JTextField searchField =
            new JTextField();

    private final JTextField nameField =
            new JTextField();

    private final JTextField phoneField =
            new JTextField();

    private final JTextField emailField =
            new JTextField();

    private final JTextArea addressField =
            new JTextArea();

    private final JTable supplierTable;

    private final DefaultTableModel tableModel;

    private final JLabel resultLabel =
            new JLabel("Loading suppliers...");

    private final JButton refreshButton =
            new JButton("Refresh");

    private int selectedSupplierId = -1;

    public SupplierPanel() {

        supplierService =
                new SupplierServiceImpl();

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
                                "SUPPLIER",
                                "PHONE",
                                "EMAIL",
                                "ADDRESS"
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

        supplierTable =
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

        root.setOpaque(false);

        root.add(
                createHeader(),
                BorderLayout.NORTH
        );

        root.add(
                createCenter(),
                BorderLayout.CENTER
        );

        add(
                root,
                BorderLayout.CENTER
        );

        installListeners();

        loadSuppliers();
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
                        "Suppliers"
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
                        "Manage your suppliers and contact information."
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

        JButton addButton =
                createPrimaryButton(
                        "+ Add Supplier"
                );

        addButton.setPreferredSize(
                new Dimension(
                        125,
                        54
                )
        );

        addButton.addActionListener(
                event ->
                        clearForm()
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
    // CENTER
    // =========================================================

    private JPanel createCenter() {

        JPanel center =
                new JPanel(
                        new BorderLayout(
                                0,
                                14
                        )
                );

        center.setOpaque(false);

        center.add(
                createSearchBar(),
                BorderLayout.NORTH
        );

        center.add(
                createTableCard(),
                BorderLayout.CENTER
        );

        center.add(
                createFormPanel(),
                BorderLayout.SOUTH
        );

        return center;
    }

    // =========================================================
    // SEARCH
    // =========================================================

    private JPanel createSearchBar() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                10,
                                0
                        )
                );

        panel.setBackground(
                Theme.SURFACE
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                Theme.BORDER
                        ),
                        new EmptyBorder(
                                10,
                                14,
                                10,
                                14
                        )
                )
        );

        JLabel label =
                new JLabel(
                        "Search"
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

        searchField.setPreferredSize(
                new Dimension(
                        350,
                        38
                )
        );

        searchField.setToolTipText(
                "Search supplier name, phone, email or address"
        );

        // Refresh button styling
        refreshButton.setBackground(
                Theme.SURFACE
        );

        refreshButton.setForeground(
                Theme.TEXT_PRIMARY
        );

        refreshButton.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        11
                )
        );

        refreshButton.setFocusPainted(
                false
        );

        refreshButton.setBorder(
                BorderFactory.createLineBorder(
                        Theme.BORDER
                )
        );

        refreshButton.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        refreshButton.setPreferredSize(
                new Dimension(
                        92,
                        38
                )
        );

        panel.add(
                label,
                BorderLayout.WEST
        );

        panel.add(
                searchField,
                BorderLayout.CENTER
        );

        panel.add(
                refreshButton,
                BorderLayout.EAST
        );

        return panel;
    }

    // =========================================================
    // TABLE
    // =========================================================

    private JPanel createTableCard() {

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

        JScrollPane scrollPane =
                new JScrollPane(
                        supplierTable
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

    private void configureTable() {

        supplierTable.setRowHeight(
                46
        );

        supplierTable.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        supplierTable.setForeground(
                Theme.TEXT_PRIMARY
        );

        supplierTable.setBackground(
                Theme.SURFACE
        );

        supplierTable.setShowGrid(
                false
        );

        supplierTable.setIntercellSpacing(
                new Dimension(
                        0,
                        0
                )
        );

        supplierTable.setFillsViewportHeight(
                true
        );

        supplierTable.setAutoCreateRowSorter(
                true
        );

        supplierTable.setSelectionBackground(
                new Color(
                        238,
                        242,
                        255
                )
        );

        supplierTable.setSelectionForeground(
                Theme.TEXT_PRIMARY
        );

        JTableHeader header =
                supplierTable.getTableHeader();

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

        supplierTable.setDefaultRenderer(
                Object.class,
                new SupplierCellRenderer()
        );

        int[] widths = {
                60,
                210,
                160,
                230,
                300
        };

        for (
                int i = 0;
                i < widths.length;
                i++
        ) {

            supplierTable
                    .getColumnModel()
                    .getColumn(i)
                    .setPreferredWidth(
                            widths[i]
                    );
        }
    }

    // =========================================================
    // FORM
    // =========================================================

    private JPanel createFormPanel() {

        JPanel card =
                new JPanel(
                        new BorderLayout(
                                16,
                                0
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
                                16,
                                18,
                                16,
                                18
                        )
                )
        );

        JPanel fields =
                new JPanel(
                        new GridBagLayout()
                );

        fields.setOpaque(false);

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        4,
                        4,
                        8,
                        14
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1;

        // NAME

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;

        fields.add(
                createFieldLabel(
                        "Supplier Name"
                ),
                gbc
        );

        gbc.gridx = 1;
        gbc.weightx = 1;

        configureTextField(
                nameField
        );

        fields.add(
                nameField,
                gbc
        );

        // PHONE

        gbc.gridx = 2;
        gbc.weightx = 0;

        fields.add(
                createFieldLabel(
                        "Phone"
                ),
                gbc
        );

        gbc.gridx = 3;
        gbc.weightx = 1;

        configureTextField(
                phoneField
        );

        fields.add(
                phoneField,
                gbc
        );

        // EMAIL

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;

        fields.add(
                createFieldLabel(
                        "Email"
                ),
                gbc
        );

        gbc.gridx = 1;
        gbc.weightx = 1;

        configureTextField(
                emailField
        );

        fields.add(
                emailField,
                gbc
        );

        // ADDRESS

        gbc.gridx = 2;
        gbc.weightx = 0;

        fields.add(
                createFieldLabel(
                        "Address"
                ),
                gbc
        );

        gbc.gridx = 3;
        gbc.weightx = 1;

        addressField.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        addressField.setLineWrap(
                true
        );

        addressField.setWrapStyleWord(
                true
        );

        addressField.setRows(
                2
        );

        JScrollPane addressScroll =
                new JScrollPane(
                        addressField
                );

        addressScroll.setPreferredSize(
                new Dimension(
                        220,
                        55
                )
        );

        fields.add(
                addressScroll,
                gbc
        );

        card.add(
                fields,
                BorderLayout.CENTER
        );

        card.add(
                createButtons(),
                BorderLayout.EAST
        );

        return card;
    }

    private JPanel createButtons() {

        JPanel buttons =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                7,
                                0
                        )
                );

        buttons.setOpaque(false);

        JButton clearButton =
                createSecondaryButton(
                        "Clear"
                );

        JButton deleteButton =
                createDangerButton(
                        "Delete"
                );

        JButton updateButton =
                createSecondaryButton(
                        "Update"
                );

        JButton saveButton =
                createPrimaryButton(
                        "Save"
                );

        clearButton.addActionListener(
                event ->
                        clearForm()
        );

        deleteButton.addActionListener(
                event ->
                        deleteSupplier()
        );

        updateButton.addActionListener(
                event ->
                        updateSupplier()
        );

        saveButton.addActionListener(
                event ->
                        saveSupplier()
        );

        buttons.add(
                clearButton
        );

        buttons.add(
                deleteButton
        );

        buttons.add(
                updateButton
        );

        buttons.add(
                saveButton
        );

        return buttons;
    }

    // =========================================================
    // LISTENERS
    // =========================================================

    private void installListeners() {

        refreshButton.addActionListener(
                event ->
                        loadSuppliers()
        );

        searchField
                .getDocument()
                .addDocumentListener(
                        new DocumentListener() {

                            @Override
                            public void insertUpdate(
                                    DocumentEvent event
                            ) {
                                applySearch();
                            }

                            @Override
                            public void removeUpdate(
                                    DocumentEvent event
                            ) {
                                applySearch();
                            }

                            @Override
                            public void changedUpdate(
                                    DocumentEvent event
                            ) {
                                applySearch();
                            }
                        }
                );

        supplierTable
                .getSelectionModel()
                .addListSelectionListener(
                        event -> {

                            if (
                                    !event.getValueIsAdjusting()
                            ) {

                                loadSelectedSupplier();
                            }
                        }
                );
    }

    // =========================================================
    // LOAD
    // =========================================================

    private void loadSuppliers() {

        refreshButton.setEnabled(
                false
        );

        resultLabel.setText(
                "Loading suppliers..."
        );

        SwingWorker<List<Supplier>, Void> worker =
                new SwingWorker<>() {

                    @Override
                    protected List<Supplier> doInBackground() {

                        return supplierService
                                .getAllSuppliers();
                    }

                    @Override
                    protected void done() {

                        refreshButton.setEnabled(
                                true
                        );

                        try {

                            List<Supplier> suppliers =
                                    get();

                            updateTable(
                                    suppliers
                            );

                            resultLabel.setText(
                                    suppliers.size()
                                            + (
                                            suppliers.size() == 1
                                                    ? " supplier found."
                                                    : " suppliers found."
                                    )
                            );

                        } catch (Exception exception) {

                            resultLabel.setText(
                                    "Unable to load suppliers."
                            );

                            showError(
                                    "Unable to load suppliers.",
                                    exception
                            );
                        }
                    }
                };

        worker.execute();
    }

    private void updateTable(
            List<Supplier> suppliers
    ) {

        tableModel.setRowCount(
                0
        );

        if (suppliers == null) {
            return;
        }

        for (Supplier supplier : suppliers) {

            tableModel.addRow(
                    new Object[]{
                            supplier.getSupplierId(),
                            safeValue(
                                    supplier.getSupplierName()
                            ),
                            safeValue(
                                    supplier.getPhone()
                            ),
                            safeValue(
                                    supplier.getEmail()
                            ),
                            safeValue(
                                    supplier.getAddress()
                            )
                    }
            );
        }

        tableModel.fireTableDataChanged();

        supplierTable.revalidate();
        supplierTable.repaint();
    }

    // =========================================================
    // SEARCH
    // =========================================================

    private void applySearch() {

        String query =
                searchField
                        .getText()
                        .trim()
                        .toLowerCase();

        SwingWorker<List<Supplier>, Void> worker =
                new SwingWorker<>() {

                    @Override
                    protected List<Supplier> doInBackground() {

                        return supplierService
                                .getAllSuppliers();
                    }

                    @Override
                    protected void done() {

                        try {

                            List<Supplier> suppliers =
                                    get();

                            tableModel.setRowCount(
                                    0
                            );

                            int count = 0;

                            for (
                                    Supplier supplier :
                                    suppliers
                            ) {

                                boolean matches =
                                        query.isEmpty()
                                                ||
                                                contains(
                                                        supplier.getSupplierName(),
                                                        query
                                                )
                                                ||
                                                contains(
                                                        supplier.getPhone(),
                                                        query
                                                )
                                                ||
                                                contains(
                                                        supplier.getEmail(),
                                                        query
                                                )
                                                ||
                                                contains(
                                                        supplier.getAddress(),
                                                        query
                                                );

                                if (!matches) {
                                    continue;
                                }

                                tableModel.addRow(
                                        new Object[]{
                                                supplier.getSupplierId(),
                                                safeValue(
                                                        supplier.getSupplierName()
                                                ),
                                                safeValue(
                                                        supplier.getPhone()
                                                ),
                                                safeValue(
                                                        supplier.getEmail()
                                                ),
                                                safeValue(
                                                        supplier.getAddress()
                                                )
                                        }
                                );

                                count++;
                            }

                            resultLabel.setText(
                                    count
                                            + (
                                            count == 1
                                                    ? " supplier found."
                                                    : " suppliers found."
                                    )
                            );

                            supplierTable.revalidate();
                            supplierTable.repaint();

                        } catch (Exception exception) {

                            showError(
                                    "Unable to search suppliers.",
                                    exception
                            );
                        }
                    }
                };

        worker.execute();
    }

    private boolean contains(
            String value,
            String query
    ) {

        return value != null
                &&
                value.toLowerCase()
                        .contains(
                                query
                        );
    }

    // =========================================================
    // SELECT
    // =========================================================

    private void loadSelectedSupplier() {

        int selectedRow =
                supplierTable.getSelectedRow();

        if (selectedRow < 0) {
            return;
        }

        int modelRow =
                supplierTable
                        .convertRowIndexToModel(
                                selectedRow
                        );

        Object idValue =
                tableModel.getValueAt(
                        modelRow,
                        0
                );

        if (idValue == null) {
            return;
        }

        int supplierId =
                Integer.parseInt(
                        idValue.toString()
                );

        SwingWorker<Optional<Supplier>, Void> worker =
                new SwingWorker<>() {

                    @Override
                    protected Optional<Supplier> doInBackground() {

                        return supplierService
                                .getSupplierById(
                                        supplierId
                                );
                    }

                    @Override
                    protected void done() {

                        try {

                            Optional<Supplier> result =
                                    get();

                            if (result.isEmpty()) {
                                return;
                            }

                            Supplier supplier =
                                    result.get();

                            selectedSupplierId =
                                    supplier.getSupplierId();

                            nameField.setText(
                                    safeValue(
                                            supplier.getSupplierName()
                                    )
                            );

                            phoneField.setText(
                                    safeValue(
                                            supplier.getPhone()
                                    )
                            );

                            emailField.setText(
                                    safeValue(
                                            supplier.getEmail()
                                    )
                            );

                            addressField.setText(
                                    safeValue(
                                            supplier.getAddress()
                                    )
                            );

                        } catch (Exception exception) {

                            showError(
                                    "Unable to load supplier details.",
                                    exception
                            );
                        }
                    }
                };

        worker.execute();
    }

    // =========================================================
    // SAVE
    // =========================================================

    private void saveSupplier() {

        try {

            Supplier supplier =
                    createSupplierFromForm();

            supplierService.createSupplier(
                    supplier
            );

            showSuccess(
                    "Supplier added successfully."
            );

            clearForm();

            loadSuppliers();

        } catch (ValidationException exception) {

            showWarning(
                    exception.getMessage()
            );

        } catch (Exception exception) {

            showError(
                    "Unable to save supplier.",
                    exception
            );
        }
    }

    // =========================================================
    // UPDATE
    // =========================================================

    private void updateSupplier() {

        if (selectedSupplierId <= 0) {

            showWarning(
                    "Select a supplier to update."
            );

            return;
        }

        try {

            Supplier supplier =
                    createSupplierFromForm();

            supplier.setSupplierId(
                    selectedSupplierId
            );

            boolean updated =
                    supplierService.updateSupplier(
                            supplier
                    );

            if (updated) {

                showSuccess(
                        "Supplier updated successfully."
                );

                clearForm();

                loadSuppliers();

            } else {

                showWarning(
                        "Supplier could not be updated."
                );
            }

        } catch (ValidationException exception) {

            showWarning(
                    exception.getMessage()
            );

        } catch (Exception exception) {

            showError(
                    "Unable to update supplier.",
                    exception
            );
        }
    }

    // =========================================================
    // DELETE
    // =========================================================

    private void deleteSupplier() {

        if (selectedSupplierId <= 0) {

            showWarning(
                    "Select a supplier to delete."
            );

            return;
        }

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        "Delete the selected supplier?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (
                result !=
                        JOptionPane.YES_OPTION
        ) {

            return;
        }

        try {

            boolean deleted =
                    supplierService.deleteSupplier(
                            selectedSupplierId
                    );

            if (deleted) {

                showSuccess(
                        "Supplier deleted successfully."
                );

                clearForm();

                loadSuppliers();

            } else {

                showWarning(
                        "Supplier could not be deleted."
                );
            }

        } catch (Exception exception) {

            showError(
                    "Unable to delete supplier.",
                    exception
            );
        }
    }

    // =========================================================
    // FORM DATA
    // =========================================================

    private Supplier createSupplierFromForm() {

        return new Supplier(
                nameField
                        .getText()
                        .trim(),

                emptyToNull(
                        phoneField.getText()
                ),

                emptyToNull(
                        emailField.getText()
                ),

                emptyToNull(
                        addressField.getText()
                )
        );
    }

    private void clearForm() {

        selectedSupplierId =
                -1;

        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        addressField.setText("");

        supplierTable.clearSelection();

        nameField.requestFocus();
    }

    // =========================================================
    // HELPERS
    // =========================================================

    private String emptyToNull(
            String value
    ) {

        if (
                value == null
                        ||
                        value.trim().isEmpty()
        ) {

            return null;
        }

        return value.trim();
    }

    private String safeValue(
            String value
    ) {

        return value == null
                ? ""
                : value;
    }

    private void configureTextField(
            JTextField field
    ) {

        field.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        field.setPreferredSize(
                new Dimension(
                        220,
                        38
                )
        );
    }

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
                        10
                )
        );

        label.setForeground(
                Theme.TEXT_SECONDARY
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

        button.setPreferredSize(
                new Dimension(
                        90,
                        38
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

    private JButton createDangerButton(
            String text
    ) {

        JButton button =
                new JButton(
                        text
                );

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
                        11
                )
        );

        button.setFocusPainted(
                false
        );

        button.setBorder(
                BorderFactory.createLineBorder(
                        new Color(
                                254,
                                202,
                                202
                        )
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
    // MESSAGES
    // =========================================================

    private void showSuccess(
            String message
    ) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "SmartStock AI",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showWarning(
            String message
    ) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "SmartStock AI",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void showError(
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
                "SmartStock AI",
                JOptionPane.ERROR_MESSAGE
        );
    }

    // =========================================================
    // TABLE RENDERER
    // =========================================================

    private static class SupplierCellRenderer
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

            if (column == 0) {

                label.setHorizontalAlignment(
                        SwingConstants.CENTER
                );

            } else {

                label.setHorizontalAlignment(
                        SwingConstants.LEFT
                );
            }

            return label;
        }
    }
}