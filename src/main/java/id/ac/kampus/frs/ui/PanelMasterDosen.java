package id.ac.kampus.frs.ui;

import id.ac.kampus.frs.dao.DosenDAO;
import id.ac.kampus.frs.dao.UserDAO;
import id.ac.kampus.frs.model.User;
import id.ac.kampus.frs.model.Dosen;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PanelMasterDosen extends JPanel {
    private final DosenDAO dao = new DosenDAO();
    private JTable table; private Model model;

    public PanelMasterDosen() {
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
        JTextField tfNidn = new JTextField();
        JTextField tfNama = new JTextField();
        JComboBox<Item> cbUser = new JComboBox<>(loadUsersByRole(User.Role.DOSEN));
        Object[] msg = {"NIDN", tfNidn, "Nama", tfNama, "User (DOSEN)", cbUser};
        if (JOptionPane.showConfirmDialog(this, msg, "Tambah Dosen", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                Dosen d = new Dosen();
                d.setNidn(tfNidn.getText().trim());
                d.setNama(tfNama.getText().trim());
                Item userItem = (Item) cbUser.getSelectedItem();
                d.setIdUser(userItem==null?0:userItem.id);
                dao.insert(d);
                reload();
            } catch (java.sql.SQLIntegrityConstraintViolationException dup) {
                JOptionPane.showMessageDialog(this, "User ini sudah dipakai oleh entitas lain atau NIDN sudah ada.", "Data duplikat", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal tambah", JOptionPane.ERROR_MESSAGE); }
        }
    }

    private void onEdit() {
        int row = table.getSelectedRow(); if (row < 0) return;
        Dosen cur = model.rows.get(row);
        JTextField tfNama = new JTextField(cur.getNama());
        JComboBox<Item> cbUser = new JComboBox<>(loadUsersByRole(User.Role.DOSEN));
        selectComboById(cbUser, cur.getIdUser());
        Object[] msg = {"Nama", tfNama, "User (DOSEN)", cbUser};
        if (JOptionPane.showConfirmDialog(this, msg, "Edit Dosen " + cur.getNidn(), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                cur.setNama(tfNama.getText().trim());
                Item userItem = (Item) cbUser.getSelectedItem();
                cur.setIdUser(userItem==null?0:userItem.id);
                dao.update(cur);
                reload();
            } catch (java.sql.SQLIntegrityConstraintViolationException dup) {
                JOptionPane.showMessageDialog(this, "User ini sudah dipakai oleh entitas lain.", "Data duplikat", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal edit", JOptionPane.ERROR_MESSAGE); }
        }
    }

    private Item[] loadUsersByRole(User.Role role) {
        try {
            var cb = new java.util.ArrayList<Item>();
            var dao = new UserDAO();
            for (var u : dao.listByRole(role)) cb.add(new Item(u.getIdUser(), u.getUsername()));
            return cb.toArray(new Item[0]);
        } catch (Exception e) { return new Item[0]; }
    }

    private void selectComboById(JComboBox<Item> combo, int id) {
        for (int i=0;i<combo.getItemCount();i++) if (combo.getItemAt(i).id == id) { combo.setSelectedIndex(i); break; }
    }

    private static class Item {
        int id; String label;
        Item(int id, String label) { this.id=id; this.label=label; }
        @Override public String toString() { return label; }
    }

    private void onDelete() {
        int row = table.getSelectedRow(); if (row < 0) return;
        Dosen cur = model.rows.get(row);
        if (JOptionPane.showConfirmDialog(this, "Hapus dosen " + cur.getNidn() + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try { dao.delete(cur.getNidn()); reload(); }
            catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal hapus", JOptionPane.ERROR_MESSAGE); }
        }
    }

    private static class Model extends AbstractTableModel {
        private final String[] cols = {"NIDN","Nama","ID User"};
        private List<Dosen> rows = new ArrayList<>();
        public void setData(List<Dosen> list) { this.rows = list; fireTableDataChanged(); }
        @Override public int getRowCount() { return rows.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int column) { return cols[column]; }
        @Override public Object getValueAt(int rowIndex, int columnIndex) {
            Dosen d = rows.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> d.getNidn();
                case 1 -> d.getNama();
                case 2 -> d.getIdUser();
                default -> null;
            };
        }
    }
}
