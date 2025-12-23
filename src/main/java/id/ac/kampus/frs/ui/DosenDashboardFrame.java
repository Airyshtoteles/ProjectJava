package id.ac.kampus.frs.ui;

import id.ac.kampus.frs.model.User;
import id.ac.kampus.frs.service.DosenService;
import id.ac.kampus.frs.dao.FRSDAO;
import id.ac.kampus.frs.model.MataKuliah;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DosenDashboardFrame extends JFrame {
    private final User user;
    private final DosenService service = new DosenService();

    private JTable tblPengajuan;
    private PengajuanTableModel pengajuanModel;
    private JTable tblDetail;
    private DetailTableModel detailModel;

    // For demo we infer nidn from username when role= DOSEN; a real app should resolve via dosen table by id_user.
    private String nidn; 

    public DosenDashboardFrame(User user) {
        this.user = user;
        this.nidn = inferNidnFromUsername(user.getUsername());
        setTitle("Dashboard Dosen - " + user.getUsername());
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setJMenuBar(buildMenuBar());
        buildUI();
        loadPengajuan();
    }

    private String inferNidnFromUsername(String username) {
        // Seed maps dosen1 -> NIDN D001; adjust as needed or query DosenDAO by id_user.
        if ("dosen1".equalsIgnoreCase(username)) return "D001";
        return username; // fallback
    }

    private void buildUI() {
    setLayout(new BorderLayout());
    add(UiUtil.header("Dashboard Dosen", "NIDN: " + nidn + " • " + user.getUsername()), BorderLayout.NORTH);
    JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setResizeWeight(0.6);

    JPanel left = new JPanel(new BorderLayout());
    JPanel filter = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JTextField tfCari = new JTextField(20);
    filter.add(new JLabel("Cari (NIM/Nama):"));
    filter.add(tfCari);
    JButton btnCari = new JButton("Filter");
    filter.add(btnCari);
    JButton btnExport = new JButton("Export Excel");
    JButton btnPrint = new JButton("Print");
    filter.add(btnExport);
    filter.add(btnPrint);

    pengajuanModel = new PengajuanTableModel();
    tblPengajuan = new JTable(pengajuanModel);
    left.add(filter, BorderLayout.NORTH);
    left.add(new JScrollPane(tblPengajuan), BorderLayout.CENTER);
    split.setLeftComponent(left);
        tblPengajuan.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPengajuan.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) onPengajuanSelected();
        });
    btnCari.addActionListener(e -> pengajuanModel.applyFilter(tfCari.getText().trim()));
    btnExport.addActionListener(e -> onExportExcel());
    btnPrint.addActionListener(e -> onPrint());

        detailModel = new DetailTableModel();
        tblDetail = new JTable(detailModel);
        JPanel right = new JPanel(new BorderLayout());
        right.add(new JScrollPane(tblDetail), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnApprove = new JButton("Setujui");
        JButton btnReject = new JButton("Tolak");
        btnApprove.addActionListener(e -> onApprove());
        btnReject.addActionListener(e -> onReject());
        actions.add(btnApprove);
        actions.add(btnReject);
        right.add(actions, BorderLayout.SOUTH);

        split.setRightComponent(right);
        add(split, BorderLayout.CENTER);
    }

    private JMenuBar buildMenuBar() {
        JMenuBar mb = new JMenuBar();
        JMenu mAkun = new JMenu("Akun");
        JMenuItem miUbah = new JMenuItem("Ajukan Ubah Password...");
        miUbah.addActionListener(e -> new ChangePasswordDialog(this, user.getIdUser()).setVisible(true));
        mAkun.add(miUbah);
        mb.add(mAkun);
        return mb;
    }

    private void loadPengajuan() {
        try {
            var list = service.listPengajuan(nidn);
            pengajuanModel.setData(list);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal memuat pengajuan", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onExportExcel() {
        javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
        fc.setSelectedFile(new java.io.File("frs-bimbingan.xlsx"));
        if (fc.showSaveDialog(this) == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File f = fc.getSelectedFile();
            try (org.apache.poi.ss.usermodel.Workbook wb = new org.apache.poi.xssf.usermodel.XSSFWorkbook()) {
                var sh = wb.createSheet("Pengajuan");
                String[] headers = {"ID FRS","NIM","Nama","Semester","Total SKS","Status"};
                var hr = sh.createRow(0);
                for (int i=0;i<headers.length;i++) hr.createCell(i).setCellValue(headers[i]);
                int r = 1;
                for (var row : pengajuanModel.getRows()) {
                    var rr = sh.createRow(r++);
                    rr.createCell(0).setCellValue(row.idFrs);
                    rr.createCell(1).setCellValue(row.nim);
                    rr.createCell(2).setCellValue(row.nama);
                    rr.createCell(3).setCellValue(row.semester);
                    rr.createCell(4).setCellValue(row.totalSks);
                    rr.createCell(5).setCellValue(row.status);
                }
                try (var out = new java.io.FileOutputStream(f)) { wb.write(out); }
                javax.swing.JOptionPane.showMessageDialog(this, "Diekspor: " + f.getAbsolutePath());
            } catch (Exception ex) {
                javax.swing.JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal ekspor", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onPrint() {
        try {
            boolean complete = tblPengajuan.print(JTable.PrintMode.FIT_WIDTH, new java.text.MessageFormat("Daftar Pengajuan FRS Bimbingan"), null);
            if (!complete) javax.swing.JOptionPane.showMessageDialog(this, "Pencetakan dibatalkan.");
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal mencetak", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onPengajuanSelected() {
        int row = tblPengajuan.getSelectedRow();
        if (row < 0) { detailModel.setData(new ArrayList<>()); return; }
        int idFrs = pengajuanModel.getIdFrs(row);
        try {
            List<MataKuliah> mks = service.listDetailMk(idFrs);
            detailModel.setData(mks);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal memuat detail", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onApprove() {
        int row = tblPengajuan.getSelectedRow();
        if (row < 0) return;
        int idFrs = pengajuanModel.getIdFrs(row);
        String catatan = JOptionPane.showInputDialog(this, "Catatan (opsional):");
        try {
            service.approve(idFrs, nidn, user.getIdUser(), catatan);
            loadPengajuan();
            detailModel.setData(new ArrayList<>());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal menyetujui", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onReject() {
        int row = tblPengajuan.getSelectedRow();
        if (row < 0) return;
        int idFrs = pengajuanModel.getIdFrs(row);
        String catatan = JOptionPane.showInputDialog(this, "Alasan penolakan:");
        if (catatan == null || catatan.isBlank()) {
            JOptionPane.showMessageDialog(this, "Catatan wajib untuk penolakan.");
            return;
        }
        try {
            service.reject(idFrs, nidn, user.getIdUser(), catatan);
            loadPengajuan();
            detailModel.setData(new ArrayList<>());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal menolak", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class PengajuanTableModel extends AbstractTableModel {
        private final String[] cols = {"ID FRS", "NIM", "Nama", "Semester", "Total SKS", "Status"};
        private List<FRSDAO.PengajuanRow> rows = new ArrayList<>();
        private List<FRSDAO.PengajuanRow> all = new ArrayList<>();

    public void setData(List<FRSDAO.PengajuanRow> data) { this.all = new ArrayList<>(data); this.rows = data; fireTableDataChanged(); }
    public List<FRSDAO.PengajuanRow> getRows() { return rows; }
        public int getIdFrs(int row) { return rows.get(row).idFrs; }
        public void applyFilter(String q) {
            q = q.toLowerCase();
            if (q.isBlank()) { rows = new ArrayList<>(all); fireTableDataChanged(); return; }
            var filtered = new ArrayList<FRSDAO.PengajuanRow>();
            for (var r : all) if (r.nim.toLowerCase().contains(q) || r.nama.toLowerCase().contains(q)) filtered.add(r);
            rows = filtered; fireTableDataChanged();
        }
        @Override public int getRowCount() { return rows.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int column) { return cols[column]; }
        @Override public Object getValueAt(int rowIndex, int columnIndex) {
            var r = rows.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> r.idFrs;
                case 1 -> r.nim;
                case 2 -> r.nama;
                case 3 -> r.semester;
                case 4 -> r.totalSks;
                case 5 -> r.status;
                default -> null;
            };
        }
    }

    private static class DetailTableModel extends AbstractTableModel {
        private final String[] cols = {"Kode", "Nama MK", "SKS"};
        private List<MataKuliah> rows = new ArrayList<>();
        public void setData(List<MataKuliah> list) { this.rows = list; fireTableDataChanged(); }
        @Override public int getRowCount() { return rows.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int column) { return cols[column]; }
        @Override public Object getValueAt(int rowIndex, int columnIndex) {
            var mk = rows.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> mk.getKodeMk();
                case 1 -> mk.getNamaMk();
                case 2 -> mk.getSks();
                default -> null;
            };
        }
    }
}
