package id.ac.kampus.frs.ui;

import id.ac.kampus.frs.model.User;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

/** Dashboard Dosen bertema modern. */
public class ModernDosenDashboardFrame extends BaseDashboardFrame {
    private final String nidn;
    private final User user;
    private JTable tblPengajuan, tblDetail;
    private PengajuanModel pengajuanModel;
    private DetailModel detailModel;
    private JLabel lblTotalPengajuan, lblSelectedInfo;

    public ModernDosenDashboardFrame(User user, String nidn) {
        super("Dashboard Dosen");
        this.nidn = nidn;
        this.user = user;
        setUserInfo("NIDN: " + nidn + " • " + user.getUsername());

        // Tombol Notifikasi di header
        UITheme.AnimatedButton btnNotif = new UITheme.AnimatedButton("Notifikasi");
        btnNotif.setPreferredSize(new Dimension(120, 32));
        btnNotif.addActionListener(e -> showNotifications());
        headerActions.add(btnNotif, 0);

        // Tombol Ganti Password
        UITheme.AnimatedButton btnPassword = new UITheme.AnimatedButton("Ganti Password");
        btnPassword.setPreferredSize(new Dimension(140, 32));
        btnPassword.addActionListener(e -> new ChangePasswordDialog(this, user.getIdUser()).setVisible(true));
        headerActions.add(btnPassword, 1);

        registerPage("Dashboard", "Dashboard", buildDashboardPage());
        registerPage("Pengajuan FRS", "Pengajuan FRS", buildPengajuanPage());
        showPage("Dashboard");
        loadPengajuan();
    }

    private void showNotifications() {
        try {
            int count = pengajuanModel != null ? pengajuanModel.getRowCount() : 0;
            JPopupMenu popup = new JPopupMenu();
            popup.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
            if (count > 0) {
                JMenuItem item = new JMenuItem(count + " pengajuan FRS menunggu persetujuan");
                item.addActionListener(e -> showPage("Pengajuan FRS"));
                popup.add(item);
            } else {
                popup.add(new JMenuItem("Tidak ada pengajuan baru"));
            }
            popup.show(headerActions, 0, headerActions.getHeight());
        } catch (Exception e) {
            JPopupMenu popup = new JPopupMenu();
            popup.add(new JMenuItem("Tidak ada notifikasi"));
            popup.show(headerActions, 0, headerActions.getHeight());
        }
    }

