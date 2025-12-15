package id.ac.kampus.frs.ui;

import id.ac.kampus.frs.model.Mahasiswa;
import id.ac.kampus.frs.model.User;

import javax.swing.*;
import java.awt.*;

/**
 * Dashboard Mahasiswa bertema modern.
 * Menunjukkan contoh halaman "Profil" sebagai implementasi penuh.
 */
public class ModernMahasiswaDashboardFrame extends BaseDashboardFrame {
    private final User user; private final Mahasiswa mhs;
    // Service dan state untuk panel Isi FRS & Status
    private final id.ac.kampus.frs.service.FRSService frsService = new id.ac.kampus.frs.service.FRSService();
    private id.ac.kampus.frs.model.FRS currentFrs;
    public ModernMahasiswaDashboardFrame(User user, Mahasiswa mhs) {
        super("Dashboard Mahasiswa");
        this.user = user; this.mhs = mhs;
        setUserInfo(mhs.getNama() + " • NIM: " + mhs.getNim());

    // Daftarkan halaman
    registerPage("Profil", "Profil", buildProfilPage());
    registerPage("Isi FRS", "Isi FRS", buildIsiFrsPage());
    registerPage("Status", "Status", buildStatusPage());

        showPage("Profil");
    }

    private JComponent buildProfilPage() {
        JPanel container = new JPanel(new GridBagLayout());
        container.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(10,10,10,10); gc.fill = GridBagConstraints.BOTH; gc.weightx = 1; gc.weighty = 0;

        // Card info utama
        UITheme.CardPanel card = new UITheme.CardPanel(20);
        card.setLayout(new GridBagLayout());
        GridBagConstraints cc = new GridBagConstraints();
        cc.insets = new Insets(8,8,8,8); cc.anchor = GridBagConstraints.WEST;

        JLabel title = new JLabel("Profil Mahasiswa");
        title.setFont(UITheme.uiFont(Font.BOLD, 18)); title.setForeground(UITheme.TEXT);

        cc.gridx=0; cc.gridy=0; cc.gridwidth=2; card.add(title, cc);
        cc.gridwidth=1; cc.gridy++;
        card.add(label("NIM"), cc); cc.gridx=1; card.add(value(mhs.getNim()), cc);
        cc.gridx=0; cc.gridy++; card.add(label("Nama"), cc); cc.gridx=1; card.add(value(mhs.getNama()), cc);
        cc.gridx=0; cc.gridy++; card.add(label("Jurusan"), cc); cc.gridx=1; card.add(value(mhs.getJurusan()), cc);
        cc.gridx=0; cc.gridy++; card.add(label("Semester Aktif"), cc); cc.gridx=1; card.add(value(String.valueOf(mhs.getSemester())), cc);

        // Tombol aksi
        cc.gridx=0; cc.gridy++; cc.gridwidth=2; cc.anchor = GridBagConstraints.EAST;
        UITheme.AnimatedButton btn = new UITheme.AnimatedButton("Start Now \uD83D\uDE80");
        btn.setPreferredSize(new Dimension(180, 40));
        card.add(btn, cc);

        gc.gridx=0; gc.gridy=0; gc.weighty=0; container.add(card, gc);

        // Card kedua (placeholder untuk info tambahan)
        UITheme.CardPanel card2 = new UITheme.CardPanel(20);
        card2.setLayout(new BorderLayout());
        JLabel tips = new JLabel("Tips: Isi FRS minimal 12 SKS.");
        tips.setFont(UITheme.uiFont(Font.PLAIN, 14)); tips.setForeground(UITheme.TEXT);
        card2.add(tips, BorderLayout.NORTH);
        gc.gridy=1; gc.weighty=1; container.add(card2, gc);
        return container;
    }

    private JLabel label(String s){ JLabel l = new JLabel(s); l.setFont(UITheme.uiFont(Font.PLAIN, 14)); l.setForeground(new Color(0,0,0,170)); return l; }
    private JLabel value(String s){ JLabel l = new JLabel(s); l.setFont(UITheme.uiFont(Font.BOLD, 14)); l.setForeground(UITheme.TEXT); return l; }

    // (placeholder utility tidak dipakai lagi)

