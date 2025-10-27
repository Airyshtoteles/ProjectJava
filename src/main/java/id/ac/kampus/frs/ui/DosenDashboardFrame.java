package id.ac.kampus.frs.ui;

import id.ac.kampus.frs.model.User;

import javax.swing.*;
import java.awt.*;

public class DosenDashboardFrame extends JFrame {
    private final User user;

    public DosenDashboardFrame(User user) {
        this.user = user;
        setTitle("Dashboard Dosen - " + user.getUsername());
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        buildUI();
    }

    private void buildUI() {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel("Daftar pengajuan FRS mahasiswa (coming soon)"), BorderLayout.CENTER);
        add(p);
    }
}
