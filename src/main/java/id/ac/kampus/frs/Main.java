package id.ac.kampus.frs;

import com.formdev.flatlaf.FlatLightLaf;
import id.ac.kampus.frs.ui.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Modern clean look & feel
                FlatLightLaf.setup();
            } catch (Exception ignored) {}

            new LoginFrame().setVisible(true);
        });
    }
}
