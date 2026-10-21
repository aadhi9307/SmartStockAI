package com.smartstock.gui;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Central visual system for SmartStock AI.
 *
 * All major GUI components should use these constants
 * instead of defining their own colors and dimensions.
 */
public final class Theme {

    private Theme() {
    }

    // =========================================================
    // BRAND
    // =========================================================

    public static final Color PRIMARY =
            new Color(79, 70, 229);

    public static final Color PRIMARY_HOVER =
            new Color(67, 56, 202);

    public static final Color PRIMARY_DARK =
            new Color(67, 56, 202);

    public static final Color PRIMARY_LIGHT =
            new Color(238, 242, 255);

    public static final Color PRIMARY_SOFT =
            new Color(224, 231, 255);

    // =========================================================
    // APPLICATION BACKGROUND
    // =========================================================

    public static final Color BACKGROUND =
            new Color(248, 250, 252);

    public static final Color SURFACE =
            Color.WHITE;

    public static final Color SURFACE_ALT =
            new Color(248, 250, 252);

    public static final Color SURFACE_HOVER =
            new Color(245, 247, 250);

    // =========================================================
    // SIDEBAR
    // =========================================================

    public static final Color SIDEBAR =
            new Color(15, 23, 42);

    public static final Color SIDEBAR_HOVER =
            new Color(30, 41, 59);

    public static final Color SIDEBAR_ACTIVE =
            new Color(79, 70, 229);

    public static final Color SIDEBAR_TEXT =
            new Color(203, 213, 225);

    public static final Color SIDEBAR_MUTED =
            new Color(100, 116, 139);

    public static final Color SIDEBAR_DIVIDER =
            new Color(51, 65, 85);

    // =========================================================
    // TEXT
    // =========================================================

    public static final Color TEXT_PRIMARY =
            new Color(15, 23, 42);

    public static final Color TEXT_SECONDARY =
            new Color(71, 85, 105);

    public static final Color TEXT_MUTED =
            new Color(100, 116, 139);

    public static final Color TEXT_DISABLED =
            new Color(148, 163, 184);

    // =========================================================
    // BORDERS / DIVIDERS
    // =========================================================

    public static final Color BORDER =
            new Color(226, 232, 240);

    public static final Color BORDER_LIGHT =
            new Color(241, 245, 249);

    public static final Color BORDER_DARK =
            new Color(203, 213, 225);

    // =========================================================
    // STATUS COLORS
    // =========================================================

    public static final Color SUCCESS =
            new Color(22, 163, 74);

    public static final Color SUCCESS_LIGHT =
            new Color(240, 253, 244);

    public static final Color WARNING =
            new Color(217, 119, 6);

    public static final Color WARNING_LIGHT =
            new Color(255, 251, 235);

    public static final Color DANGER =
            new Color(220, 38, 38);

    public static final Color DANGER_LIGHT =
            new Color(254, 242, 242);

    public static final Color INFO =
            new Color(37, 99, 235);

    public static final Color INFO_LIGHT =
            new Color(239, 246, 255);

    // =========================================================
    // CHART COLORS
    // =========================================================

    public static final Color CHART_PRIMARY =
            new Color(79, 70, 229);

    public static final Color CHART_SECONDARY =
            new Color(59, 130, 246);

    public static final Color CHART_SUCCESS =
            new Color(16, 185, 129);

    public static final Color CHART_WARNING =
            new Color(245, 158, 11);

    public static final Color CHART_DANGER =
            new Color(239, 68, 68);

    // =========================================================
    // DIMENSIONS
    // =========================================================

    public static final int SIDEBAR_WIDTH = 248;

    public static final int PAGE_PADDING = 28;

    public static final int CARD_PADDING = 20;

    public static final int SMALL_GAP = 8;

    public static final int MEDIUM_GAP = 14;

    public static final int LARGE_GAP = 22;

    public static final int CARD_RADIUS = 12;

    public static final int BUTTON_RADIUS = 8;

    public static final int INPUT_RADIUS = 8;

    // =========================================================
    // FONTS
    // =========================================================

    public static final Font PAGE_TITLE_FONT =
            new Font(
                    "SansSerif",
                    Font.BOLD,
                    25
            );

    public static final Font PAGE_SUBTITLE_FONT =
            new Font(
                    "SansSerif",
                    Font.PLAIN,
                    13
            );

    public static final Font SECTION_TITLE_FONT =
            new Font(
                    "SansSerif",
                    Font.BOLD,
                    16
            );

    public static final Font CARD_TITLE_FONT =
            new Font(
                    "SansSerif",
                    Font.BOLD,
                    13
            );

    public static final Font BODY_FONT =
            new Font(
                    "SansSerif",
                    Font.PLAIN,
                    13
            );

    public static final Font BODY_BOLD_FONT =
            new Font(
                    "SansSerif",
                    Font.BOLD,
                    13
            );

    public static final Font SMALL_FONT =
            new Font(
                    "SansSerif",
                    Font.PLAIN,
                    11
            );

    public static final Font SMALL_BOLD_FONT =
            new Font(
                    "SansSerif",
                    Font.BOLD,
                    11
            );

    public static final Font STAT_VALUE_FONT =
            new Font(
                    "SansSerif",
                    Font.BOLD,
                    25
            );