    private JComponent buildDashboardPage() {
        JPanel root = new JPanel(new BorderLayout(16, 16));
        root.setOpaque(false);
        root.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Welcome Card
        UITheme.CardPanel welcomeCard = new UITheme.CardPanel(20);
        welcomeCard.setLayout(new BorderLayout());
        welcomeCard.setPreferredSize(new Dimension(0, 120));

        JPanel welcomeContent = new JPanel(new GridBagLayout());
        welcomeContent.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.WEST;
        gc.insets = new Insets(8, 16, 8, 16);

        JLabel lblWelcome = new JLabel("Selamat Datang, Dosen!");
        lblWelcome.setFont(UITheme.uiFont(Font.BOLD, 24));
        lblWelcome.setForeground(UITheme.PRIMARY);
        gc.gridx = 0; gc.gridy = 0;
        welcomeContent.add(lblWelcome, gc);

        JLabel lblNidn = new JLabel("NIDN: " + nidn);
        lblNidn.setFont(UITheme.uiFont(Font.PLAIN, 16));
        lblNidn.setForeground(UITheme.TEXT);
        gc.gridy = 1;
        welcomeContent.add(lblNidn, gc);

        welcomeCard.add(welcomeContent, BorderLayout.WEST);

        // Stats Panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 16, 0));
        statsPanel.setOpaque(false);

        int pendingCount = 0;
        try {
            pendingCount = new id.ac.kampus.frs.service.DosenService().listPengajuan(nidn).size();
        } catch (Exception ignored) {}

        statsPanel.add(createStatCard("i", "Pengajuan Pending", String.valueOf(pendingCount), new Color(59, 130, 246)));
        statsPanel.add(createStatCard("OK", "Total Disetujui", "-", new Color(34, 197, 94)));
        statsPanel.add(createStatCard("Mhs", "Mahasiswa Bimbingan", "-", new Color(168, 85, 247)));

        // Quick Actions
        UITheme.CardPanel actionsCard = new UITheme.CardPanel(20);
        actionsCard.setLayout(new BorderLayout());

        JLabel lblActions = new JLabel("Aksi Cepat");
        lblActions.setFont(UITheme.uiFont(Font.BOLD, 16));
        lblActions.setForeground(UITheme.TEXT);
        lblActions.setBorder(BorderFactory.createEmptyBorder(8, 8, 16, 8));
        actionsCard.add(lblActions, BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        buttonsPanel.setOpaque(false);

        UITheme.AnimatedButton btnLihatPengajuan = new UITheme.AnimatedButton("Lihat Pengajuan FRS");
        btnLihatPengajuan.setPreferredSize(new Dimension(180, 40));
        btnLihatPengajuan.addActionListener(e -> showPage("Pengajuan FRS"));
        buttonsPanel.add(btnLihatPengajuan);

        UITheme.AnimatedButton btnRefresh = new UITheme.AnimatedButton("Refresh Data");
        btnRefresh.setPreferredSize(new Dimension(140, 40));
        btnRefresh.addActionListener(e -> {
            loadPengajuan();
            JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!");
        });
        buttonsPanel.add(btnRefresh);

        actionsCard.add(buttonsPanel, BorderLayout.CENTER);

        // Layout
        JPanel topPanel = new JPanel(new BorderLayout(16, 16));
        topPanel.setOpaque(false);
        topPanel.add(welcomeCard, BorderLayout.NORTH);
        topPanel.add(statsPanel, BorderLayout.CENTER);

        root.add(topPanel, BorderLayout.NORTH);
        root.add(actionsCard, BorderLayout.CENTER);

        return root;
    }

    private JPanel createStatCard(String icon, String title, String value, Color accentColor) {
        UITheme.CardPanel card = new UITheme.CardPanel(16);
        card.setLayout(new BorderLayout());

        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.CENTER;

        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(UITheme.uiFont(Font.PLAIN, 32));
        gc.gridx = 0; gc.gridy = 0;
        content.add(lblIcon, gc);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(UITheme.uiFont(Font.BOLD, 28));
        lblValue.setForeground(accentColor);
        gc.gridy = 1;
        gc.insets = new Insets(8, 0, 4, 0);
        content.add(lblValue, gc);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(UITheme.uiFont(Font.PLAIN, 12));
        lblTitle.setForeground(new Color(100, 100, 100));
        gc.gridy = 2;
        gc.insets = new Insets(0, 0, 0, 0);
        content.add(lblTitle, gc);

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private JComponent buildPengajuanPage() {
        JPanel root = new JPanel(new BorderLayout(16, 16));
        root.setOpaque(false);
        root.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Top info bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        lblTotalPengajuan = new JLabel("Total Pengajuan: 0");
        lblTotalPengajuan.setFont(UITheme.uiFont(Font.BOLD, 14));
        lblTotalPengajuan.setForeground(UITheme.TEXT);
        topBar.add(lblTotalPengajuan, BorderLayout.WEST);

        lblSelectedInfo = new JLabel("Pilih mahasiswa untuk melihat detail FRS");
        lblSelectedInfo.setFont(UITheme.uiFont(Font.ITALIC, 12));
        lblSelectedInfo.setForeground(new Color(120, 120, 120));
        topBar.add(lblSelectedInfo, BorderLayout.EAST);

        root.add(topBar, BorderLayout.NORTH);

        // Main content - Split Panel
        JPanel mainContent = new JPanel(new GridBagLayout());
        mainContent.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH;
        gc.insets = new Insets(0, 0, 0, 8);

        // Left Panel - Daftar Pengajuan
        UITheme.CardPanel leftCard = new UITheme.CardPanel(16);
        leftCard.setLayout(new BorderLayout());

        JLabel lblLeft = new JLabel("Daftar Pengajuan FRS");
        lblLeft.setFont(UITheme.uiFont(Font.BOLD, 14));
        lblLeft.setForeground(UITheme.TEXT);
        lblLeft.setBorder(BorderFactory.createEmptyBorder(8, 8, 12, 8));
        leftCard.add(lblLeft, BorderLayout.NORTH);

        pengajuanModel = new PengajuanModel();
        tblPengajuan = new JTable(pengajuanModel);
        tblPengajuan.setRowHeight(32);
        tblPengajuan.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPengajuan.setFont(UITheme.uiFont(Font.PLAIN, 12));
        tblPengajuan.getTableHeader().setFont(UITheme.uiFont(Font.BOLD, 12));
        tblPengajuan.setShowGrid(false);
        tblPengajuan.setIntercellSpacing(new Dimension(0, 0));

        // Custom renderer untuk status
        tblPengajuan.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(CENTER);
                if ("MENUNGGU".equals(value)) {
                    setForeground(new Color(234, 179, 8));
                    setText("MENUNGGU");
                } else if ("DISETUJUI".equals(value)) {
                    setForeground(new Color(34, 197, 94));
                    setText("DISETUJUI");
                } else if ("DITOLAK".equals(value)) {
                    setForeground(new Color(239, 68, 68));
                    setText("DITOLAK");
                }
                return this;
            }
        });

