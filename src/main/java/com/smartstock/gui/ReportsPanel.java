package com.smartstock.gui;

import com.smartstock.model.Product;
import com.smartstock.model.Sale;
import com.smartstock.service.ProductService;
import com.smartstock.service.ProductServiceImpl;
import com.smartstock.service.ReportService;
import com.smartstock.service.ReportServiceImpl;
import com.smartstock.service.SaleService;
import com.smartstock.service.SaleServiceImpl;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class ReportsPanel extends JPanel {

    private final ProductService productService;
    private final SaleService saleService;
    private final ReportService reportService;

    private JTextArea reportArea;
    private JLabel statusLabel;
    private JButton inventoryReportButton;
    private JButton salesReportButton;
    private JButton combinedReportButton;

    public ReportsPanel() {
        productService = new ProductServiceImpl();
        saleService = new SaleServiceImpl();
        reportService = new ReportServiceImpl();

        initializeUI();
        showWelcomeState();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND);

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BACKGROUND);
        header.setBorder(new EmptyBorder(28, 30, 18, 30));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("Reports");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Theme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel(
                "Generate clear inventory and sales reports from your live database."
        );
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(Theme.TEXT_SECONDARY);
        subtitle.setBorder(new EmptyBorder(6, 0, 0, 0));

        titlePanel.add(title);
        titlePanel.add(subtitle);

        header.add(titlePanel, BorderLayout.WEST);

        return header;
    }

    private JPanel createMainContent() {
        JPanel main = new JPanel(new BorderLayout(0, 16));
        main.setOpaque(false);
        main.setBorder(new EmptyBorder(0, 30, 20, 30));

        main.add(createReportToolbar(), BorderLayout.NORTH);
        main.add(createReportCard(), BorderLayout.CENTER);

        return main;
    }

    private JPanel createReportToolbar() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolbar.setBackground(Theme.SURFACE);
        toolbar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                new EmptyBorder(14, 16, 14, 16)
        ));

        inventoryReportButton = createPrimaryButton("Inventory Report");
        salesReportButton = createSecondaryButton("Sales Report");
        combinedReportButton = createSecondaryButton("Full Business Report");

        inventoryReportButton.addActionListener(e -> generateInventoryReport());
        salesReportButton.addActionListener(e -> generateSalesReport());
        combinedReportButton.addActionListener(e -> generateCombinedReport());

        toolbar.add(inventoryReportButton);
        toolbar.add(salesReportButton);
        toolbar.add(combinedReportButton);

        return toolbar;
    }

    private JPanel createReportCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Theme.SURFACE);
        card.setBorder(BorderFactory.createLineBorder(Theme.BORDER));

        JPanel cardHeader = new JPanel(new BorderLayout());
        cardHeader.setBackground(Theme.SURFACE);
        cardHeader.setBorder(new EmptyBorder(18, 20, 14, 20));

        JLabel title = new JLabel("Report Preview");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(Theme.TEXT_PRIMARY);

        JLabel hint = new JLabel("Generated from current SmartStock data");
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        hint.setForeground(Theme.TEXT_SECONDARY);

        JPanel labels = new JPanel();
        labels.setLayout(new BoxLayout(labels, BoxLayout.Y_AXIS));
        labels.setOpaque(false);

        labels.add(title);
        labels.add(Box.createVerticalStrut(4));
        labels.add(hint);

        cardHeader.add(labels, BorderLayout.WEST);

        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        reportArea.setForeground(Theme.TEXT_PRIMARY);
        reportArea.setBackground(Color.WHITE);
        reportArea.setLineWrap(false);
        reportArea.setWrapStyleWord(false);
        reportArea.setMargin(new Insets(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setBorder(BorderFactory.createMatteBorder(
                1, 0, 0, 0, Theme.BORDER
        ));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        card.add(cardHeader, BorderLayout.NORTH);
        card.add(scrollPane, BorderLayout.CENTER);

        return card;
    }

    private JPanel createStatusBar() {
        JPanel status = new JPanel(new BorderLayout());
        status.setBackground(Theme.BACKGROUND);
        status.setBorder(new EmptyBorder(0, 30, 12, 30));

        statusLabel = new JLabel("Ready.");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(Theme.TEXT_SECONDARY);

        status.add(statusLabel, BorderLayout.WEST);

        return status;
    }

    private void showWelcomeState() {
        reportArea.setText(
                "SMARTSTOCK AI — REPORT CENTER\n" +
                        "\n" +
                        "Generate a report using the controls above.\n" +
                        "\n" +
                        "Available reports\n" +
                        "────────────────────────────────────────\n" +
                        "• Inventory Report\n" +
                        "  Current products, stock levels and reorder information.\n" +
                        "\n" +
                        "• Sales Report\n" +
                        "  Recorded transactions and sales performance.\n" +
                        "\n" +
                        "• Full Business Report\n" +
                        "  Combined inventory and sales overview.\n"
        );

        statusLabel.setText("Ready to generate a report.");
    }

    private void generateInventoryReport() {
        setButtonsEnabled(false);
        statusLabel.setText("Generating inventory report...");

        SwingWorker<String, Void> worker = new SwingWorker<>() {

            @Override
            protected String doInBackground() {
                List<Product> products = productService.getAllProducts();
                return reportService.generateInventoryReport(products);
            }

            @Override
            protected void done() {
                try {
                    displayReport(get());
                    statusLabel.setText("Inventory report generated successfully.");
                } catch (Exception e) {
                    showError("Unable to generate inventory report.", e);
                } finally {
                    setButtonsEnabled(true);
                }
            }
        };

        worker.execute();
    }

    private void generateSalesReport() {
        setButtonsEnabled(false);
        statusLabel.setText("Generating sales report...");

        SwingWorker<String, Void> worker = new SwingWorker<>() {

            @Override
            protected String doInBackground() {
                List<Sale> sales = saleService.getAllSales();
                return reportService.generateSalesReport(sales);
            }

            @Override
            protected void done() {
                try {
                    displayReport(get());
                    statusLabel.setText("Sales report generated successfully.");
                } catch (Exception e) {
                    showError("Unable to generate sales report.", e);
                } finally {
                    setButtonsEnabled(true);
                }
            }
        };

        worker.execute();
    }

    private void generateCombinedReport() {
        setButtonsEnabled(false);
        statusLabel.setText("Generating full business report...");

        SwingWorker<String, Void> worker = new SwingWorker<>() {

            @Override
            protected String doInBackground() {
                List<Product> products = productService.getAllProducts();
                List<Sale> sales = saleService.getAllSales();

                String inventoryReport =
                        reportService.generateInventoryReport(products);

                String salesReport =
                        reportService.generateSalesReport(sales);

                return buildCombinedReport(
                        inventoryReport,
                        salesReport
                );
            }

            @Override
            protected void done() {
                try {
                    displayReport(get());
                    statusLabel.setText("Full business report generated successfully.");
                } catch (Exception e) {
                    showError("Unable to generate full business report.", e);
                } finally {
                    setButtonsEnabled(true);
                }
            }
        };

        worker.execute();
    }

    private String buildCombinedReport(
            String inventoryReport,
            String salesReport
    ) {
        return "SMARTSTOCK AI — FULL BUSINESS REPORT\n\n" +
                "════════════════════════════════════════════════════════════\n\n" +
                "INVENTORY REPORT\n\n" +
                inventoryReport +
                "\n\n" +
                "════════════════════════════════════════════════════════════\n\n" +
                "SALES REPORT\n\n" +
                salesReport +
                "\n\n" +
                "════════════════════════════════════════════════════════════\n" +
                "Generated by SmartStock AI\n";
    }

    private void displayReport(String report) {
        if (report == null || report.trim().isEmpty()) {
            reportArea.setText(
                    "No report data is currently available."
            );
            return;
        }

        reportArea.setText(report);
        reportArea.setCaretPosition(0);
    }

    private void setButtonsEnabled(boolean enabled) {
        inventoryReportButton.setEnabled(enabled);
        salesReportButton.setEnabled(enabled);
        combinedReportButton.setEnabled(enabled);
    }

    private void showError(String message, Exception exception) {
        statusLabel.setText("Report generation failed.");

        JOptionPane.showMessageDialog(
                this,
                message + "\n\n" + getRootMessage(exception),
                "Report Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private String getRootMessage(Exception exception) {
        Throwable cause = exception;

        while (cause.getCause() != null) {
            cause = cause.getCause();
        }

        return cause.getMessage() == null
                ? cause.toString()
                : cause.getMessage();
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);

        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(Theme.PRIMARY);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(155, 40));

        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);

        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Theme.TEXT_PRIMARY);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(155, 40));

        return button;
    }
}