package id.ac.kampus.frs.ui;

import id.ac.kampus.frs.model.FRS;
import id.ac.kampus.frs.model.Mahasiswa;
import id.ac.kampus.frs.model.MataKuliah;
import id.ac.kampus.frs.model.User;
import id.ac.kampus.frs.service.FRSService;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MahasiswaDashboardFrame extends JFrame {
    private final User user;
    private final Mahasiswa mhs;
    private final FRSService frsService = new FRSService();

    private JTable tblMk;
    private MkTableModel mkModel;
    private JLabel lblTotalSks;

    private FRS currentFrs;

    public MahasiswaDashboardFrame(User user, Mahasiswa mhs) {
        this.user = user;
        this.mhs = mhs;
        setTitle("Dashboard Mahasiswa - " + mhs.getNama());
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        buildUI();
        loadData();
    }

    private void buildUI() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Profil", buildProfilPanel());
        tabs.addTab("Isi FRS", buildFrsPanel());
        add(tabs);
    }

    private JPanel buildProfilPanel() {
        JPanel p = new JPanel(new GridLayout(0,2,8,8));
        p.setBorder(BorderFactory.createEmptyBorder(16,16,16,16));
        p.add(new JLabel("NIM")); p.add(new JLabel(mhs.getNim()));
        p.add(new JLabel("Nama")); p.add(new JLabel(mhs.getNama()));
        p.add(new JLabel("Jurusan")); p.add(new JLabel(mhs.getJurusan()));
        p.add(new JLabel("Semester aktif")); p.add(new JLabel(String.valueOf(mhs.getSemester())));
        return p;
    }

    private JPanel buildFrsPanel() {
        JPanel root = new JPanel(new BorderLayout());
        mkModel = new MkTableModel();
        tblMk = new JTable(mkModel);
        tblMk.setFillsViewportHeight(true);
        tblMk.getColumnModel().getColumn(0).setPreferredWidth(30);
        root.add(new JScrollPane(tblMk), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        lblTotalSks = new JLabel("Total SKS: 0");
        bottom.add(lblTotalSks, BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSimpan = new JButton("Simpan Draft");
        btnSimpan.addActionListener(e -> onSimpan());
        JButton btnAjukan = new JButton("Ajukan FRS");
        btnAjukan.addActionListener(e -> onAjukan());
        actions.add(btnSimpan);
        actions.add(btnAjukan);
        bottom.add(actions, BorderLayout.EAST);

        root.add(bottom, BorderLayout.SOUTH);
        return root;
    }

    private void loadData() {
        try {
            currentFrs = frsService.getOrCreateDraft(mhs.getNim(), mhs.getSemester());
            List<MataKuliah> mks = frsService.listMkSemester(mhs.getSemester());
            mkModel.setData(mks);
            recalcTotal();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal memuat data", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onSimpan() {
        try {
            frsService.saveDraft(currentFrs, mkModel.getSelectedKode(), user.getIdUser());
            JOptionPane.showMessageDialog(this, "Draft FRS disimpan.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal menyimpan", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onAjukan() {
        try {
            frsService.submit(currentFrs, mkModel.getSelectedKode(), user.getIdUser());
            JOptionPane.showMessageDialog(this, "FRS diajukan ke Dosen PA.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal mengajukan", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void recalcTotal() {
        int total = mkModel.getSelectedTotalSks();
        lblTotalSks.setText("Total SKS: " + total);
    }

    private class MkTableModel extends AbstractTableModel {
        private final String[] cols = {"Pilih", "Kode", "Nama Mata Kuliah", "SKS"};
        private final Class<?>[] types = {Boolean.class, String.class, String.class, Integer.class};
        private List<Row> rows = new ArrayList<>();

        public void setData(List<MataKuliah> list) {
            rows.clear();
            for (MataKuliah mk : list) {
                rows.add(new Row(false, mk));
            }
            fireTableDataChanged();
        }

        public List<String> getSelectedKode() {
            List<String> kode = new ArrayList<>();
            for (Row r : rows) if (r.selected) kode.add(r.mk.getKodeMk());
            return kode;
        }

        public int getSelectedTotalSks() {
            int t = 0;
            for (Row r : rows) if (r.selected) t += r.mk.getSks();
            return t;
        }

        @Override public int getRowCount() { return rows.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int column) { return cols[column]; }
        @Override public Class<?> getColumnClass(int columnIndex) { return types[columnIndex]; }
        @Override public boolean isCellEditable(int rowIndex, int columnIndex) { return columnIndex == 0; }
        @Override public Object getValueAt(int rowIndex, int columnIndex) {
            Row r = rows.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> r.selected;
                case 1 -> r.mk.getKodeMk();
                case 2 -> r.mk.getNamaMk();
                case 3 -> r.mk.getSks();
                default -> null;
            };
        }
        @Override public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                rows.get(rowIndex).selected = (Boolean) aValue;
                recalcTotal();
            }
        }

        private class Row {
            boolean selected; MataKuliah mk;
            Row(boolean s, MataKuliah m) { selected = s; mk = m; }
        }
    }
}
