package com.smartstock.gui;

import com.smartstock.util.DatabaseConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;

public class SettingsPanel extends JPanel {

    private JCheckBox lowStockAlertsCheckBox;
    private JCheckBox automaticRefreshCheckBox;

    private JLabel connectionStatusLabel;

    private JButton resetButton;
    private JButton saveButton;
    private JButton testConnectionButton;

    private boolean savedLowStockAlerts = true;
    private boolean savedAutomaticRefresh = true;

    public SettingsPanel() {

        setLayout(new BorderLayout());

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
    }

    // =========================================================
    // MAIN INTERFACE
    // =========================================================

    private void createInterface() {

        JPanel root =
                new JPanel(
                        new BorderLayout(
                                0,
                                20
                        )
                );

        root.setOpaque(false);

        root.add(
                createHeader(),
                BorderLayout.NORTH
        );

        JPanel content =
                new JPanel();

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );

        content.setOpaque(false);

        content.add(
                createApplicationCard()
        );

        content.add(
                Box.createVerticalStrut(16)
        );

        content.add(
                createInventoryConfigurationCard()
        );

        content.add(
                Box.createVerticalStrut(16)
        );

        content.add(
                createDatabaseCard()
        );

        content.add(
                Box.createVerticalGlue()
        );

        JScrollPane scrollPane =
                new JScrollPane(
                        content
                );

        scrollPane.setBorder(null);

        scrollPane.setOpaque(false);

        scrollPane.getViewport()
                .setOpaque(false);

        scrollPane.getVerticalScrollBar()
                .setUnitIncrement(16);

        root.add(
                scrollPane,
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
                        "Settings"
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
                        "Configure your SmartStock workspace."
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
    // APPLICATION
    // =========================================================

    private JPanel createApplicationCard() {

        JPanel card =
                createCard();

        card.setLayout(
                new BorderLayout(
                        0,
                        16
                )
        );

        card.add(
                createSectionHeader(
                        "Application",
                        "SmartStock AI application information."
                ),
                BorderLayout.NORTH
        );

        JPanel details =
                new JPanel(
                        new GridLayout(
                                4,
                                2,
                                30,
                                12
                        )
                );

        details.setOpaque(false);

        addInfoRow(
                details,
                "Product",
                "SmartStock AI"
        );

        addInfoRow(
                details,
                "Version",
                "1.0.0"
        );

        addInfoRow(
                details,
                "Platform",
                "Java Desktop Application"
        );

        addInfoRow(
                details,
                "Database",
                "MySQL + JDBC"
        );

        card.add(
                details,
                BorderLayout.CENTER
        );

        return card;
    }

    // =========================================================
    // INVENTORY CONFIGURATION
    // =========================================================

    private JPanel createInventoryConfigurationCard() {

        JPanel card =
                createCard();

        card.setLayout(
                new BorderLayout(
                        0,
                        16
                )
        );

        card.add(
                createSectionHeader(
                        "Inventory Configuration",
                        "Control how SmartStock handles inventory monitoring."
                ),
                BorderLayout.NORTH
        );

        JPanel options =
                new JPanel();

        options.setLayout(
                new BoxLayout(
                        options,
                        BoxLayout.Y_AXIS
                )
        );

        options.setOpaque(false);

        lowStockAlertsCheckBox =
                createCheckBox(
                        "Low-stock alerts",
                        "Show warnings when products reach or fall below their reorder level.",
                        true
                );

        automaticRefreshCheckBox =
                createCheckBox(
                        "Automatic data refresh",
                        "Allow inventory screens to refresh their data automatically.",
                        true
                );

        options.add(
                lowStockAlertsCheckBox
        );

        options.add(
                Box.createVerticalStrut(14)
        );

        options.add(
                automaticRefreshCheckBox
        );

        card.add(
                options,
                BorderLayout.CENTER
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

        resetButton =
                createSecondaryButton(
                        "Reset"
                );

        resetButton.setPreferredSize(
                new Dimension(
                        100,
                        40
                )
        );

        resetButton.addActionListener(
                event ->
                        resetSettings()
        );

        saveButton =
                createPrimaryButton(
                        "Save Settings"
                );

        saveButton.setPreferredSize(
                new Dimension(
                        125,
                        40
                )
        );

        saveButton.addActionListener(
                event ->
                        saveSettings()
        );

        buttons.add(
                resetButton
        );

        buttons.add(
                saveButton
        );

        card.add(
                buttons,
                BorderLayout.SOUTH
        );

        return card;
    }

    // =========================================================
    // DATABASE
    // =========================================================

    private JPanel createDatabaseCard() {

        JPanel card =
                createCard();

        card.setLayout(
                new BorderLayout(
                        0,
                        16
                )
        );

        card.add(
                createSectionHeader(
                        "Database Connection",
                        "Current SmartStock database connection details."
                ),
                BorderLayout.NORTH
        );

        JPanel details =
                new JPanel(
                        new GridLayout(
                                4,
                                2,
                                30,
                                12
                        )
                );

        details.setOpaque(false);

        JLabel statusTitle =
                createInfoTitle(
                        "Status"
                );

        connectionStatusLabel =
                new JLabel(
                        "Checking..."
                );

        connectionStatusLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        details.add(
                statusTitle
        );

        details.add(
                connectionStatusLabel
        );

        addInfoRow(
                details,
                "Database",
                "smartstock"
        );

        addInfoRow(
                details,
                "Host",
                "localhost:3306"
        );

        addInfoRow(
                details,
                "Driver",
                "MySQL Connector/J"
        );

        card.add(
                details,
                BorderLayout.CENTER
        );

        JPanel bottom =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                0,
                                0
                        )
                );

        bottom.setOpaque(false);

        testConnectionButton =
                createSecondaryButton(
                        "Test Connection"
                );

        testConnectionButton.setPreferredSize(
                new Dimension(
                        145,
                        40
                )
        );

        testConnectionButton.addActionListener(
                event ->
                        testDatabaseConnection()
        );

        bottom.add(
                testConnectionButton
        );

        card.add(
                bottom,
                BorderLayout.SOUTH
        );

        SwingUtilities.invokeLater(
                this::testDatabaseConnection
        );

        return card;
    }

