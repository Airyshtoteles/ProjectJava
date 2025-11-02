package id.ac.kampus.frs.ui;

import id.ac.kampus.frs.dao.PasswordChangeRequestDAO;
import id.ac.kampus.frs.util.ActivityLogger;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PanelPermintaanPassword extends JPanel {
    private final int adminUserId;
    private JTable table;
    private Model model;

    public PanelPermintaanPassword(int adminUserId) {
        this.adminUserId = adminUserId;
        setLayout(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRefresh = new JButton("Refresh");
        JButton btnApprove = new JButton("Setujui");
        JButton btnReject = new JButton("Tolak");
        top.add(new JLabel("Permintaan Ubah Password (pending)"));
        top.add(Box.createHorizontalStrut(16));
        top.add(btnRefresh);
        top.add(btnApprove);
        top.add(btnReject);
        add(top, BorderLayout.NORTH);

        model = new Model();
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnRefresh.addActionListener(e -> loadData());
        btnApprove.addActionListener(e -> onApprove());
        btnReject.addActionListener(e -> onReject());
        loadData();
    }

    private void loadData() {
        try {
            var list = new PasswordChangeRequestDAO().listPending();
            model.setRows(list);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal memuat permintaan", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onApprove() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        int reqId = model.getId(row);
        try {
            new PasswordChangeRequestDAO().approve(reqId, adminUserId);
            ActivityLogger.log(adminUserId, "APPROVE_PWD_REQ:" + reqId);
            loadData();
            JOptionPane.showMessageDialog(this, "Permintaan disetujui dan password telah diperbarui.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal menyetujui", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onReject() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        int reqId = model.getId(row);
        String reason = JOptionPane.showInputDialog(this, "Alasan penolakan:");
        if (reason == null) return;
        try {
            new PasswordChangeRequestDAO().reject(reqId, adminUserId, reason);
            ActivityLogger.log(adminUserId, "REJECT_PWD_REQ:" + reqId);
            loadData();
            JOptionPane.showMessageDialog(this, "Permintaan ditolak.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal menolak", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class Model extends AbstractTableModel {
        private final String[] cols = {"ID","User ID","Username","Role","Requested At"};
        private List<PasswordChangeRequestDAO.Row> rows = new ArrayList<>();
        public void setRows(List<PasswordChangeRequestDAO.Row> list) { this.rows = list; fireTableDataChanged(); }
        public int getId(int row) { return rows.get(row).id; }
        @Override public int getRowCount() { return rows.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int column) { return cols[column]; }
        @Override public Object getValueAt(int rowIndex, int columnIndex) {
            var r = rows.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> r.id;
                case 1 -> r.userId;
                case 2 -> r.username;
                case 3 -> r.role;
                case 4 -> r.requestedAt;
                default -> null;
            };
        }
    }
}
