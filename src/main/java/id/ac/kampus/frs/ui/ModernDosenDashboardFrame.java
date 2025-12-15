package id.ac.kampus.frs.ui;

import id.ac.kampus.frs.model.User;

import javax.swing.*;
import java.awt.*;

/** Dashboard Dosen bertema modern (placeholder navigasi dasar). */
public class ModernDosenDashboardFrame extends BaseDashboardFrame {
    private final String nidn;
    private JTable tblPengajuan, tblDetail; private PengajuanModel pengajuanModel; private DetailModel detailModel;
    public ModernDosenDashboardFrame(User user, String nidn) {
        super("Dashboard Dosen");
        this.nidn = nidn;
        setUserInfo("NIDN: " + nidn + " • " + user.getUsername());
        registerPage("Pengajuan", "Pengajuan", buildPengajuanPage());
        showPage("Pengajuan");
        loadPengajuan();
    }

    private JComponent buildPengajuanPage(){
        JPanel root = new JPanel(new GridLayout(1,2,12,12)); root.setOpaque(false);
        UITheme.CardPanel left = new UITheme.CardPanel(20); left.setLayout(new BorderLayout());
        UITheme.CardPanel right = new UITheme.CardPanel(20); right.setLayout(new BorderLayout());

        pengajuanModel = new PengajuanModel(); tblPengajuan = new JTable(pengajuanModel);
        tblPengajuan.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPengajuan.getSelectionModel().addListSelectionListener(e->{ if(!e.getValueIsAdjusting()) onSelectPengajuan(); });
        left.add(new JScrollPane(tblPengajuan), BorderLayout.CENTER);

        detailModel = new DetailModel(); tblDetail = new JTable(detailModel);
        right.add(new JScrollPane(tblDetail), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT)); actions.setOpaque(false);
        UITheme.AnimatedButton approve = new UITheme.AnimatedButton("Setujui"); approve.setPreferredSize(new Dimension(120,34)); approve.addActionListener(e->onApprove());
        UITheme.AnimatedButton reject = new UITheme.AnimatedButton("Tolak"); reject.setPreferredSize(new Dimension(120,34)); reject.addActionListener(e->onReject());
        actions.add(approve); actions.add(reject);
        right.add(actions, BorderLayout.SOUTH);

        root.add(left); root.add(right);
        return root;
    }

    private void loadPengajuan(){
        try {
            var list = new id.ac.kampus.frs.service.DosenService().listPengajuan(nidn);
            pengajuanModel.setData(list);
        } catch (Exception ex){ javax.swing.JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal memuat", javax.swing.JOptionPane.ERROR_MESSAGE);}    }

    private void onSelectPengajuan(){
        int r = tblPengajuan.getSelectedRow(); if (r<0){ detailModel.setData(new java.util.ArrayList<>()); return; }
        int idFrs = pengajuanModel.getIdFrs(r);
        try {
            var mks = new id.ac.kampus.frs.service.DosenService().listDetailMk(idFrs);
            detailModel.setData(mks);
        } catch(Exception ex){ javax.swing.JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal detail", javax.swing.JOptionPane.ERROR_MESSAGE);} }

    private void onApprove(){
        int r = tblPengajuan.getSelectedRow(); if (r<0) return; int idFrs = pengajuanModel.getIdFrs(r);
        String cat = javax.swing.JOptionPane.showInputDialog(this, "Catatan (opsional):");
        try { new id.ac.kampus.frs.service.DosenService().approve(idFrs, nidn, 0, cat); loadPengajuan(); detailModel.setData(new java.util.ArrayList<>());} 
        catch(Exception ex){ javax.swing.JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal", javax.swing.JOptionPane.ERROR_MESSAGE);} }

    private void onReject(){
        int r = tblPengajuan.getSelectedRow(); if (r<0) return; int idFrs = pengajuanModel.getIdFrs(r);
        String cat = javax.swing.JOptionPane.showInputDialog(this, "Alasan penolakan:"); if (cat==null || cat.isBlank()){ javax.swing.JOptionPane.showMessageDialog(this, "Catatan wajib."); return; }
        try { new id.ac.kampus.frs.service.DosenService().reject(idFrs, nidn, 0, cat); loadPengajuan(); detailModel.setData(new java.util.ArrayList<>());} 
        catch(Exception ex){ javax.swing.JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal", javax.swing.JOptionPane.ERROR_MESSAGE);} }

    private static class PengajuanModel extends javax.swing.table.AbstractTableModel {
        private final String[] cols={"ID FRS","NIM","Nama","Semester","Total SKS","Status"};
        private java.util.List<id.ac.kampus.frs.dao.FRSDAO.PengajuanRow> rows=new java.util.ArrayList<>();
        void setData(java.util.List<id.ac.kampus.frs.dao.FRSDAO.PengajuanRow> l){ rows=l; fireTableDataChanged(); }
        int getIdFrs(int r){ return rows.get(r).idFrs; }
        @Override public int getRowCount(){ return rows.size(); }
        @Override public int getColumnCount(){ return cols.length; }
        @Override public String getColumnName(int c){ return cols[c]; }
        @Override public Object getValueAt(int r,int c){ var x=rows.get(r); return switch(c){ case 0->x.idFrs; case 1->x.nim; case 2->x.nama; case 3->x.semester; case 4->x.totalSks; case 5->x.status; default->null; }; }
    }

    private static class DetailModel extends javax.swing.table.AbstractTableModel {
        private final String[] cols={"Kode","Nama MK","SKS"};
        private java.util.List<id.ac.kampus.frs.model.MataKuliah> rows=new java.util.ArrayList<>();
        void setData(java.util.List<id.ac.kampus.frs.model.MataKuliah> l){ rows=l; fireTableDataChanged(); }
        @Override public int getRowCount(){ return rows.size(); }
        @Override public int getColumnCount(){ return cols.length; }
        @Override public String getColumnName(int c){ return cols[c]; }
        @Override public Object getValueAt(int r,int c){ var mk=rows.get(r); return switch(c){ case 0->mk.getKodeMk(); case 1->mk.getNamaMk(); case 2->mk.getSks(); default->null; }; }
    }
}