        tblPengajuan.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) onSelectPengajuan();
        });

        JScrollPane scrollLeft = new JScrollPane(tblPengajuan);
        scrollLeft.setBorder(BorderFactory.createEmptyBorder());
        leftCard.add(scrollLeft, BorderLayout.CENTER);

        gc.gridx = 0; gc.gridy = 0;
        gc.weightx = 0.55; gc.weighty = 1;
        mainContent.add(leftCard, gc);

        // Right Panel - Detail FRS
        UITheme.CardPanel rightCard = new UITheme.CardPanel(16);
        rightCard.setLayout(new BorderLayout());

        JLabel lblRight = new JLabel("📚 Detail Mata Kuliah");
        lblRight.setFont(UITheme.uiFont(Font.BOLD, 14));
        lblRight.setForeground(UITheme.TEXT);
        lblRight.setBorder(BorderFactory.createEmptyBorder(8, 8, 12, 8));
        rightCard.add(lblRight, BorderLayout.NORTH);

        detailModel = new DetailModel();
        tblDetail = new JTable(detailModel);
        tblDetail.setRowHeight(28);
        tblDetail.setFont(UITheme.uiFont(Font.PLAIN, 12));
        tblDetail.getTableHeader().setFont(UITheme.uiFont(Font.BOLD, 12));
        tblDetail.setShowGrid(false);

        JScrollPane scrollRight = new JScrollPane(tblDetail);
        scrollRight.setBorder(BorderFactory.createEmptyBorder());
        rightCard.add(scrollRight, BorderLayout.CENTER);

        // Action buttons
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        actions.setOpaque(false);
        actions.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        UITheme.AnimatedButton btnPrint = new UITheme.AnimatedButton("🖨️ Print");
        btnPrint.setPreferredSize(new Dimension(90, 36));
        btnPrint.addActionListener(e -> onPrint());

        UITheme.AnimatedButton btnPdf = new UITheme.AnimatedButton("📄 PDF");
        btnPdf.setPreferredSize(new Dimension(90, 36));
        btnPdf.addActionListener(e -> onCetakPdf());

        UITheme.AnimatedButton approve = new UITheme.AnimatedButton("✅ Setujui");
        approve.setPreferredSize(new Dimension(100, 36));
        approve.addActionListener(e -> onApprove());

        UITheme.AnimatedButton reject = new UITheme.AnimatedButton("❌ Tolak");
        reject.setPreferredSize(new Dimension(100, 36));
        reject.addActionListener(e -> onReject());

        actions.add(btnPrint);
        actions.add(btnPdf);
        actions.add(Box.createHorizontalStrut(16));
        actions.add(approve);
        actions.add(reject);

        rightCard.add(actions, BorderLayout.SOUTH);

        gc.gridx = 1;
        gc.weightx = 0.45;
        gc.insets = new Insets(0, 8, 0, 0);
        mainContent.add(rightCard, gc);

        root.add(mainContent, BorderLayout.CENTER);
        return root;
    }

    private void onPrint() {
        try {
            if (tblDetail.getRowCount() > 0) {
                tblDetail.print(JTable.PrintMode.FIT_WIDTH, 
                    new java.text.MessageFormat("Detail FRS Mahasiswa"), 
                    new java.text.MessageFormat("Halaman {0}"));
            } else {
                JOptionPane.showMessageDialog(this, "Tidak ada data untuk dicetak.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal mencetak: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCetakPdf() {
        int r = tblPengajuan.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Pilih mahasiswa terlebih dahulu.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        var row = pengajuanModel.getRow(r);

        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new java.io.File("KSM_" + row.nim + "_Sem" + row.semester + ".pdf"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                id.ac.kampus.frs.util.PdfUtil.exportFrs(fc.getSelectedFile(), "Semester " + row.semester, row.nim, row.nama, row.semester, detailModel.rows, row.totalSks, row.status);
                JOptionPane.showMessageDialog(this, "PDF berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadPengajuan() {
        try {
            var list = new id.ac.kampus.frs.service.DosenService().listPengajuan(nidn);
            pengajuanModel.setData(list);
            lblTotalPengajuan.setText("Total Pengajuan: " + list.size());
            
            // Update badge
            setSidebarBadge("Pengajuan FRS", list.size());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal memuat", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onSelectPengajuan() {
        int r = tblPengajuan.getSelectedRow();
        if (r < 0) {
            detailModel.setData(new java.util.ArrayList<>());
            lblSelectedInfo.setText("Pilih mahasiswa untuk melihat detail FRS");
            return;
        }
        int idFrs = pengajuanModel.getIdFrs(r);
        var row = pengajuanModel.getRow(r);
        lblSelectedInfo.setText("📌 " + row.nama + " (" + row.nim + ") - " + row.totalSks + " SKS");
        try {
            var mks = new id.ac.kampus.frs.service.DosenService().listDetailMk(idFrs);
            detailModel.setData(mks);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal detail", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onApprove() {
        int r = tblPengajuan.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Pilih mahasiswa terlebih dahulu.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int idFrs = pengajuanModel.getIdFrs(r);
        var row = pengajuanModel.getRow(r);

        int confirm = JOptionPane.showConfirmDialog(this,
            "Setujui FRS mahasiswa " + row.nama + " (" + row.nim + ")?",
            "Konfirmasi Persetujuan", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String cat = JOptionPane.showInputDialog(this, "Catatan (opsional):");
            try {
                new id.ac.kampus.frs.service.DosenService().approve(idFrs, nidn, user.getIdUser(), cat);
                JOptionPane.showMessageDialog(this, "FRS berhasil disetujui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadPengajuan();
                detailModel.setData(new java.util.ArrayList<>());
                lblSelectedInfo.setText("Pilih mahasiswa untuk melihat detail FRS");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onReject() {
        int r = tblPengajuan.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Pilih mahasiswa terlebih dahulu.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int idFrs = pengajuanModel.getIdFrs(r);
        var row = pengajuanModel.getRow(r);

        String cat = JOptionPane.showInputDialog(this, "Alasan penolakan FRS " + row.nama + ":");
        if (cat == null || cat.isBlank()) {
            JOptionPane.showMessageDialog(this, "Alasan penolakan wajib diisi.", "Info", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            new id.ac.kampus.frs.service.DosenService().reject(idFrs, nidn, user.getIdUser(), cat);
            JOptionPane.showMessageDialog(this, "FRS berhasil ditolak.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            loadPengajuan();
            detailModel.setData(new java.util.ArrayList<>());
            lblSelectedInfo.setText("Pilih mahasiswa untuk melihat detail FRS");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class PengajuanModel extends javax.swing.table.AbstractTableModel {
        private final String[] cols = {"ID", "NIM", "Nama", "Sem", "SKS", "Status"};
        private java.util.List<id.ac.kampus.frs.dao.FRSDAO.PengajuanRow> rows = new java.util.ArrayList<>();

        void setData(java.util.List<id.ac.kampus.frs.dao.FRSDAO.PengajuanRow> l) {
            rows = l;
            fireTableDataChanged();
        }

        int getIdFrs(int r) { return rows.get(r).idFrs; }
        id.ac.kampus.frs.dao.FRSDAO.PengajuanRow getRow(int r) { return rows.get(r); }

        @Override public int getRowCount() { return rows.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int c) { return cols[c]; }
        @Override public Object getValueAt(int r, int c) {
            var x = rows.get(r);
            return switch (c) {
                case 0 -> x.idFrs;
                case 1 -> x.nim;
                case 2 -> x.nama;
                case 3 -> x.semester;
                case 4 -> x.totalSks;
                case 5 -> x.status;
                default -> null;
            };
        }
    }

    private static class DetailModel extends javax.swing.table.AbstractTableModel {
        private final String[] cols = {"Kode MK", "Nama Mata Kuliah", "SKS"};
        java.util.List<id.ac.kampus.frs.model.MataKuliah> rows = new java.util.ArrayList<>();

        void setData(java.util.List<id.ac.kampus.frs.model.MataKuliah> l) {
            rows = l;
            fireTableDataChanged();
        }

        @Override public int getRowCount() { return rows.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int c) { return cols[c]; }
        @Override public Object getValueAt(int r, int c) {
            var mk = rows.get(r);
            return switch (c) {
                case 0 -> mk.getKodeMk();
                case 1 -> mk.getNamaMk();
                case 2 -> mk.getSks();
                default -> null;
            };
        }
    }
}
