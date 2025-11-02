package id.ac.kampus.frs.ui;

import id.ac.kampus.frs.dao.ReportDAO;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PanelLaporanPersetujuan extends JPanel {
    private JTable table;
    private Model model;

    public PanelLaporanPersetujuan() {
        setLayout(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRefresh = new JButton("Refresh");
        top.add(new JLabel("Laporan Persetujuan FRS"));
        top.add(Box.createHorizontalStrut(16));
        top.add(btnRefresh);
        add(top, BorderLayout.NORTH);

        model = new Model();
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnRefresh.addActionListener(e -> loadData());
        loadData();
    }

    private void loadData() {
        try {
            var list = new ReportDAO().listApprovals(200);
            model.setRows(list);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal memuat laporan", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class Model extends AbstractTableModel {
        private final String[] cols = {"Waktu","Status","Catatan","ID FRS","Semester","NIM","Nama Mhs","NIDN","Nama Dosen"};
        private List<ReportDAO.ApprovalRow> rows = new ArrayList<>();
        public void setRows(List<ReportDAO.ApprovalRow> list) { this.rows = list; fireTableDataChanged(); }
        @Override public int getRowCount() { return rows.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int column) { return cols[column]; }
        @Override public Object getValueAt(int rowIndex, int columnIndex) {
            var r = rows.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> r.waktu;
                case 1 -> r.status;
                case 2 -> r.catatan;
                case 3 -> r.idFrs;
                case 4 -> r.semester;
                case 5 -> r.nim;
                case 6 -> r.namaMhs;
                case 7 -> r.nidn;
                case 8 -> r.namaDosen;
                default -> null;
            };
        }
    }
}
