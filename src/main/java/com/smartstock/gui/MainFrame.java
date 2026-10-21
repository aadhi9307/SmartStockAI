package com.smartstock.gui;

import com.smartstock.ai.AIInsightsPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainFrame extends JFrame {

    private final JPanel contentPanel;
    private final CardLayout cardLayout;
    private final Map<String, JButton> navigationButtons;

    private JButton activeButton;

    private static final String DASHBOARD = "dashboard";
    private static final String PRODUCTS = "products";
    private static final String INVENTORY = "inventory";
    private static final String SUPPLIERS = "suppliers";
    private static final String SALES = "sales";
    private static final String ANALYTICS = "analytics";
    private static final String AI_INSIGHTS = "ai_insights";
    private static final String REPORTS = "reports";
    private static final String SETTINGS = "settings";

    public MainFrame() {
        navigationButtons = new LinkedHashMap<>();

        setTitle("SmartStock AI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setMinimumSize(new Dimension(1100, 700));
        setSize(1366, 768);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();

        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Theme.BACKGROUND);

        initializePanels();
        initializeFrame();

        showPage(DASHBOARD);
    }

    private void initializeFrame() {
        setLayout(new BorderLayout());

        add(createSidebar(), BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void initializePanels() {
        contentPanel.add(
                new DashboardPanel(),
                DASHBOARD
        );

        contentPanel.add(
                new ProductPanel(),
                PRODUCTS
        );

        contentPanel.add(
                new InventoryPanel(),
                INVENTORY
        );

        contentPanel.add(
                new SupplierPanel(),
                SUPPLIERS
        );

        contentPanel.add(
                new SalesPanel(),
                SALES
        );

        contentPanel.add(
                new AnalyticsPanel(),
                ANALYTICS
        );

        contentPanel.add(
                new AIInsightsPanel(),
                AI_INSIGHTS
        );

        contentPanel.add(
                new ReportsPanel(),
                REPORTS
        );

        contentPanel.add(
                new SettingsPanel(),
                SETTINGS
        );
    }

    private JPanel createSidebar() {

        JPanel sidebar = new JPanel(
                new BorderLayout()
        );

        sidebar.setPreferredSize(
                new Dimension(240, 0)
        );

        sidebar.setBackground(
                Theme.SIDEBAR
        );

        sidebar.add(
                createBrand(),
                BorderLayout.NORTH
        );

        sidebar.add(
                createNavigation(),
                BorderLayout.CENTER
        );

        sidebar.add(
                createUserSection(),
                BorderLayout.SOUTH
        );

        return sidebar;
    }

    private JPanel createBrand() {

        JPanel brandPanel = new JPanel();

        brandPanel.setLayout(
                new BoxLayout(
                        brandPanel,
                        BoxLayout.Y_AXIS
                )
        );

        brandPanel.setBackground(
                Theme.SIDEBAR
        );

        brandPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        30,
                        24,
                        26,
                        24
                )
        );

        JPanel logoLine = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT,
                        0,
                        0
                )
        );

        logoLine.setOpaque(false);

        JLabel smartLabel = new JLabel(
                "SMARTSTOCK "
        );

        smartLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        21
                )
        );

        smartLabel.setForeground(
                Color.WHITE
        );

        JLabel aiLabel = new JLabel(
                "AI"
        );

        aiLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        21
                )
        );

        aiLabel.setForeground(
                Theme.PRIMARY
        );

        logoLine.add(smartLabel);
        logoLine.add(aiLabel);

        JLabel subtitle = new JLabel(
                "Inventory Intelligence"
        );

        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        subtitle.setForeground(
                new Color(
                        148,
                        163,
                        184
                )
        );

        subtitle.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        brandPanel.add(logoLine);
        brandPanel.add(
                Box.createVerticalStrut(4)
        );
        brandPanel.add(subtitle);

        return brandPanel;
    }

    private JPanel createNavigation() {

        JPanel navigation = new JPanel();

        navigation.setLayout(
                new BoxLayout(
                        navigation,
                        BoxLayout.Y_AXIS
                )
        );

        navigation.setBackground(
                Theme.SIDEBAR
        );

        navigation.setBorder(
                BorderFactory.createEmptyBorder(
                        0,
                        12,
                        10,
                        12
                )
        );

        addSectionLabel(
                navigation,
                "MAIN"
        );

        addNavigationButton(
                navigation,
                "Dashboard",
                DASHBOARD
        );

        addNavigationButton(
                navigation,
                "Products",
                PRODUCTS
        );

        addNavigationButton(
                navigation,
                "Inventory",
                INVENTORY
        );

        addNavigationButton(
                navigation,
                "Suppliers",
                SUPPLIERS
        );

        addSectionSpacing(
                navigation
        );

        addSectionLabel(
                navigation,
                "BUSINESS"
        );

        addNavigationButton(
                navigation,
                "Sales",
                SALES
        );

        addNavigationButton(
                navigation,
                "Analytics",
                ANALYTICS
        );

        addNavigationButton(
                navigation,
                "AI Insights",
                AI_INSIGHTS
        );

        addNavigationButton(
                navigation,
                "Reports",
                REPORTS
        );

        navigation.add(
                Box.createVerticalGlue()
        );

        addNavigationButton(
                navigation,
                "Settings",
                SETTINGS
        );

        return navigation;
    }

    private void addSectionLabel(
            JPanel navigation,
            String text
    ) {

        JLabel label = new JLabel(
                text
        );

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        10
                )
        );

        label.setForeground(
                new Color(
                        100,
                        116,
                        139
                )
        );

        label.setBorder(
                BorderFactory.createEmptyBorder(
                        8,
                        12,
                        10,
                        12
                )
        );

        label.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        navigation.add(label);
    }

    private void addSectionSpacing(
            JPanel navigation
    ) {

        navigation.add(
                Box.createVerticalStrut(18)
        );
    }

    private void addNavigationButton(
            JPanel navigation,
            String text,
            String page
    ) {

        JButton button = new JButton(
                text
        );

        button.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        button.setForeground(
                new Color(
                        226,
                        232,
                        240
                )
        );

        button.setBackground(
                Theme.SIDEBAR
        );

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        11,
                        20,
                        11,
                        12
                )
        );

        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);

        button.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        button.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        44
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        navigationButtons.put(
                page,
                button
        );

        button.addActionListener(
                e -> showPage(page)
        );

        button.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        if (button != activeButton) {
                            button.setBackground(
                                    Theme.SIDEBAR_HOVER
                            );
                        }
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        if (button != activeButton) {
                            button.setBackground(
                                    Theme.SIDEBAR
                            );
                        }
                    }
                }
        );

        navigation.add(button);
    }

    private JPanel createUserSection() {

        JPanel outer = new JPanel(
                new BorderLayout()
        );

        outer.setBackground(
                Theme.SIDEBAR
        );

        outer.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        12,
                        18,
                        12
                )
        );

        JPanel userPanel = new JPanel(
                new BorderLayout(10, 0)
        );

        userPanel.setBackground(
                new Color(
                        30,
                        41,
                        59
                )
        );

        userPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        12,
                        10,
                        12
                )
        );

        JLabel avatar = new JLabel(
                "SS"
        );

        avatar.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        avatar.setVerticalAlignment(
                SwingConstants.CENTER
        );

        avatar.setPreferredSize(
                new Dimension(
                        36,
                        36
                )
        );

        avatar.setOpaque(true);

        avatar.setBackground(
                Theme.PRIMARY
        );

        avatar.setForeground(
                Color.WHITE
        );

        avatar.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        JPanel details = new JPanel();

        details.setLayout(
                new BoxLayout(
                        details,
                        BoxLayout.Y_AXIS
                )
        );

        details.setOpaque(false);

        JLabel name = new JLabel(
                "Administrator"
        );

        name.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        name.setForeground(
                Color.WHITE
        );

        JLabel role = new JLabel(
                "Inventory Manager"
        );

        role.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        10
                )
        );

        role.setForeground(
                new Color(
                        148,
                        163,
                        184
                )
        );

        details.add(name);

        details.add(
                Box.createVerticalStrut(3)
        );

        details.add(role);

        userPanel.add(
                avatar,
                BorderLayout.WEST
        );

        userPanel.add(
                details,
                BorderLayout.CENTER
        );

        outer.add(
                userPanel,
                BorderLayout.CENTER
        );

        return outer;
    }

    private void showPage(
            String page
    ) {

        if (!navigationButtons.containsKey(page)) {
            return;
        }

        cardLayout.show(
                contentPanel,
                page
        );

        updateActiveNavigation(
                page
        );
    }

    private void updateActiveNavigation(
            String page
    ) {

        if (activeButton != null) {

            activeButton.setBackground(
                    Theme.SIDEBAR
            );

            activeButton.setForeground(
                    new Color(
                            226,
                            232,
                            240
                    )
            );

            activeButton.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            14
                    )
            );
        }

        activeButton =
                navigationButtons.get(page);

        if (activeButton != null) {

            activeButton.setBackground(
                    Theme.SIDEBAR_ACTIVE
            );

            activeButton.setForeground(
                    Color.WHITE
            );

            activeButton.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            14
                    )
            );
        }
    }
}