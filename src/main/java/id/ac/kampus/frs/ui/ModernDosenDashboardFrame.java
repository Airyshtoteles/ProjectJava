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
        
        UITheme.AnimatedButton btnNotif = new UITheme.AnimatedButton("Notifikasi");
        btnNotif.setPreferredSize(new Dimension(100, 32));
        btnNotif.addActionListener(e -> showNotifications());
        headerActions.add(btnNotif, 0);

        registerPage("Pengajuan", "Pengajuan", buildPengajuanPage());
        showPage("Pengajuan");
        loadPengajuan();
    }

    private void showNotifications() {
        JPopupMenu popup = new JPopupMenu();
        popup.add(new JMenuItem("Belum ada notifikasi baru."));
        popup.show(headerActions, 0, headerActions.getHeight());
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
        
        UITheme.AnimatedButton btnPrint = new UITheme.AnimatedButton("Print");
        btnPrint.setPreferredSize(new Dimension(80,34));
        btnPrint.addActionListener(e -> onPrint());
        actions.add(btnPrint);

        UITheme.AnimatedButton btnPdf = new UITheme.AnimatedButton("Cetak PDF");
        btnPdf.setPreferredSize(new Dimension(100,34));
        btnPdf.addActionListener(e -> onCetakPdf());
        actions.add(btnPdf);

        UITheme.AnimatedButton approve = new UITheme.AnimatedButton("Setujui"); approve.setPreferredSize(new Dimension(100,34)); approve.addActionListener(e->onApprove());
        UITheme.AnimatedButton reject = new UITheme.AnimatedButton("Tolak"); reject.setPreferredSize(new Dimension(100,34)); reject.addActionListener(e->onReject());
        actions.add(approve); actions.add(reject);
        right.add(actions, BorderLayout.SOUTH);

        root.add(left); root.add(right);
        return root;
    }

    private void onPrint() {
        try {
            if (tblDetail.getRowCount() > 0) tblDetail.print();
            else javax.swing.JOptionPane.showMessageDialog(this, "Tidak ada data untuk dicetak.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void onCetakPdf() {
        int r = tblPengajuan.getSelectedRow();
        if (r < 0) { javax.swing.JOptionPane.showMessageDialog(this, "Pilih mahasiswa terlebih dahulu."); return; }
        var row = pengajuanModel.getRow(r);
        
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new java.io.File("KSM_" + row.nim + ".pdf"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                id.ac.kampus.frs.util.PdfUtil.exportFrs(fc.getSelectedFile(), "Semester " + row.semester, row.nim, row.nama, row.semester, detailModel.rows, row.totalSks, row.status);
                javax.swing.JOptionPane.showMessageDialog(this, "PDF berhasil disimpan.");
            } catch (Exception ex) {
                javax.swing.JOptionPane.showMessageDialog(this, "Gagal menyimpan PDF: " + ex.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
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
        id.ac.kampus.frs.dao.FRSDAO.PengajuanRow getRow(int r){ return rows.get(r); }
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