    // =========================================================
    // BORDERS
    // =========================================================

    public static Border cardBorder() {

        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        BORDER,
                        1
                ),
                BorderFactory.createEmptyBorder(
                        CARD_PADDING,
                        CARD_PADDING,
                        CARD_PADDING,
                        CARD_PADDING
                )
        );
    }

    public static Border lightCardBorder() {

        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        BORDER_LIGHT,
                        1
                ),
                BorderFactory.createEmptyBorder(
                        CARD_PADDING,
                        CARD_PADDING,
                        CARD_PADDING,
                        CARD_PADDING
                )
        );
    }

    public static Border pageBorder() {

        return BorderFactory.createEmptyBorder(
                PAGE_PADDING,
                PAGE_PADDING,
                PAGE_PADDING,
                PAGE_PADDING
        );
    }

    public static Border compactBorder() {

        return BorderFactory.createEmptyBorder(
                10,
                12,
                10,
                12
        );
    }

    // =========================================================
    // GLOBAL LOOK & FEEL
    // =========================================================

    public static void apply() {

        try {

            FlatLightLaf.setup();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to initialize SmartStock theme.",
                    e
            );
        }

        // -----------------------------------------------------
        // Global component shape
        // -----------------------------------------------------

        UIManager.put(
                "Component.arc",
                CARD_RADIUS
        );

        UIManager.put(
                "Button.arc",
                BUTTON_RADIUS
        );

        UIManager.put(
                "TextComponent.arc",
                INPUT_RADIUS
        );

        UIManager.put(
                "ProgressBar.arc",
                8
        );

        UIManager.put(
                "ScrollBar.width",
                10
        );

        UIManager.put(
                "ScrollBar.thumbArc",
                10
        );

        // -----------------------------------------------------
        // Buttons
        // -----------------------------------------------------

        UIManager.put(
                "Button.font",
                BODY_BOLD_FONT
        );

        UIManager.put(
                "Button.margin",
                new Insets(
                        9,
                        15,
                        9,
                        15
                )
        );

        UIManager.put(
                "Button.focusWidth",
                0
        );

        UIManager.put(
                "Button.innerFocusWidth",
                0
        );

        // -----------------------------------------------------
        // Text fields
        // -----------------------------------------------------

        UIManager.put(
                "TextField.font",
                BODY_FONT
        );

        UIManager.put(
                "TextField.margin",
                new Insets(
                        9,
                        11,
                        9,
                        11
                )
        );

        UIManager.put(
                "TextField.focusWidth",
                1
        );

        // -----------------------------------------------------
        // Combo boxes
        // -----------------------------------------------------

        UIManager.put(
                "ComboBox.font",
                BODY_FONT
        );

        UIManager.put(
                "ComboBox.padding",
                new Insets(
                        4,
                        8,
                        4,
                        8
                )
        );

        // -----------------------------------------------------
        // Tables
        // -----------------------------------------------------

        UIManager.put(
                "Table.font",
                BODY_FONT
        );

        UIManager.put(
                "Table.rowHeight",
                42
        );

        UIManager.put(
                "Table.showHorizontalLines",
                false
        );

        UIManager.put(
                "Table.showVerticalLines",
                false
        );

        UIManager.put(
                "Table.intercellSpacing",
                new Dimension(
                        0,
                        0
                )
        );

        UIManager.put(
                "Table.cellNoFocusBorder",
                BorderFactory.createEmptyBorder()
        );

        UIManager.put(
                "Table.focusCellHighlightBorder",
                BorderFactory.createEmptyBorder()
        );

        UIManager.put(
                "TableHeader.font",
                SMALL_BOLD_FONT
        );

        UIManager.put(
                "TableHeader.height",
                40
        );

        UIManager.put(
                "TableHeader.bottomSeparatorHeight",
                1
        );

        // -----------------------------------------------------
        // Scroll panes
        // -----------------------------------------------------

        UIManager.put(
                "ScrollPane.border",
                BorderFactory.createEmptyBorder()
        );

        UIManager.put(
                "ScrollPane.background",
                BACKGROUND
        );

        // -----------------------------------------------------
        // Tooltips
        // -----------------------------------------------------

        UIManager.put(
                "ToolTip.font",
                SMALL_FONT
        );

        UIManager.put(
                "ToolTip.background",
                new Color(
                        15,
                        23,
                        42
                )
        );

        UIManager.put(
                "ToolTip.foreground",
                Color.WHITE
        );

        UIManager.put(
                "ToolTip.border",
                BorderFactory.createEmptyBorder(
                        6,
                        9,
                        6,
                        9
                )
        );

        // -----------------------------------------------------
        // Tabs
        // -----------------------------------------------------

        UIManager.put(
                "TabbedPane.showTabSeparators",
                false
        );

        UIManager.put(
                "TabbedPane.tabHeight",
                38
        );

        // -----------------------------------------------------
        // Labels
        // -----------------------------------------------------

        UIManager.put(
                "Label.font",
                BODY_FONT
        );

        UIManager.put(
                "Label.foreground",
                TEXT_PRIMARY
        );

        // -----------------------------------------------------
        // Panels
        // -----------------------------------------------------

        UIManager.put(
                "Panel.background",
                BACKGROUND
        );
    }
}