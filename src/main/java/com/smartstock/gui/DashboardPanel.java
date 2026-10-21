package com.smartstock.gui;

import com.smartstock.model.Product;
import com.smartstock.service.ProductService;
import com.smartstock.service.ProductServiceImpl;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * SmartStock AI executive dashboard.
 *
 * Focuses on the information an inventory manager needs immediately:
 * inventory size, stock value, health and actionable stock alerts.
 */
public class DashboardPanel extends JPanel {

    private final ProductService productService;

    private final JLabel totalProductsValue =
            createMetricValue();

    private final JLabel lowStockValue =
            createMetricValue();

    private final JLabel outOfStockValue =
            createMetricValue();

    private final JLabel inventoryValue =
            createMetricValue();

    private final JLabel healthValue =
            new JLabel("--");

    private final JLabel healthDescription =
            new JLabel("Loading inventory health...");

    private final JProgressBar healthBar =
            new JProgressBar(0, 100);

    private final JPanel alertsPanel =
            new JPanel();

    private final JLabel statusLabel =
            new JLabel("Loading inventory...");

    private final JButton refreshButton =
            new JButton("Refresh");

    public DashboardPanel() {

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

        JPanel content =
                new JPanel();

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );

        content.setBackground(
                Theme.BACKGROUND
        );

        content.add(
                createHeader()
        );

        content.add(
                Box.createVerticalStrut(20)
        );

        content.add(
                createMetrics()
        );

        content.add(
                Box.createVerticalStrut(14)
        );

        content.add(
                createOverviewRow()
        );

        content.add(
                Box.createVerticalStrut(14)
        );

        content.add(
                createAlertsCard()
        );

        JScrollPane scrollPane =
                new JScrollPane(
                        content
                );

        scrollPane.setBorder(
                BorderFactory.createEmptyBorder()
        );

        scrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );

        scrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scrollPane.getVerticalScrollBar()
                .setUnitIncrement(16);

        scrollPane.getViewport()
                .setBackground(
                        Theme.BACKGROUND
                );

        add(
                scrollPane,
                BorderLayout.CENTER
        );

        statusLabel.setFont(
                Theme.SMALL_FONT
        );

        statusLabel.setForeground(
                Theme.TEXT_MUTED
        );

        statusLabel.setBorder(
                new EmptyBorder(
                        8,
                        2,
                        0,
                        2
                )
        );

        add(
                statusLabel,
                BorderLayout.SOUTH
        );

        refreshButton.addActionListener(
                event ->
                        loadDashboardData()
        );

        loadDashboardData();
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

        header.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        62
                )
        );

        JPanel titleBlock =
                new JPanel();

        titleBlock.setLayout(
                new BoxLayout(
                        titleBlock,
                        BoxLayout.Y_AXIS
                )
        );

        titleBlock.setOpaque(false);

        JLabel title =
                new JLabel(
                        "Inventory Overview"
                );

        title.setFont(
                Theme.PAGE_TITLE_FONT
        );

        title.setForeground(
                Theme.TEXT_PRIMARY
        );

        JLabel subtitle =
                new JLabel(
                        "A real-time view of your inventory position and stock risks."
                );

        subtitle.setFont(
                Theme.PAGE_SUBTITLE_FONT
        );

        subtitle.setForeground(
                Theme.TEXT_SECONDARY
        );

        titleBlock.add(
                title
        );

        titleBlock.add(
                Box.createVerticalStrut(5)
        );

        titleBlock.add(
                subtitle
        );

        stylePrimaryButton(
                refreshButton
        );

        refreshButton.setPreferredSize(
                new Dimension(
                        96,
                        38
                )
        );

        header.add(
                titleBlock,
                BorderLayout.WEST
        );

        header.add(
                refreshButton,
                BorderLayout.EAST
        );

        return header;
    }

    // =========================================================
    // KPI METRICS
    // =========================================================

    private JPanel createMetrics() {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                12,
                                0
                        )
                );

        panel.setOpaque(false);

        panel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        108
                )
        );

        panel.add(
                createMetricCard(
                        "TOTAL PRODUCTS",
                        totalProductsValue,
                        "Products being tracked",
                        Theme.PRIMARY
                )
        );

        panel.add(
                createMetricCard(
                        "LOW STOCK",
                        lowStockValue,
                        "At or below reorder level",
                        Theme.WARNING
                )
        );

        panel.add(
                createMetricCard(
                        "OUT OF STOCK",
                        outOfStockValue,
                        "Currently unavailable",
                        Theme.DANGER
                )
        );

        panel.add(
                createMetricCard(
                        "STOCK VALUE",
                        inventoryValue,
                        "Current inventory valuation",
                        Theme.SUCCESS
                )
        );

        return panel;
    }

    private JPanel createMetricCard(
            String title,
            JLabel value,
            String description,
            Color accent
    ) {

        JPanel card =
                createCardPanel();

        card.setLayout(
                new BorderLayout(
                        0,
                        8
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
                Theme.SMALL_BOLD_FONT
        );

        titleLabel.setForeground(
                Theme.TEXT_SECONDARY
        );

        JPanel marker =
                new JPanel();

        marker.setPreferredSize(
                new Dimension(
                        5,
                        22
                )
        );

        marker.setBackground(
                accent
        );

        top.add(
                titleLabel,
                BorderLayout.WEST
        );

        top.add(
                marker,
                BorderLayout.EAST
        );

        JLabel descriptionLabel =
                new JLabel(
                        description
                );

        descriptionLabel.setFont(
                Theme.SMALL_FONT
        );

        descriptionLabel.setForeground(
                Theme.TEXT_MUTED
        );

        value.setForeground(
                Theme.TEXT_PRIMARY
        );

        value.setFont(
                Theme.STAT_VALUE_FONT
        );

        card.add(
                top,
                BorderLayout.NORTH
        );

        card.add(
                value,
                BorderLayout.CENTER
        );

        card.add(
                descriptionLabel,
                BorderLayout.SOUTH
        );

        return card;
    }

    // =========================================================
    // OVERVIEW
    // =========================================================

    private JPanel createOverviewRow() {

        JPanel row =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                14,
                                0
                        )
                );

        row.setOpaque(false);

        row.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        164
                )
        );

        row.add(
                createInventoryHealthCard()
        );

        row.add(
                createSmartAlertCard()
        );

        return row;
    }

    // =========================================================
    // INVENTORY HEALTH
    // =========================================================

    private JPanel createInventoryHealthCard() {

        JPanel card =
                createCardPanel();

        card.setLayout(
                new BorderLayout(
                        0,
                        12
                )
        );

        JPanel heading =
                new JPanel(
                        new BorderLayout()
                );

        heading.setOpaque(false);

        JLabel title =
                new JLabel(
                        "Inventory Health"
                );

        title.setFont(
                Theme.SECTION_TITLE_FONT
        );

        title.setForeground(
                Theme.TEXT_PRIMARY
        );

        JLabel live =
                createStatusBadge(
                        "LIVE"
                );

        heading.add(
                title,
                BorderLayout.WEST
        );

        heading.add(
                live,
                BorderLayout.EAST
        );

        JPanel center =
                new JPanel(
                        new BorderLayout(
                                16,
                                0
                        )
                );

        center.setOpaque(false);

        healthValue.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        25
                )
        );

        healthValue.setForeground(
                Theme.TEXT_PRIMARY
        );

        healthBar.setValue(
                0
        );

        healthBar.setStringPainted(
                false
        );

        healthBar.setPreferredSize(
                new Dimension(
                        0,
                        9
                )
        );

        healthBar.setBorderPainted(
                false
        );

        healthBar.setForeground(
                Theme.SUCCESS
        );

        healthBar.setBackground(
                Theme.BORDER_LIGHT
        );

        healthDescription.setFont(
                Theme.SMALL_FONT
        );

        healthDescription.setForeground(
                Theme.TEXT_SECONDARY
        );

        JPanel text =
                new JPanel();

        text.setLayout(
                new BoxLayout(
                        text,
                        BoxLayout.Y_AXIS
                )
        );

        text.setOpaque(false);

        text.add(
                healthValue
        );

        text.add(
                Box.createVerticalStrut(2)
        );

        text.add(
                healthDescription
        );

        center.add(
                text,
                BorderLayout.WEST
        );

        center.add(
                healthBar,
                BorderLayout.CENTER
        );

        card.add(
                heading,
                BorderLayout.NORTH
        );

        card.add(
                center,
                BorderLayout.CENTER
        );

        return card;
    }

    // =========================================================
    // SMART ALERT
    // =========================================================

    private JPanel createSmartAlertCard() {

        JPanel card =
                createCardPanel();

        card.setBackground(
                Theme.PRIMARY_LIGHT
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                Theme.PRIMARY_SOFT
                        ),
                        new EmptyBorder(
                                20,
                                20,
                                20,
                                20
                        )
                )
        );

        JPanel icon =
                new JPanel(
                        new GridBagLayout()
                );

        icon.setPreferredSize(
                new Dimension(
                        48,
                        48
                )
        );

        icon.setBackground(
                Theme.PRIMARY
        );

        JLabel ai =
                new JLabel(
                        "AI",
                        SwingConstants.CENTER
                );

        ai.setForeground(
                Color.WHITE
        );

        ai.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        icon.add(
                ai
        );

        JPanel text =
                new JPanel();

        text.setLayout(
                new BoxLayout(
                        text,
                        BoxLayout.Y_AXIS
                )
        );

        text.setOpaque(false);

        JLabel eyebrow =
                new JLabel(
                        "SMART ALERT"
                );

        eyebrow.setFont(
                Theme.SMALL_BOLD_FONT
        );

        eyebrow.setForeground(
                Theme.PRIMARY_DARK
        );

        JLabel headline =
                new JLabel(
                        "Inventory intelligence at a glance."
                );

        headline.setFont(
                Theme.BODY_BOLD_FONT
        );

        headline.setForeground(
                Theme.TEXT_PRIMARY
        );

        JLabel description =
                new JLabel(
                        "Review the alerts below before replenishing stock."
                );

        description.setFont(
                Theme.SMALL_FONT
        );

        description.setForeground(
                Theme.TEXT_SECONDARY
        );

        text.add(
                eyebrow
        );

        text.add(
                Box.createVerticalStrut(5)
        );

        text.add(
                headline
        );

        text.add(
                Box.createVerticalStrut(4)
        );

        text.add(
                description
        );

        JPanel wrapper =
                new JPanel(
                        new BorderLayout(
                                14,
                                0
                        )
                );

        wrapper.setOpaque(false);

        wrapper.add(
                icon,
                BorderLayout.WEST
        );

        wrapper.add(
                text,
                BorderLayout.CENTER
        );

        card.setLayout(
                new BorderLayout()
        );

        card.add(
                wrapper,
                BorderLayout.CENTER
        );

        return card;
    }

    // =========================================================
    // STOCK ALERTS
    // =========================================================

    private JPanel createAlertsCard() {

        JPanel card =
                createCardPanel();

        card.setLayout(
                new BorderLayout(
                        0,
                        12
                )
        );

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setOpaque(false);

        JPanel titleBlock =
                new JPanel();

        titleBlock.setLayout(
                new BoxLayout(
                        titleBlock,
                        BoxLayout.Y_AXIS
                )
        );

        titleBlock.setOpaque(false);

        JLabel title =
                new JLabel(
                        "Stock Alerts"
                );

        title.setFont(
                Theme.SECTION_TITLE_FONT
        );

        title.setForeground(
                Theme.TEXT_PRIMARY
        );

        JLabel subtitle =
                new JLabel(
                        "Products requiring attention right now."
                );

        subtitle.setFont(
                Theme.SMALL_FONT
        );

        subtitle.setForeground(
                Theme.TEXT_MUTED
        );

        titleBlock.add(
                title
        );

        titleBlock.add(
                Box.createVerticalStrut(3)
        );

        titleBlock.add(
                subtitle
        );

        JLabel live =
                new JLabel(
                        "LIVE"
                );

        live.setFont(
                Theme.SMALL_BOLD_FONT
        );

        live.setForeground(
                Theme.WARNING
        );

        header.add(
                titleBlock,
                BorderLayout.WEST
        );

        header.add(
                live,
                BorderLayout.EAST
        );

        alertsPanel.setLayout(
                new BoxLayout(
                        alertsPanel,
                        BoxLayout.Y_AXIS
                )
        );

        alertsPanel.setBackground(
                Theme.SURFACE
        );

        JScrollPane alertsScroll =
                new JScrollPane(
                        alertsPanel
                );

        alertsScroll.setBorder(
                BorderFactory.createEmptyBorder()
        );

        alertsScroll.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );

        alertsScroll.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        alertsScroll.setPreferredSize(
                new Dimension(
                        0,
                        220
                )
        );

        alertsScroll.getViewport()
                .setBackground(
                        Theme.SURFACE
                );

        card.add(
                header,
                BorderLayout.NORTH
        );

        card.add(
                alertsScroll,
                BorderLayout.CENTER
        );

        return card;
    }

    private JPanel createAlertRow(
            Product product
    ) {

        boolean outOfStock =
                product.isOutOfStock();

        Color accent =
                outOfStock
                        ? Theme.DANGER
                        : Theme.WARNING;

        Color soft =
                outOfStock
                        ? Theme.DANGER_LIGHT
                        : Theme.WARNING_LIGHT;

        JPanel row =
                new JPanel(
                        new BorderLayout(
                                14,
                                0
                        )
                );

        row.setBackground(
                soft
        );

        row.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                outOfStock
                                        ? new Color(
                                        254,
                                        202,
                                        202
                                )
                                        : new Color(
                                        253,
                                        230,
                                        138
                                )
                        ),
                        new EmptyBorder(
                                12,
                                14,
                                12,
                                14
                        )
                )
        );

        row.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        72
                )
        );

        JPanel indicator =
                new JPanel();

        indicator.setPreferredSize(
                new Dimension(
                        5,
                        40
                )
        );

        indicator.setBackground(
                accent
        );

        JPanel info =
                new JPanel();

        info.setLayout(
                new BoxLayout(
                        info,
                        BoxLayout.Y_AXIS
                )
        );

        info.setOpaque(false);

        JLabel name =
                new JLabel(
                        product.getProductName()
                );

        name.setFont(
                Theme.BODY_BOLD_FONT
        );

        name.setForeground(
                Theme.TEXT_PRIMARY
        );

        JLabel category =
                new JLabel(
                        product.getCategory()
                );

        category.setFont(
                Theme.SMALL_FONT
        );

        category.setForeground(
                Theme.TEXT_MUTED
        );

        info.add(
                name
        );

        info.add(
                Box.createVerticalStrut(3)
        );

        info.add(
                category
        );

        JPanel stock =
                new JPanel();

        stock.setLayout(
                new BoxLayout(
                        stock,
                        BoxLayout.Y_AXIS
                )
        );

        stock.setOpaque(false);

        JLabel quantity =
                new JLabel(
                        product.getQuantity()
                                + " / "
                                + product.getReorderLevel()
                );

        quantity.setFont(
                Theme.BODY_BOLD_FONT
        );

        quantity.setForeground(
                Theme.TEXT_PRIMARY
        );

        quantity.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        JLabel caption =
                new JLabel(
                        "current / reorder"
                );

        caption.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        10
                )
        );

        caption.setForeground(
                Theme.TEXT_MUTED
        );

        caption.setAlignmentX(
                Component.RIGHT_ALIGNMENT
        );

        stock.add(
                quantity
        );

        stock.add(
                Box.createVerticalStrut(2)
        );

        stock.add(
                caption
        );

        JLabel badge =
                createStatusBadge(
                        outOfStock
                                ? "OUT OF STOCK"
                                : "LOW STOCK"
                );

        badge.setForeground(
                accent
        );

        badge.setBackground(
                Color.WHITE
        );

        JPanel right =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                12,
                                0
                        )
                );

        right.setOpaque(false);

        right.add(
                stock
        );

        right.add(
                badge
        );

        row.add(
                indicator,
                BorderLayout.WEST
        );

        row.add(
                info,
                BorderLayout.CENTER
        );

        row.add(
                right,
                BorderLayout.EAST
        );

        return row;
    }

    // =========================================================
    // DATA
    // =========================================================

    private void loadDashboardData() {

        refreshButton.setEnabled(
                false
        );

        statusLabel.setText(
                "Refreshing inventory..."
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

                            List<Product> products =
                                    get();

                            updateDashboard(
                                    products
                            );

                            statusLabel.setText(
                                    "Inventory updated successfully."
                            );

                        } catch (Exception exception) {

                            statusLabel.setText(
                                    "Unable to load inventory data."
                            );

                            showLoadError();
                        }
                    }
                };

        worker.execute();
    }

    private void updateDashboard(
            List<Product> products
    ) {

        if (products == null) {

            products =
                    new ArrayList<>();
        }

        int total =
                products.size();

        int low =
                0;

        int out =
                0;

        BigDecimal value =
                BigDecimal.ZERO;

        List<Product> alerts =
                new ArrayList<>();

        for (Product product : products) {

            if (product == null) {
                continue;
            }

            if (product.isOutOfStock()) {

                out++;

                alerts.add(
                        product
                );

            } else if (product.isLowStock()) {

                low++;

                alerts.add(
                        product
                );
            }

            if (product.getPrice() != null) {

                value =
                        value.add(
                                product.getPrice()
                                        .multiply(
                                                BigDecimal.valueOf(
                                                        product.getQuantity()
                                                )
                                        )
                        );
            }
        }

        totalProductsValue.setText(
                String.valueOf(
                        total
                )
        );

        lowStockValue.setText(
                String.valueOf(
                        low
                )
        );

        outOfStockValue.setText(
                String.valueOf(
                        out
                )
        );

        inventoryValue.setText(
                formatCurrency(
                        value
                )
        );

        updateHealth(
                total,
                low,
                out
        );

        updateAlerts(
                alerts
        );
    }

    // =========================================================
    // HEALTH
    // =========================================================

    private void updateHealth(
            int total,
            int low,
            int out
    ) {

        if (total == 0) {

            healthValue.setText(
                    "No data"
            );

            healthDescription.setText(
                    "Add products to measure inventory health."
            );

            healthBar.setValue(
                    0
            );

            healthBar.setForeground(
                    Theme.BORDER_DARK
            );

            return;
        }

        int healthy =
                Math.max(
                        0,
                        total - low - out
                );

        int percentage =
                (int) Math.round(
                        healthy * 100.0 / total
                );

        healthValue.setText(
                percentage
                        + "% healthy"
        );

        healthDescription.setText(
                healthy
                        + " of "
                        + total
                        + " products are above reorder level."
        );

        healthBar.setValue(
                percentage
        );

        if (out > 0) {

            healthBar.setForeground(
                    Theme.DANGER
            );

        } else if (low > 0) {

            healthBar.setForeground(
                    Theme.WARNING
            );

        } else {

            healthBar.setForeground(
                    Theme.SUCCESS
            );
        }
    }

    // =========================================================
    // ALERTS
    // =========================================================

    private void updateAlerts(
            List<Product> alerts
    ) {

        alertsPanel.removeAll();

        alerts.sort(
                Comparator
                        .comparingInt(
                                Product::getQuantity
                        )
                        .thenComparing(
                                Product::getProductName,
                                String.CASE_INSENSITIVE_ORDER
                        )
        );

        if (alerts.isEmpty()) {

            JPanel empty =
                    new JPanel(
                            new GridBagLayout()
                    );

            empty.setBackground(
                    Theme.SUCCESS_LIGHT
            );

            empty.setBorder(
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(
                                    new Color(
                                            187,
                                            247,
                                            208
                                    )
                            ),
                            new EmptyBorder(
                                    20,
                                    20,
                                    20,
                                    20
                            )
                    )
            );

            empty.setMaximumSize(
                    new Dimension(
                            Integer.MAX_VALUE,
                            82
                    )
            );

            JLabel message =
                    new JLabel(
                            "All products are currently above their reorder levels."
                    );

            message.setFont(
                    Theme.BODY_BOLD_FONT
            );

            message.setForeground(
                    Theme.SUCCESS
            );

            empty.add(
                    message
            );

            alertsPanel.add(
                    empty
            );

        } else {

            for (
                    int index = 0;
                    index < alerts.size();
                    index++
            ) {

                alertsPanel.add(
                        createAlertRow(
                                alerts.get(index)
                        )
                );

                if (
                        index
                                <
                                alerts.size() - 1
                ) {

                    alertsPanel.add(
                            Box.createVerticalStrut(
                                    8
                            )
                    );
                }
            }
        }

        alertsPanel.revalidate();

        alertsPanel.repaint();
    }

    private void showLoadError() {

        alertsPanel.removeAll();

        JLabel error =
                new JLabel(
                        "Could not load inventory data. Check the database connection."
                );

        error.setFont(
                Theme.BODY_FONT
        );

        error.setForeground(
                Theme.DANGER
        );

        error.setBorder(
                new EmptyBorder(
                        12,
                        4,
                        12,
                        4
                )
        );

        alertsPanel.add(
                error
        );

        alertsPanel.revalidate();

        alertsPanel.repaint();
    }

    // =========================================================
    // COMPONENT HELPERS
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
                                18,
                                18,
                                18
                        )
                )
        );

        return panel;
    }

    private static JLabel createMetricValue() {

        JLabel label =
                new JLabel(
                        "--"
                );

        label.setFont(
                Theme.STAT_VALUE_FONT
        );

        label.setForeground(
                Theme.TEXT_PRIMARY
        );

        return label;
    }

    private JLabel createStatusBadge(
            String text
    ) {

        JLabel label =
                new JLabel(
                        text,
                        SwingConstants.CENTER
                );

        label.setFont(
                Theme.SMALL_BOLD_FONT
        );

        label.setForeground(
                Theme.TEXT_SECONDARY
        );

        label.setBackground(
                Theme.SURFACE_ALT
        );

        label.setOpaque(
                true
        );

        label.setBorder(
                new EmptyBorder(
                        5,
                        8,
                        5,
                        8
                )
        );

        return label;
    }

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
                Theme.BODY_BOLD_FONT
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

    private String formatCurrency(
            BigDecimal amount
    ) {

        if (amount == null) {

            return "₹0.00";
        }

        return "₹"
                + amount.setScale(
                2,
                RoundingMode.HALF_UP
        );
    }
}