    // =========================================================
    // CARD HELPERS
    // =========================================================

    private JPanel createCard() {

        JPanel card =
                new JPanel();

        card.setBackground(
                Theme.SURFACE
        );

        card.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                Theme.BORDER
                        ),
                        new EmptyBorder(
                                20,
                                22,
                                20,
                                22
                        )
                )
        );

        return card;
    }

    private JPanel createSectionHeader(
            String title,
            String subtitle
    ) {

        JPanel panel =
                new JPanel();

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.setOpaque(false);

        JLabel titleLabel =
                new JLabel(
                        title
                );

        titleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        18
                )
        );

        titleLabel.setForeground(
                Theme.TEXT_PRIMARY
        );

        JLabel subtitleLabel =
                new JLabel(
                        subtitle
                );

        subtitleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        subtitleLabel.setForeground(
                Theme.TEXT_SECONDARY
        );

        panel.add(
                titleLabel
        );

        panel.add(
                Box.createVerticalStrut(4)
        );

        panel.add(
                subtitleLabel
        );

        return panel;
    }

    private void addInfoRow(
            JPanel panel,
            String title,
            String value
    ) {

        panel.add(
                createInfoTitle(
                        title
                )
        );

        JLabel valueLabel =
                new JLabel(
                        value
                );

        valueLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        valueLabel.setForeground(
                Theme.TEXT_PRIMARY
        );

        panel.add(
                valueLabel
        );
    }

    private JLabel createInfoTitle(
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

    private JCheckBox createCheckBox(
            String title,
            String description,
            boolean selected
    ) {

        JCheckBox checkBox =
                new JCheckBox(
                        title
                );

        checkBox.setSelected(
                selected
        );

        checkBox.setOpaque(false);

        checkBox.setFocusPainted(
                false
        );

        checkBox.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        checkBox.setForeground(
                Theme.TEXT_PRIMARY
        );

        checkBox.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        checkBox.setToolTipText(
                description
        );

        return checkBox;
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
    // SAVE
    // =========================================================

    private void saveSettings() {

        savedLowStockAlerts =
                lowStockAlertsCheckBox
                        .isSelected();

        savedAutomaticRefresh =
                automaticRefreshCheckBox
                        .isSelected();

        JOptionPane.showMessageDialog(
                this,
                "Settings saved successfully.",
                "Settings",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =========================================================
    // RESET
    // =========================================================

    private void resetSettings() {

        lowStockAlertsCheckBox.setSelected(
                savedLowStockAlerts
        );

        automaticRefreshCheckBox.setSelected(
                savedAutomaticRefresh
        );
    }

    // =========================================================
    // DATABASE TEST
    // =========================================================

    private void testDatabaseConnection() {

        testConnectionButton.setEnabled(
                false
        );

        connectionStatusLabel.setText(
                "Checking..."
        );

        connectionStatusLabel.setForeground(
                Theme.WARNING
        );

        SwingWorker<Boolean, Void> worker =
                new SwingWorker<>() {

                    @Override
                    protected Boolean doInBackground()
                            throws Exception {

                        try (
                                Connection connection =
                                        DatabaseConnection
                                                .getConnection()
                        ) {

                            return connection != null
                                    &&
                                    !connection.isClosed();
                        }
                    }

                    @Override
                    protected void done() {

                        try {

                            boolean connected =
                                    get();

                            if (
                                    connected
                            ) {

                                connectionStatusLabel
                                        .setText(
                                                "Connected"
                                        );

                                connectionStatusLabel
                                        .setForeground(
                                                Theme.SUCCESS
                                        );

                            } else {

                                connectionStatusLabel
                                        .setText(
                                                "Disconnected"
                                        );

                                connectionStatusLabel
                                        .setForeground(
                                                Theme.DANGER
                                        );
                            }

                        } catch (Exception exception) {

                            connectionStatusLabel
                                    .setText(
                                            "Disconnected"
                                    );

                            connectionStatusLabel
                                    .setForeground(
                                            Theme.DANGER
                                    );

                        } finally {

                            testConnectionButton
                                    .setEnabled(
                                            true
                                    );
                        }
                    }
                };

        worker.execute();
    }
}