package com.smartstock;

import com.smartstock.gui.MainFrame;
import com.smartstock.gui.Theme;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {
                Theme.apply();

                MainFrame mainFrame = new MainFrame();

                mainFrame.setVisible(true);

            } catch (Exception e) {

                e.printStackTrace();

                JOptionPane.showMessageDialog(
                        null,
                        "SmartStock AI could not start.\n\n"
                                + e.getMessage(),
                        "Startup Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}