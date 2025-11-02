package id.ac.kampus.frs.ui;

import id.ac.kampus.frs.dao.MataKuliahDAO;
import id.ac.kampus.frs.model.MataKuliah;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PanelMasterMataKuliah extends JPanel {
    private final MataKuliahDAO dao = new MataKuliahDAO();
    private JTable table; private Model model;

    public PanelMasterMataKuliah() {
        setLayout(new BorderLayout());
        model = new Model();
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton bAdd = new JButton("Tambah"); bAdd.addActionListener(e -> onAdd());
        JButton bEdit = new JButton("Edit"); bEdit.addActionListener(e -> onEdit());
        JButton bDel = new JButton("Hapus"); bDel.addActionListener(e -> onDelete());
        actions.add(bAdd); actions.add(bEdit); actions.add(bDel);
        add(actions, BorderLayout.SOUTH);

        reload();
    }

    private void reload() {
        try { model.setData(dao.listAll()); }
        catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal muat", JOptionPane.ERROR_MESSAGE); }
    }

    private void onAdd() {
        JTextField tfKode = new JTextField();
        JTextField tfNama = new JTextField();
        JSpinner spSks = new JSpinner(new SpinnerNumberModel(3,1,6,1));
        JSpinner spSem = new JSpinner(new SpinnerNumberModel(1,1,14,1));
        JTextField tfPra = new JTextField();
        Object[] msg = {"Kode", tfKode, "Nama", tfNama, "SKS", spSks, "Semester", spSem, "Prasyarat (kode)", tfPra};
        if (JOptionPane.showConfirmDialog(this, msg, "Tambah Mata Kuliah", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                MataKuliah mk = new MataKuliah();
                mk.setKodeMk(tfKode.getText().trim());
                mk.setNamaMk(tfNama.getText().trim());
                mk.setSks((Integer) spSks.getValue());
                mk.setSemester((Integer) spSem.getValue());
                mk.setPrasyarat(tfPra.getText().trim().isEmpty()?null:tfPra.getText().trim());
                dao.insert(mk);
                reload();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal tambah", JOptionPane.ERROR_MESSAGE); }
        }
    }

    private void onEdit() {
        int row = table.getSelectedRow(); if (row < 0) return;
        MataKuliah cur = model.rows.get(row);
        JTextField tfNama = new JTextField(cur.getNamaMk());
        JSpinner spSks = new JSpinner(new SpinnerNumberModel(cur.getSks(),1,6,1));
        JSpinner spSem = new JSpinner(new SpinnerNumberModel(cur.getSemester(),1,14,1));
        JTextField tfPra = new JTextField(cur.getPrasyarat()==null?"":cur.getPrasyarat());
        Object[] msg = {"Nama", tfNama, "SKS", spSks, "Semester", spSem, "Prasyarat (kode)", tfPra};
        if (JOptionPane.showConfirmDialog(this, msg, "Edit MK " + cur.getKodeMk(), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                cur.setNamaMk(tfNama.getText().trim());
                cur.setSks((Integer) spSks.getValue());
                cur.setSemester((Integer) spSem.getValue());
                cur.setPrasyarat(tfPra.getText().trim().isEmpty()?null:tfPra.getText().trim());
                dao.update(cur);
                reload();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal edit", JOptionPane.ERROR_MESSAGE); }
        }
    }

    private void onDelete() {
        int row = table.getSelectedRow(); if (row < 0) return;
        MataKuliah cur = model.rows.get(row);
        if (JOptionPane.showConfirmDialog(this, "Hapus MK " + cur.getKodeMk() + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try { dao.delete(cur.getKodeMk()); reload(); }
            catch (SQLException ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal hapus", JOptionPane.ERROR_MESSAGE); }
        }
    }

    private static class Model extends AbstractTableModel {
        private final String[] cols = {"Kode","Nama","SKS","Semester","Prasyarat"};
        private List<MataKuliah> rows = new ArrayList<>();
        public void setData(List<MataKuliah> list) { this.rows = list; fireTableDataChanged(); }
        @Override public int getRowCount() { return rows.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int column) { return cols[column]; }
        @Override public Object getValueAt(int rowIndex, int columnIndex) {
            MataKuliah mk = rows.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> mk.getKodeMk();
                case 1 -> mk.getNamaMk();
                case 2 -> mk.getSks();
                case 3 -> mk.getSemester();
                case 4 -> mk.getPrasyarat();
                default -> null;
            };
        }
    }
}
