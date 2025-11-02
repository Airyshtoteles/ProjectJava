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
        setLayout(new BorderLayout());
        add(UiUtil.header("Dashboard Admin", "Masuk sebagai: " + user.getUsername()), BorderLayout.NORTH);
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Pengaturan", buildPengaturanPanel());
        tabs.addTab("Mahasiswa", new PanelMasterMahasiswa());
        tabs.addTab("Dosen", new PanelMasterDosen());
        tabs.addTab("Mata Kuliah", new PanelMasterMataKuliah());
        tabs.addTab("Laporan", new PanelLaporanPersetujuan());
        tabs.addTab("Permintaan Password", new PanelPermintaanPassword(user.getIdUser()));
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel buildPengaturanPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8,8,8,8);
        gc.anchor = GridBagConstraints.WEST;

    SettingsDAO dao = new SettingsDAO();
    Settings s;
    try { s = dao.get(); } catch (Exception ex) { s = new Settings(); s.setSemesterAktif(1); s.setFrsAktif(true); }

        int r=0;
        p.add(new JLabel("Semester Aktif:"), pos(gc,0,r));
        JSpinner spSemester = new JSpinner(new SpinnerNumberModel(s.getSemesterAktif(),1,14,1));
        p.add(spSemester, pos(gc,1,r++));

        JCheckBox cbPeriode = new JCheckBox("Aktifkan periode FRS");
        cbPeriode.setSelected(s.isFrsAktif());
        p.add(cbPeriode, pos(gc,1,r++));

        p.add(new JLabel("Tanggal Mulai (opsional):"), pos(gc,0,r));
    JPanel mulaiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
    DatePicker dpMulai = new DatePicker();
    TimePicker tpMulai = new TimePicker();
        JButton btnClrMulai = new JButton("Kosongkan");
        if (s.getTanggalMulai()!=null) {
            dpMulai.setDate(s.getTanggalMulai().toLocalDate());
            tpMulai.setTime(s.getTanggalMulai().toLocalTime());
        }
        btnClrMulai.addActionListener(e -> { dpMulai.setDate(null); tpMulai.setTime(null); });
        mulaiPanel.add(dpMulai); mulaiPanel.add(tpMulai); mulaiPanel.add(btnClrMulai);
        p.add(mulaiPanel, pos(gc,1,r++));

        p.add(new JLabel("Tanggal Selesai (opsional):"), pos(gc,0,r));
    JPanel selesaiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
    DatePicker dpSelesai = new DatePicker();
    TimePicker tpSelesai = new TimePicker();
        JButton btnClrSelesai = new JButton("Kosongkan");
        if (s.getTanggalSelesai()!=null) {
            dpSelesai.setDate(s.getTanggalSelesai().toLocalDate());
            tpSelesai.setTime(s.getTanggalSelesai().toLocalTime());
        }
        btnClrSelesai.addActionListener(e -> { dpSelesai.setDate(null); tpSelesai.setTime(null); });
        selesaiPanel.add(dpSelesai); selesaiPanel.add(tpSelesai); selesaiPanel.add(btnClrSelesai);
        p.add(selesaiPanel, pos(gc,1,r++));

        JButton btnSimpan = new JButton("Simpan Pengaturan");
        btnSimpan.addActionListener(e -> {
            try {
                Settings ns = new Settings();
                ns.setSemesterAktif((Integer) spSemester.getValue());
                ns.setFrsAktif(cbPeriode.isSelected());
                LocalDate dMulai = dpMulai.getDate();
                LocalTime tMulai = tpMulai.getTime();
                LocalDate dSelesai = dpSelesai.getDate();
                LocalTime tSelesai = tpSelesai.getTime();
                ns.setTanggalMulai((dMulai==null || tMulai==null)? null : LocalDateTime.of(dMulai, tMulai));
                ns.setTanggalSelesai((dSelesai==null || tSelesai==null)? null : LocalDateTime.of(dSelesai, tSelesai));
                new SettingsDAO().save(ns);
                JOptionPane.showMessageDialog(this, "Pengaturan disimpan.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal simpan", JOptionPane.ERROR_MESSAGE);
            }
        });
        p.add(btnSimpan, pos(gc,1,r));

        return p;
    }

    private GridBagConstraints pos(GridBagConstraints base, int x, int y) {
        GridBagConstraints gc = (GridBagConstraints) base.clone();
        gc.gridx = x; gc.gridy = y;
        return gc;
    }
}
