package com.smartstock.ai;

import com.smartstock.gui.Theme;
import com.smartstock.model.Product;
import com.smartstock.model.Sale;
import com.smartstock.service.ProductService;
import com.smartstock.service.ProductServiceImpl;
import com.smartstock.service.SaleService;
import com.smartstock.service.SaleServiceImpl;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class AIInsightsPanel extends JPanel {

    private final ProductService productService;
    private final SaleService saleService;
    private final InventoryAIService aiService;

    private JButton analyzeButton;

    private JTextPane insightsPane;

    private JLabel statusLabel;
    private JLabel productCountLabel;
    private JLabel salesCountLabel;
    private JLabel lowStockLabel;

    private static final Color AI_PURPLE =
            new Color(79, 70, 229);

    private static final Color TEXT =
            new Color(15, 23, 42);

    private static final Color MUTED =
            new Color(100, 116, 139);

    private static final Color BORDER =
            new Color(226, 232, 240);

    private static final Color SUCCESS =
            new Color(22, 163, 74);

    private static final Color WARNING =
            new Color(217, 119, 6);

    public AIInsightsPanel() {

        productService =
                new ProductServiceImpl();

        saleService =
                new SaleServiceImpl();

        aiService =
                new InventoryAIService();

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

        loadOverview();
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
                createHeader(),
                BorderLayout.NORTH
        );

        JPanel body =
                new JPanel(
                        new BorderLayout(
                                0,
                                16
                        )
                );

        body.setOpaque(false);

        body.add(
                createOverviewCards(),
                BorderLayout.NORTH
        );

        body.add(
                createInsightsCard(),
                BorderLayout.CENTER
        );

        root.add(
                body,
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

        JPanel titleArea =
                new JPanel();

        titleArea.setLayout(
                new BoxLayout(
                        titleArea,
                        BoxLayout.Y_AXIS
                )
        );

        titleArea.setOpaque(false);

        JLabel title =
                new JLabel(
                        "AI Insights"
                );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        28
                )
        );

        title.setForeground(
                TEXT
        );

        JLabel subtitle =
                new JLabel(
                        "AI-powered analysis of your inventory and sales data."
                );

        subtitle.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        subtitle.setForeground(
                MUTED
        );

        JLabel engine =
                new JLabel(
                        "GROQ  •  GPT-OSS 120B  •  Live database analysis"
                );

        engine.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        11
                )
        );

        engine.setForeground(
                AI_PURPLE
        );

        titleArea.add(title);

        titleArea.add(
                Box.createVerticalStrut(5)
        );

        titleArea.add(subtitle);

        titleArea.add(
                Box.createVerticalStrut(7)
        );

        titleArea.add(engine);

        header.add(
                titleArea,
                BorderLayout.WEST
        );

        analyzeButton =
                createPrimaryButton(
                        "Analyze Now"
                );

        analyzeButton.setPreferredSize(
                new Dimension(
                        125,
                        56
                )
        );

        analyzeButton.addActionListener(
                event ->
                        runAnalysis()
        );

        header.add(
                analyzeButton,
                BorderLayout.EAST
        );

        return header;
    }

    // =========================================================
    // OVERVIEW CARDS
    // =========================================================

    private JPanel createOverviewCards() {

        JPanel cards =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                14,
                                0
                        )
                );

        cards.setOpaque(false);

        productCountLabel =
                createOverviewValue(
                        "—"
                );

        salesCountLabel =
                createOverviewValue(
                        "—"
                );

        lowStockLabel =
                createOverviewValue(
                        "—"
                );

        cards.add(
                createOverviewCard(
                        "PRODUCTS ANALYZED",
                        productCountLabel,
                        "Current inventory records",
                        AI_PURPLE
                )
        );

        cards.add(
                createOverviewCard(
                        "SALES ANALYZED",
                        salesCountLabel,
                        "Recorded transactions",
                        new Color(
                                37,
                                99,
                                235
                        )
                )
        );

        cards.add(
                createOverviewCard(
                        "STOCK ATTENTION",
                        lowStockLabel,
                        "Products at or below reorder level",
                        WARNING
                )
        );

        return cards;
    }

    private JPanel createOverviewCard(
            String title,
            JLabel value,
            String description,
            Color accent
    ) {

        JPanel card =
                new JPanel(
                        new BorderLayout(
                                0,
                                6
                        )
                );

        card.setBackground(
                Color.WHITE
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                14,
                                17,
                                14,
                                17
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
                MUTED
        );

        JPanel indicator =
                new JPanel();

        indicator.setBackground(
                accent
        );

        indicator.setPreferredSize(
                new Dimension(
                        5,
                        20
                )
        );

        top.add(
                titleLabel,
                BorderLayout.WEST
        );

        top.add(
                indicator,
                BorderLayout.EAST
        );

        card.add(
                top,
                BorderLayout.NORTH
        );

        value.setForeground(
                TEXT
        );

        card.add(
                value,
                BorderLayout.CENTER
        );

        JLabel descriptionLabel =
                new JLabel(
                        description
                );

        descriptionLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        10
                )
        );

        descriptionLabel.setForeground(
                MUTED
        );

        card.add(
                descriptionLabel,
                BorderLayout.SOUTH
        );

        return card;
    }

    private JLabel createOverviewValue(
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
                        21
                )
        );

        return label;
    }

    // =========================================================
    // INSIGHTS CARD
    // =========================================================

    private JPanel createInsightsCard() {

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBackground(
                Color.WHITE
        );

        card.setBorder(
                BorderFactory.createLineBorder(
                        BORDER
                )
        );

        card.add(
                createInsightsHeader(),
                BorderLayout.NORTH
        );

        insightsPane =
                createInsightsPane();

        JScrollPane scrollPane =
                new JScrollPane(
                        insightsPane
                );

        scrollPane.setBorder(
                BorderFactory.createEmptyBorder()
        );

        scrollPane.getViewport()
                .setBackground(
                        Color.WHITE
                );

        scrollPane.getVerticalScrollBar()
                .setUnitIncrement(
                        16
                );

        card.add(
                scrollPane,
                BorderLayout.CENTER
        );

        return card;
    }

    private JPanel createInsightsHeader() {

        JPanel header =
                new JPanel(
                        new BorderLayout(
                                12,
                                0
                        )
                );

        header.setBackground(
                Color.WHITE
        );

        header.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0,
                                0,
                                1,
                                0,
                                BORDER
                        ),
                        new EmptyBorder(
                                14,
                                18,
                                14,
                                18
                        )
                )
        );

        JPanel icon =
                new JPanel(
                        new GridBagLayout()
                );

        icon.setBackground(
                AI_PURPLE
        );

        icon.setPreferredSize(
                new Dimension(
                        42,
                        42
                )
        );

        JLabel iconText =
                new JLabel(
                        "AI"
                );

        iconText.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        iconText.setForeground(
                Color.WHITE
        );

        icon.add(
                iconText
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

        JLabel title =
                new JLabel(
                        "Inventory Intelligence"
                );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        14
                )
        );

        title.setForeground(
                TEXT
        );

        JLabel subtitle =
                new JLabel(
                        "AI-generated assessment based on the latest database snapshot."
                );

        subtitle.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        11
                )
        );

        subtitle.setForeground(
                MUTED
        );

        text.add(title);

        text.add(
                Box.createVerticalStrut(3)
        );

        text.add(subtitle);

        header.add(
                icon,
                BorderLayout.WEST
        );

        header.add(
                text,
                BorderLayout.CENTER
        );

        statusLabel =
                new JLabel(
                        "Ready"
                );

        statusLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        10
                )
        );

        statusLabel.setForeground(
                SUCCESS
        );

        header.add(
                statusLabel,
                BorderLayout.EAST
        );

        return header;
    }

    // =========================================================
    // AI TEXT PANE
    // =========================================================

    private JTextPane createInsightsPane() {

        JTextPane pane =
                new JTextPane();

        pane.setEditable(
                false
        );

        pane.setContentType(
                "text/html"
        );

        HTMLEditorKit kit =
                new HTMLEditorKit();

        StyleSheet styleSheet =
                new StyleSheet();

        styleSheet.addRule(
                "body {" +
                        "font-family: SansSerif;" +
                        "font-size: 13px;" +
                        "color: rgb(15,23,42);" +
                        "background: white;" +
                        "margin: 0;" +
                        "padding: 22px 26px 28px 26px;" +
                        "line-height: 1.55;" +
                        "}"
        );

        styleSheet.addRule(
                ".hero {" +
                        "background: rgb(238,242,255);" +
                        "border: 1px solid rgb(199,210,254);" +
                        "padding: 16px 18px;" +
                        "margin-bottom: 22px;" +
                        "}"
        );

        styleSheet.addRule(
                ".hero-title {" +
                        "font-size: 17px;" +
                        "font-weight: bold;" +
                        "color: rgb(67,56,202);" +
                        "margin-bottom: 5px;" +
                        "}"
        );

        styleSheet.addRule(
                ".hero-text {" +
                        "font-size: 12px;" +
                        "color: rgb(71,85,105);" +
                        "}"
        );

        styleSheet.addRule(
                ".section {" +
                        "margin-top: 20px;" +
                        "margin-bottom: 22px;" +
                        "}"
        );

        styleSheet.addRule(
                ".section-title {" +
                        "font-size: 13px;" +
                        "font-weight: bold;" +
                        "color: rgb(67,56,202);" +
                        "padding-bottom: 8px;" +
                        "border-bottom: 1px solid rgb(226,232,240);" +
                        "margin-bottom: 10px;" +
                        "}"
        );

        styleSheet.addRule(
                ".item {" +
                        "margin: 0 0 9px 8px;" +
                        "color: rgb(30,41,59);" +
                        "}"
        );

        styleSheet.addRule(
                ".bullet {" +
                        "color: rgb(79,70,229);" +
                        "font-weight: bold;" +
                        "}"
        );

        styleSheet.addRule(
                ".emphasis {" +
                        "font-weight: bold;" +
                        "color: rgb(15,23,42);" +
                        "}"
        );

        styleSheet.addRule(
                ".footer {" +
                        "background: rgb(248,250,252);" +
                        "border: 1px solid rgb(226,232,240);" +
                        "padding: 12px 14px;" +
                        "margin-top: 20px;" +
                        "color: rgb(71,85,105);" +
                        "font-size: 11px;" +
                        "}"
        );

        kit.setStyleSheet(
                styleSheet
        );

        pane.setEditorKit(
                kit
        );

        pane.setBackground(
                Color.WHITE
        );

        pane.setBorder(
                BorderFactory.createEmptyBorder()
        );

        pane.setText(
                createWelcomeContent()
        );

        pane.setCaretPosition(
                0
        );

        return pane;
    }

    private String createWelcomeContent() {

        return
                "<html><body>" +

                        "<div class='hero'>" +
                        "<div class='hero-title'>" +
                        "SmartStock AI is ready." +
                        "</div>" +

                        "<div class='hero-text'>" +
                        "Run an analysis to turn your current " +
                        "inventory and sales data into a concise " +
                        "operational report." +
                        "</div>" +

                        "</div>" +

                        "<div class='section'>" +

                        "<div class='section-title'>" +
                        "WHAT THIS ANALYSIS COVERS" +
                        "</div>" +

                        "<div class='item'>" +
                        "<span class='bullet'>•</span> " +
                        "Current stock position and reorder risks" +
                        "</div>" +

                        "<div class='item'>" +
                        "<span class='bullet'>•</span> " +
                        "Product-level sales activity" +
                        "</div>" +

                        "<div class='item'>" +
                        "<span class='bullet'>•</span> " +
                        "Recorded sales and revenue performance" +
                        "</div>" +

                        "<div class='item'>" +
                        "<span class='bullet'>•</span> " +
                        "Replenishment priorities based on available data" +
                        "</div>" +

                        "</div>" +

                        "<div class='footer'>" +
                        "<b>Data source:</b> SmartStock AI's live MySQL " +
                        "inventory and sales records." +
                        "</div>" +

                        "</body></html>";
    }

    // =========================================================
    // RUN ANALYSIS
    // =========================================================

    private void runAnalysis() {

        analyzeButton.setEnabled(
                false
        );

        analyzeButton.setText(
                "Analyzing..."
        );

        statusLabel.setText(
                "Analyzing"
        );

        statusLabel.setForeground(
                AI_PURPLE
        );

        insightsPane.setText(
                createLoadingContent()
        );

        SwingWorker<String, Void> worker =
                new SwingWorker<>() {

                    @Override
                    protected String doInBackground()
                            throws Exception {

                        List<Product> products =
                                productService
                                        .getAllProducts();

                        List<Sale> sales =
                                saleService
                                        .getAllSales();

                        updateOverviewCounts(
                                products,
                                sales
                        );

                        return generateAIInsights(
                                products,
                                sales
                        );
                    }

                    @Override
                    protected void done() {

                        try {

                            String result =
                                    get();

                            if (
                                    result == null
                                            ||
                                            result.trim().isEmpty()
                            ) {

                                insightsPane.setText(
                                        createEmptyContent()
                                );

                                statusLabel.setText(
                                        "No insights"
                                );

                                statusLabel.setForeground(
                                        WARNING
                                );

                            } else {

                                insightsPane.setText(
                                        convertAITextToHtml(
                                                result
                                        )
                                );

                                statusLabel.setText(
                                        "Analysis complete"
                                );

                                statusLabel.setForeground(
                                        SUCCESS
                                );

                                insightsPane.setCaretPosition(
                                        0
                                );
                            }

                        } catch (Exception exception) {

                            insightsPane.setText(
                                    createErrorContent(
                                            getErrorMessage(
                                                    exception
                                            )
                                    )
                            );

                            statusLabel.setText(
                                    "Analysis failed"
                            );

                            statusLabel.setForeground(
                                    Theme.DANGER
                            );

                        } finally {

                            analyzeButton.setEnabled(
                                    true
                            );

                            analyzeButton.setText(
                                    "Analyze Again"
                            );
                        }
                    }
                };

        worker.execute();
    }

    // =========================================================
    // AI SERVICE COMPATIBILITY
    // =========================================================

    private String generateAIInsights(
            List<Product> products,
            List<Sale> sales
    ) throws Exception {

        Method[] methods =
                aiService
                        .getClass()
                        .getMethods();

        Method twoArgumentMethod =
                null;

        Method threeArgumentMethod =
                null;

        Object productSalesSummary =
                null;

        for (
                Method method :
                methods
        ) {

            if (
                    !method.getName()
                            .equals(
                                    "generateInsights"
                            )
            ) {

                continue;
            }

            Class<?>[] parameters =
                    method.getParameterTypes();

            if (
                    parameters.length == 2
            ) {

                twoArgumentMethod =
                        method;
            }

            if (
                    parameters.length == 3
            ) {

                threeArgumentMethod =
                        method;
            }
        }

        if (
                threeArgumentMethod != null
        ) {

            try {

                Method summaryMethod =
                        saleService
                                .getClass()
                                .getMethod(
                                        "getProductSalesSummary"
                                );

                productSalesSummary =
                        summaryMethod.invoke(
                                saleService
                        );

            } catch (
                    NoSuchMethodException ignored
            ) {

                productSalesSummary =
                        null;
            }

            if (
                    productSalesSummary != null
            ) {

                try {

                    return (String)
                            threeArgumentMethod.invoke(
                                    aiService,
                                    products,
                                    sales,
                                    productSalesSummary
                            );

                } catch (
                        IllegalArgumentException ignored
                ) {
                    // Fall through to two-argument method.
                }
            }
        }

        if (
                twoArgumentMethod != null
        ) {

            return (String)
                    twoArgumentMethod.invoke(
                            aiService,
                            products,
                            sales
                    );
        }

        throw new IllegalStateException(
                "InventoryAIService does not provide a compatible generateInsights method."
        );
    }

    // =========================================================
    // LOAD OVERVIEW
    // =========================================================

    private void loadOverview() {

        SwingWorker<int[], Void> worker =
                new SwingWorker<>() {

                    @Override
                    protected int[] doInBackground()
                            throws Exception {

                        List<Product> products =
                                productService
                                        .getAllProducts();

                        List<Sale> sales =
                                saleService
                                        .getAllSales();

                        int lowStock =
                                0;

                        if (
                                products != null
                        ) {

                            for (
                                    Product product :
                                    products
                            ) {

                                if (
                                        product != null
                                                &&
                                                product.isLowStock()
                                ) {

                                    lowStock++;
                                }
                            }
                        }

                        return new int[]{
                                products == null
                                        ? 0
                                        : products.size(),

                                sales == null
                                        ? 0
                                        : sales.size(),

                                lowStock
                        };
                    }

                    @Override
                    protected void done() {

                        try {

                            int[] values =
                                    get();

                            productCountLabel.setText(
                                    String.valueOf(
                                            values[0]
                                    )
                            );

                            salesCountLabel.setText(
                                    String.valueOf(
                                            values[1]
                                    )
                            );

                            lowStockLabel.setText(
                                    String.valueOf(
                                            values[2]
                                    )
                            );

                        } catch (Exception ignored) {
                        }
                    }
                };

        worker.execute();
    }

    // =========================================================
    // UPDATE OVERVIEW
    // =========================================================

    private void updateOverviewCounts(
            List<Product> products,
            List<Sale> sales
    ) {

        final int productsCount =
                products == null
                        ? 0
                        : products.size();

        final int salesCount =
                sales == null
                        ? 0
                        : sales.size();

        int lowStockCount =
                0;

        if (
                products != null
        ) {

            for (
                    Product product :
                    products
            ) {

                if (
                        product != null
                                &&
                                product.isLowStock()
                ) {

                    lowStockCount++;
                }
            }
        }

        /*
         * IMPORTANT:
         * The value is copied into a final variable before
         * entering the Swing lambda.
         */
        final int finalLowStockCount =
                lowStockCount;

        SwingUtilities.invokeLater(
                () -> {

                    productCountLabel.setText(
                            String.valueOf(
                                    productsCount
                            )
                    );

                    salesCountLabel.setText(
                            String.valueOf(
                                    salesCount
                            )
                    );

                    lowStockLabel.setText(
                            String.valueOf(
                                    finalLowStockCount
                            )
                    );
                }
        );
    }

    // =========================================================
    // AI TEXT TO HTML
    // =========================================================

    private String convertAITextToHtml(
            String rawText
    ) {

        String cleaned =
                rawText
                        .replace(
                                "\r\n",
                                "\n"
                        )
                        .replace(
                                "\r",
                                "\n"
                        )
                        .trim();

        cleaned =
                removeMarkdown(
                        cleaned
                );

        String[] lines =
                cleaned.split(
                        "\n"
                );

        StringBuilder html =
                new StringBuilder();

        html.append(
                "<html><body>"
        );

        html.append(
                "<div class='hero'>"
        );

        html.append(
                "<div class='hero-title'>"
        );

        html.append(
                "SmartStock AI Assessment"
        );

        html.append(
                "</div>"
        );

        html.append(
                "<div class='hero-text'>"
        );

        html.append(
                "Generated from the current inventory and sales snapshot."
        );

        html.append(
                "</div>"
        );

        html.append(
                "</div>"
        );

        boolean sectionOpen =
                false;

        boolean listOpen =
                false;

        for (
                String line :
                lines
        ) {

            String trimmed =
                    line.trim();

            if (
                    trimmed.isEmpty()
            ) {

                continue;
            }

            if (
                    isHeading(
                            trimmed
                    )
            ) {

                if (
                        listOpen
                ) {

                    html.append(
                            "</div>"
                    );

                    listOpen =
                            false;
                }

                if (
                        sectionOpen
                ) {

                    html.append(
                            "</div>"
                    );
                }

                html.append(
                        "<div class='section'>"
                );

                html.append(
                        "<div class='section-title'>"
                );

                html.append(
                        escapeHtml(
                                cleanHeading(
                                        trimmed
                                )
                        )
                );

                html.append(
                        "</div>"
                );

                sectionOpen =
                        true;

                continue;
            }

            if (
                    isBullet(
                            trimmed
                    )
            ) {

                if (
                        !listOpen
                ) {

                    html.append(
                            "<div>"
                    );

                    listOpen =
                            true;
                }

                String bulletText =
                        trimmed
                                .replaceFirst(
                                        "^[•●▪◦\\-*]+\\s*",
                                        ""
                                )
                                .trim();

                html.append(
                        "<div class='item'>"
                );

                html.append(
                        "<span class='bullet'>•</span> "
                );

                html.append(
                        formatInlineText(
                                bulletText
                        )
                );

                html.append(
                        "</div>"
                );

                continue;
            }

            if (
                    listOpen
            ) {

                html.append(
                        "</div>"
                );

                listOpen =
                        false;
            }

            html.append(
                    "<div class='item'>"
            );

            html.append(
                    formatInlineText(
                            trimmed
                    )
            );

            html.append(
                    "</div>"
            );
        }

        if (
                listOpen
        ) {

            html.append(
                    "</div>"
            );
        }

        if (
                sectionOpen
        ) {

            html.append(
                    "</div>"
            );
        }

        html.append(
                "<div class='footer'>"
        );

        html.append(
                "<b>Next check:</b> Re-run the analysis after " +
                        "meaningful inventory or sales changes."
        );

        html.append(
                "</div>"
        );

        html.append(
                "</body></html>"
        );

        return html.toString();
    }

    // =========================================================
    // MARKDOWN CLEANUP
    // =========================================================

    private String removeMarkdown(
            String text
    ) {

        return text
                .replaceAll(
                        "```[\\s\\S]*?```",
                        ""
                )
                .replace(
                        "**",
                        ""
                )
                .replace(
                        "__",
                        ""
                )
                .replaceAll(
                        "`([^`]*)`",
                        "$1"
                )
                .replaceAll(
                        "^#{1,6}\\s*",
                        ""
                );
    }

    private boolean isHeading(
            String text
    ) {

        String normalized =
                text
                        .replaceAll(
                                "^[#\\s]+",
                                ""
                        )
                        .trim()
                        .toUpperCase();

        return normalized.equals(
                "EXECUTIVE SUMMARY"
        )
                ||
                normalized.equals(
                        "STOCK RISKS"
                )
                ||
                normalized.equals(
                        "PRODUCT DEMAND"
                )
                ||
                normalized.equals(
                        "SALES PERFORMANCE"
                )
                ||
                normalized.equals(
                        "SALES ANALYSIS"
                )
                ||
                normalized.equals(
                        "REPLENISHMENT PRIORITY"
                )
                ||
                normalized.equals(
                        "RECOMMENDED ACTIONS"
                )
                ||
                normalized.equals(
                        "NEXT CHECK"
                )
                ||
                normalized.equals(
                        "INVENTORY OVERVIEW"
                );
    }

    private String cleanHeading(
            String text
    ) {

        return text
                .replaceAll(
                        "^[#\\s]+",
                        ""
                )
                .replace(
                        "**",
                        ""
                )
                .trim()
                .toUpperCase();
    }

    private boolean isBullet(
            String text
    ) {

        return text.matches(
                "^[•●▪◦\\-*]\\s+.*"
        );
    }

    private String formatInlineText(
            String text
    ) {

        String escaped =
                escapeHtml(
                        text
                );

        escaped =
                escaped.replaceAll(
                        "(?i)\\b(LOW STOCK|REORDER|REPLENISH|URGENT|RISK|OUT OF STOCK)\\b",
                        "<span class='emphasis'>$1</span>"
                );

        return escaped;
    }

    private String escapeHtml(
            String text
    ) {

        if (
                text == null
        ) {

            return "";
        }

        return text
                .replace(
                        "&",
                        "&amp;"
                )
                .replace(
                        "<",
                        "&lt;"
                )
                .replace(
                        ">",
                        "&gt;"
                )
                .replace(
                        "\"",
                        "&quot;"
                )
                .replace(
                        "'",
                        "&#39;"
                );
    }

    // =========================================================
    // LOADING STATE
    // =========================================================

    private String createLoadingContent() {

        return
                "<html><body>" +

                        "<div class='hero'>" +
                        "<div class='hero-title'>" +
                        "Analyzing your inventory..." +
                        "</div>" +

                        "<div class='hero-text'>" +
                        "SmartStock AI is reviewing current stock, " +
                        "sales activity and available demand data." +
                        "</div>" +

                        "</div>" +

                        "<div class='section'>" +

                        "<div class='section-title'>" +
                        "PROCESSING" +
                        "</div>" +

                        "<div class='item'>" +
                        "<span class='bullet'>•</span> " +
                        "Reading live inventory records" +
                        "</div>" +

                        "<div class='item'>" +
                        "<span class='bullet'>•</span> " +
                        "Reviewing recorded sales" +
                        "</div>" +

                        "<div class='item'>" +
                        "<span class='bullet'>•</span> " +
                        "Building demand context" +
                        "</div>" +

                        "<div class='item'>" +
                        "<span class='bullet'>•</span> " +
                        "Generating operational recommendations" +
                        "</div>" +

                        "</div>" +

                        "</body></html>";
    }

    // =========================================================
    // EMPTY STATE
    // =========================================================

    private String createEmptyContent() {

        return
                "<html><body>" +

                        "<div class='hero'>" +
                        "<div class='hero-title'>" +
                        "No analysis was returned." +
                        "</div>" +

                        "<div class='hero-text'>" +
                        "The AI service returned an empty response. " +
                        "Run the analysis again." +
                        "</div>" +

                        "</div>" +

                        "</body></html>";
    }

    // =========================================================
    // ERROR STATE
    // =========================================================

    private String createErrorContent(
            String message
    ) {

        return
                "<html><body>" +

                        "<div class='hero'>" +

                        "<div class='hero-title'>" +
                        "Analysis could not be completed." +
                        "</div>" +

                        "<div class='hero-text'>" +
                        escapeHtml(
                                message
                        ) +
                        "</div>" +

                        "</div>" +

                        "<div class='section'>" +

                        "<div class='section-title'>" +
                        "WHAT TO CHECK" +
                        "</div>" +

                        "<div class='item'>" +
                        "<span class='bullet'>•</span> " +
                        "Confirm the GROQ_API_KEY environment variable is available." +
                        "</div>" +

                        "<div class='item'>" +
                        "<span class='bullet'>•</span> " +
                        "Confirm the database connection is working." +
                        "</div>" +

                        "<div class='item'>" +
                        "<span class='bullet'>•</span> " +
                        "Run the analysis again." +
                        "</div>" +

                        "</div>" +

                        "</body></html>";
    }

    // =========================================================
    // PRIMARY BUTTON
    // =========================================================

    private JButton createPrimaryButton(
            String text
    ) {

        JButton button =
                new JButton(
                        text
                );

        button.setBackground(
                AI_PURPLE
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
    // ERROR MESSAGE
    // =========================================================

    private String getErrorMessage(
            Exception exception
    ) {

        Throwable cause =
                exception;

        while (
                cause.getCause() != null
        ) {

            cause =
                    cause.getCause();
        }

        if (
                cause instanceof InvocationTargetException
        ) {

            Throwable target =
                    ((InvocationTargetException)
                            cause)
                            .getTargetException();

            if (
                    target != null
                            &&
                            target.getMessage() != null
            ) {

                return target.getMessage();
            }
        }

        if (
                cause.getMessage() != null
        ) {

            return cause.getMessage();
        }

        return "An unexpected error occurred.";
    }
}