    // ===== Isi FRS =====
    private JTable tblMk; private MkModel mkModel; private JLabel lblTotal;
    private JComponent buildIsiFrsPage(){
        JPanel root = new JPanel(new BorderLayout()); root.setOpaque(false);
        UITheme.CardPanel card = new UITheme.CardPanel(20); card.setLayout(new BorderLayout());
        mkModel = new MkModel(); tblMk = new JTable(mkModel); tblMk.setFillsViewportHeight(true);
        card.add(new JScrollPane(tblMk), BorderLayout.CENTER);
        JPanel bottom = new JPanel(new BorderLayout()); bottom.setOpaque(false);
        lblTotal = new JLabel("Total SKS: 0"); lblTotal.setFont(UITheme.uiFont(Font.PLAIN, 14));
        bottom.add(lblTotal, BorderLayout.WEST);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT)); actions.setOpaque(false);
        UITheme.AnimatedButton bSimpan = new UITheme.AnimatedButton("Simpan Draft"); bSimpan.setPreferredSize(new Dimension(140,34)); bSimpan.addActionListener(e->onSimpanDraft());
        UITheme.AnimatedButton bAjukan = new UITheme.AnimatedButton("Ajukan FRS"); bAjukan.setPreferredSize(new Dimension(140,34)); bAjukan.addActionListener(e->onAjukanFrs());
        actions.add(bSimpan); actions.add(bAjukan);
        bottom.add(actions, BorderLayout.EAST);
        card.add(bottom, BorderLayout.SOUTH);
        root.add(card, BorderLayout.CENTER);
        SwingUtilities.invokeLater(this::loadIsiFrsData);
        return root;
    }

    private void loadIsiFrsData(){
        try {
            currentFrs = frsService.getOrCreateDraft(mhs.getNim(), mhs.getSemester());
            var mks = frsService.listMkSemester(mhs.getSemester());
            mkModel.setData(mks); recalcTotal();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal memuat FRS", JOptionPane.ERROR_MESSAGE); }
    }
    private void onSimpanDraft(){
        try { frsService.saveDraft(currentFrs, mkModel.getSelectedKode(), user.getIdUser()); JOptionPane.showMessageDialog(this, "Draft disimpan."); } 
        catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal", JOptionPane.ERROR_MESSAGE);} }
    private void onAjukanFrs(){
        try { frsService.submit(currentFrs, mkModel.getSelectedKode(), user.getIdUser()); JOptionPane.showMessageDialog(this, "FRS diajukan."); refreshStatusUI(); } 
        catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal", JOptionPane.ERROR_MESSAGE);} }
    private void recalcTotal(){ lblTotal.setText("Total SKS: "+ mkModel.getSelectedTotalSks()); }

    private static class MkModel extends javax.swing.table.AbstractTableModel {
        private final String[] cols={"Pilih","Kode","Nama Mata Kuliah","SKS"};
        private final Class<?>[] types={Boolean.class,String.class,String.class,Integer.class};
        private java.util.List<id.ac.kampus.frs.model.MataKuliah> rows=new java.util.ArrayList<>();
        private java.util.List<Boolean> selected=new java.util.ArrayList<>();
        void setData(java.util.List<id.ac.kampus.frs.model.MataKuliah> list){ rows=list; selected = new java.util.ArrayList<>(java.util.Collections.nCopies(list.size(), false)); fireTableDataChanged(); }
        java.util.List<String> getSelectedKode(){ java.util.List<String> k=new java.util.ArrayList<>(); for(int i=0;i<rows.size();i++) if(Boolean.TRUE.equals(selected.get(i))) k.add(rows.get(i).getKodeMk()); return k; }
        int getSelectedTotalSks(){ int t=0; for(int i=0;i<rows.size();i++) if(Boolean.TRUE.equals(selected.get(i))) t+=rows.get(i).getSks(); return t; }
        @Override public int getRowCount(){ return rows.size(); }
        @Override public int getColumnCount(){ return cols.length; }
        @Override public String getColumnName(int c){ return cols[c]; }
        @Override public Class<?> getColumnClass(int c){ return types[c]; }
        @Override public boolean isCellEditable(int r,int c){ return c==0; }
        @Override public Object getValueAt(int r,int c){ var mk=rows.get(r); return switch(c){ case 0->selected.get(r); case 1->mk.getKodeMk(); case 2->mk.getNamaMk(); case 3->mk.getSks(); default->null; }; }
        @Override public void setValueAt(Object v,int r,int c){ if(c==0){ selected.set(r, (Boolean)v); }}
    }

    // ===== Status =====
    private JLabel lblStatus; private JTable tblHist; private HistoryModel histModel;
    private JComponent buildStatusPage(){
        JPanel root = new JPanel(new BorderLayout()); root.setOpaque(false);
        UITheme.CardPanel card = new UITheme.CardPanel(20); card.setLayout(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT)); top.setOpaque(false);
        lblStatus = new JLabel("Status: -"); lblStatus.setFont(UITheme.uiFont(Font.BOLD, 14));
        top.add(lblStatus); card.add(top, BorderLayout.NORTH);
        histModel = new HistoryModel(); tblHist = new JTable(histModel);
        card.add(new JScrollPane(tblHist), BorderLayout.CENTER);
        root.add(card, BorderLayout.CENTER);
        SwingUtilities.invokeLater(this::refreshStatusUI);
        return root;
    }
    private void refreshStatusUI(){
        try {
            var frsDAO = new id.ac.kampus.frs.dao.FRSDAO();
            if (currentFrs == null) currentFrs = frsService.getOrCreateDraft(mhs.getNim(), mhs.getSemester());
            var latest = frsDAO.findById(currentFrs.getIdFrs()); if (latest!=null) currentFrs = latest;
            lblStatus.setText("Status: "+ currentFrs.getStatus());
            var pdao = new id.ac.kampus.frs.dao.PersetujuanFRSDAO();
            var list = pdao.listByFrs(currentFrs.getIdFrs()); histModel.setData(list);
        } catch(Exception ignored){}
    }
    private static class HistoryModel extends javax.swing.table.AbstractTableModel {
        private final String[] cols={"Waktu","NIDN Dosen","Status","Catatan"};
        private java.util.List<id.ac.kampus.frs.model.PersetujuanFRS> rows=new java.util.ArrayList<>();
        void setData(java.util.List<id.ac.kampus.frs.model.PersetujuanFRS> l){ rows=l; fireTableDataChanged(); }
        @Override public int getRowCount(){ return rows.size(); }
        @Override public int getColumnCount(){ return cols.length; }
        @Override public String getColumnName(int c){ return cols[c]; }
        @Override public Object getValueAt(int r,int c){ var p=rows.get(r); return switch(c){ case 0->p.getWaktu(); case 1->p.getIdDosen(); case 2->p.getStatus(); case 3->p.getCatatan(); default->null; }; }
    }
}
