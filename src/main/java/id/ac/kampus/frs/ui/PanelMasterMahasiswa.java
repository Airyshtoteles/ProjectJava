package id.ac.kampus.frs.ui;

import id.ac.kampus.frs.dao.MahasiswaDAO;
import id.ac.kampus.frs.dao.DosenDAO;
import id.ac.kampus.frs.dao.UserDAO;
import id.ac.kampus.frs.model.User;
import id.ac.kampus.frs.model.Mahasiswa;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PanelMasterMahasiswa extends JPanel {
    private final MahasiswaDAO dao = new MahasiswaDAO();
    private JTable table; private Model model;

    public PanelMasterMahasiswa() {
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
        JTextField tfNim = new JTextField();
        JTextField tfNama = new JTextField();
        JTextField tfJur = new JTextField();
        JSpinner spSem = new JSpinner(new SpinnerNumberModel(1,1,14,1));
        JComboBox<Item> cbUser = new JComboBox<>(loadUsersByRole(User.Role.MAHASISWA));
        JCheckBox cbAutoUser = new JCheckBox("Buat akun otomatis dari NIM (password: password)");
        JComboBox<Item> cbWali = new JComboBox<>(loadDosenItems());
        Object[] msg = {"NIM", tfNim, "Nama", tfNama, "Jurusan", tfJur, "Semester", spSem, "User (MAHASISWA)", cbUser, cbAutoUser, "Dosen Wali", cbWali};
        if (JOptionPane.showConfirmDialog(this, msg, "Tambah Mahasiswa", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                Mahasiswa m = new Mahasiswa();
                m.setNim(tfNim.getText().trim());
                m.setNama(tfNama.getText().trim());
                m.setJurusan(tfJur.getText().trim());
                m.setSemester((Integer) spSem.getValue());
                Item userItem = (Item) cbUser.getSelectedItem();
                if (cbAutoUser.isSelected()) {
                    // create user with username=NIM
                    var userDao = new UserDAO();
                    String username = m.getNim();
                    String hash = id.ac.kampus.frs.util.PasswordUtil.sha256("password");
                    int idUser = userDao.insert(username, hash, User.Role.MAHASISWA);
                    m.setIdUser(idUser);
                } else {
                    m.setIdUser(userItem==null?0:userItem.id);
                }
                Item waliItem = (Item) cbWali.getSelectedItem();
                dao.insert(m, waliItem==null?null:waliItem.value);
                reload();
            } catch (java.sql.SQLIntegrityConstraintViolationException dup) {
                JOptionPane.showMessageDialog(this, "User ini sudah dipakai oleh entitas lain atau NIM sudah ada.", "Data duplikat", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal tambah", JOptionPane.ERROR_MESSAGE); }
        }
    }

    private void onEdit() {
        int row = table.getSelectedRow(); if (row < 0) return;
        Mahasiswa cur = model.rows.get(row);
        JTextField tfNama = new JTextField(cur.getNama());
        JTextField tfJur = new JTextField(cur.getJurusan());
        JSpinner spSem = new JSpinner(new SpinnerNumberModel(cur.getSemester(),1,14,1));
        JComboBox<Item> cbUser = new JComboBox<>(loadUsersByRole(User.Role.MAHASISWA));
        selectComboById(cbUser, cur.getIdUser());
        JComboBox<Item> cbWali = new JComboBox<>(loadDosenItems());
        Object[] msg = {"Nama", tfNama, "Jurusan", tfJur, "Semester", spSem, "User (MAHASISWA)", cbUser, "Dosen Wali", cbWali};
        if (JOptionPane.showConfirmDialog(this, msg, "Edit Mahasiswa " + cur.getNim(), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                cur.setNama(tfNama.getText().trim());
                cur.setJurusan(tfJur.getText().trim());
                cur.setSemester((Integer) spSem.getValue());
                Item userItem = (Item) cbUser.getSelectedItem();
                cur.setIdUser(userItem==null?0:userItem.id);
                Item waliItem = (Item) cbWali.getSelectedItem();
                dao.update(cur, waliItem==null?null:waliItem.value);
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
            var list = dao.listByRole(role);
            for (var u : list) cb.add(new Item(u.getIdUser(), u.getUsername()));
            return cb.toArray(new Item[0]);
        } catch (Exception e) { return new Item[0]; }
    }

    private Item[] loadDosenItems() {
        try {
            var cb = new java.util.ArrayList<Item>();
            var dao = new DosenDAO();
            for (var d : dao.listAll()) cb.add(new Item(d.getNidn(), d.getNama() + " (" + d.getNidn() + ")"));
            return cb.toArray(new Item[0]);
        } catch (Exception e) { return new Item[0]; }
    }

    private void selectComboById(JComboBox<Item> combo, int id) {
        for (int i=0;i<combo.getItemCount();i++) if (combo.getItemAt(i).id == id) { combo.setSelectedIndex(i); break; }
    }

    private static class Item {
        int id; String value; String label;
        Item(int id, String label) { this.id=id; this.label=label; this.value=String.valueOf(id); }
        Item(String value, String label) { this.value=value; this.label=label; }
        @Override public String toString() { return label; }
    }

    private void onDelete() {
        int row = table.getSelectedRow(); if (row < 0) return;
        Mahasiswa cur = model.rows.get(row);
        if (JOptionPane.showConfirmDialog(this, "Hapus mahasiswa " + cur.getNim() + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try { dao.delete(cur.getNim()); reload(); }
            catch (SQLException ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal hapus", JOptionPane.ERROR_MESSAGE); }
        }
    }

    private static class Model extends AbstractTableModel {
        private final String[] cols = {"NIM","Nama","Jurusan","Semester","ID User"};
        private List<Mahasiswa> rows = new ArrayList<>();
        public void setData(List<Mahasiswa> list) { this.rows = list; fireTableDataChanged(); }
        @Override public int getRowCount() { return rows.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int column) { return cols[column]; }
        @Override public Object getValueAt(int rowIndex, int columnIndex) {
            Mahasiswa m = rows.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> m.getNim();
                case 1 -> m.getNama();
                case 2 -> m.getJurusan();
                case 3 -> m.getSemester();
                case 4 -> m.getIdUser();
                default -> null;
            };
        }
    }
}
