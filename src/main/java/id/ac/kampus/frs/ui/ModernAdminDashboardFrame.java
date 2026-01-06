package id.ac.kampus.frs.ui;

import id.ac.kampus.frs.model.User;
import id.ac.kampus.frs.dao.SettingsDAO;
import id.ac.kampus.frs.model.Settings;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;

/** Dashboard Admin bertema modern (placeholder navigasi dasar). */
public class ModernAdminDashboardFrame extends BaseDashboardFrame {
    public ModernAdminDashboardFrame(User user) {
        super("Dashboard Admin");
        setUserInfo(user.getUsername());
        // Embed panel-panel lama yang sudah fungsional
        registerPage("Pengaturan", "Pengaturan", buildPengaturanPanel());
        registerPage("Mahasiswa", "Mahasiswa", new PanelMasterMahasiswa());
        registerPage("Dosen", "Dosen", new PanelMasterDosen());
        registerPage("Mata Kuliah", "Mata Kuliah", new PanelMasterMataKuliah());
        registerPage("Laporan", "Laporan", new PanelLaporanPersetujuan());
        registerPage("Permintaan Password", "Permintaan Password", new PanelPermintaanPassword(user.getIdUser()));

        // Badge notifikasi jumlah pending permintaan password
        try {
            int pending = new id.ac.kampus.frs.dao.PasswordChangeRequestDAO().listPending().size();
            setSidebarBadge("Permintaan Password", pending);
        } catch (Exception ignored) {}

        showPage("Pengaturan");
    }

    private JComponent buildPengaturanPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        
        UITheme.CardPanel card = new UITheme.CardPanel(20);
        card.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(10, 10, 10, 10);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;

        SettingsDAO dao = new SettingsDAO();
        Settings s;
        try { s = dao.get(); } catch (Exception ex) { s = new Settings(); s.setSemesterAktif(1); s.setFrsAktif(true); }

        int r = 0;
        
        // Title
        JLabel title = new JLabel("Pengaturan Periode FRS");
        title.setFont(UITheme.uiFont(Font.BOLD, 18));
        title.setForeground(UITheme.TEXT);
        gc.gridx = 0; gc.gridy = r; gc.gridwidth = 2;
        card.add(title, gc);
        gc.gridwidth = 1;
        r++;

        // Semester Aktif
        gc.gridx = 0; gc.gridy = r;
        card.add(label("Semester Aktif:"), gc);
        JSpinner spSemester = new JSpinner(new SpinnerNumberModel(s.getSemesterAktif(), 1, 14, 1));
        gc.gridx = 1;
        card.add(spSemester, gc);
        r++;

        // Checkbox FRS Aktif
        JCheckBox cbPeriode = new JCheckBox("Aktifkan Periode FRS");
        cbPeriode.setSelected(s.isFrsAktif());
        cbPeriode.setOpaque(false);
        cbPeriode.setFont(UITheme.uiFont(Font.PLAIN, 14));
        gc.gridx = 1; gc.gridy = r;
        card.add(cbPeriode, gc);
        r++;

        // Tanggal Mulai
        gc.gridx = 0; gc.gridy = r;
        card.add(label("Tanggal & Jam Mulai:"), gc);
        JPanel mulaiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        mulaiPanel.setOpaque(false);
        DatePicker dpMulai = new DatePicker();
        TimePicker tpMulai = new TimePicker();
        JButton btnClrMulai = new JButton("Kosongkan");
        if (s.getTanggalMulai() != null) {
            dpMulai.setDate(s.getTanggalMulai().toLocalDate());
            tpMulai.setTime(s.getTanggalMulai().toLocalTime());
        }
        btnClrMulai.addActionListener(e -> { dpMulai.setDate(null); tpMulai.setTime(null); });
        mulaiPanel.add(dpMulai);
        mulaiPanel.add(tpMulai);
        mulaiPanel.add(btnClrMulai);
        gc.gridx = 1;
        card.add(mulaiPanel, gc);
        r++;

        // Tanggal Selesai
        gc.gridx = 0; gc.gridy = r;
        card.add(label("Tanggal & Jam Selesai:"), gc);
        JPanel selesaiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        selesaiPanel.setOpaque(false);
        DatePicker dpSelesai = new DatePicker();
        TimePicker tpSelesai = new TimePicker();
        JButton btnClrSelesai = new JButton("Kosongkan");
        if (s.getTanggalSelesai() != null) {
            dpSelesai.setDate(s.getTanggalSelesai().toLocalDate());
            tpSelesai.setTime(s.getTanggalSelesai().toLocalTime());
        }
        btnClrSelesai.addActionListener(e -> { dpSelesai.setDate(null); tpSelesai.setTime(null); });
        selesaiPanel.add(dpSelesai);
        selesaiPanel.add(tpSelesai);
        selesaiPanel.add(btnClrSelesai);
        gc.gridx = 1;
        card.add(selesaiPanel, gc);
        r++;

        // Status Periode Saat Ini
        gc.gridx = 0; gc.gridy = r;
        card.add(label("Status Periode Saat Ini:"), gc);
        JLabel lblStatus = new JLabel();
        lblStatus.setFont(UITheme.uiFont(Font.BOLD, 14));
        updateStatusLabel(lblStatus, dao);
        gc.gridx = 1;
        card.add(lblStatus, gc);
        r++;

        // Tombol Simpan
        UITheme.AnimatedButton btnSimpan = new UITheme.AnimatedButton("Simpan Pengaturan");
        btnSimpan.setPreferredSize(new Dimension(180, 40));
        btnSimpan.addActionListener(e -> {
            try {
                Settings ns = new Settings();
                ns.setSemesterAktif((Integer) spSemester.getValue());
                ns.setFrsAktif(cbPeriode.isSelected());
                LocalDate dMulai = dpMulai.getDate();
                LocalTime tMulai = tpMulai.getTime();
                LocalDate dSelesai = dpSelesai.getDate();
                LocalTime tSelesai = tpSelesai.getTime();
                ns.setTanggalMulai((dMulai == null || tMulai == null) ? null : LocalDateTime.of(dMulai, tMulai));
                ns.setTanggalSelesai((dSelesai == null || tSelesai == null) ? null : LocalDateTime.of(dSelesai, tSelesai));
                new SettingsDAO().save(ns);
                updateStatusLabel(lblStatus, new SettingsDAO());
                JOptionPane.showMessageDialog(this, "Pengaturan berhasil disimpan!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal Simpan", JOptionPane.ERROR_MESSAGE);
            }
        });
        gc.gridx = 1; gc.gridy = r;
        gc.anchor = GridBagConstraints.EAST;
        card.add(btnSimpan, gc);

        wrapper.add(card, BorderLayout.NORTH);
        return wrapper;
    }

    private void updateStatusLabel(JLabel lbl, SettingsDAO dao) {
        try {
            boolean aktif = dao.isWithinWindow();
            if (aktif) {
                lbl.setText("✓ AKTIF");
                lbl.setForeground(new Color(0, 150, 0));
            } else {
                lbl.setText("✗ TIDAK AKTIF");
                lbl.setForeground(new Color(200, 0, 0));
            }
        } catch (Exception e) {
            lbl.setText("?");
            lbl.setForeground(Color.GRAY);
        }
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UITheme.uiFont(Font.PLAIN, 14));
        l.setForeground(UITheme.TEXT);
        return l;
    }
}
