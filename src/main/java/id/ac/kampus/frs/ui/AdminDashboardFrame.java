package id.ac.kampus.frs.ui;

import id.ac.kampus.frs.model.User;

import javax.swing.*;
import java.awt.*;

public class AdminDashboardFrame extends JFrame {
    private final User user;

    public AdminDashboardFrame(User user) {
        this.user = user;
        setTitle("Dashboard Admin - " + user.getUsername());
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        buildUI();
    }

    private void buildUI() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Pengaturan", buildPengaturanPanel());
        tabs.addTab("Master Data", new JLabel("CRUD Mahasiswa/Dosen/Mata Kuliah (coming soon)"));
        tabs.addTab("Laporan", new JLabel("Rekap dan Statistik (coming soon)"));
        add(tabs);
    }

    private JPanel buildPengaturanPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8,8,8,8);
        gc.anchor = GridBagConstraints.WEST;

        int r=0;
        p.add(new JLabel("Semester Aktif:"), pos(gc,0,r));
        JTextField tfSemester = new JTextField("3", 5);
        p.add(tfSemester, pos(gc,1,r++));

        JCheckBox cbPeriode = new JCheckBox("Aktifkan periode FRS");
        cbPeriode.setSelected(true);
        p.add(cbPeriode, pos(gc,1,r++));

        JButton btnSimpan = new JButton("Simpan Pengaturan");
        btnSimpan.addActionListener(e -> JOptionPane.showMessageDialog(this, "Disimpan (demo)", "Info", JOptionPane.INFORMATION_MESSAGE));
        p.add(btnSimpan, pos(gc,1,r));

        return p;
    }

    private GridBagConstraints pos(GridBagConstraints base, int x, int y) {
        GridBagConstraints gc = (GridBagConstraints) base.clone();
        gc.gridx = x; gc.gridy = y;
        return gc;
    }
}
