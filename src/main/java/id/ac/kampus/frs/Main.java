package id.ac.kampus.frs;

import id.ac.kampus.frs.ui.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Native look and feel when available
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            new LoginFrame().setVisible(true);
        });
    }
}
