package id.ac.kampus.frs.ui;

import id.ac.kampus.frs.model.User;

 

/** Dashboard Admin bertema modern (placeholder navigasi dasar). */
public class ModernAdminDashboardFrame extends BaseDashboardFrame {
    public ModernAdminDashboardFrame(User user) {
        super("Dashboard Admin");
        setUserInfo(user.getUsername());
        // Embed panel-panel lama yang sudah fungsional
        registerPage("Mahasiswa", "Mahasiswa", new PanelMasterMahasiswa());
        registerPage("Dosen", "Dosen", new PanelMasterDosen());
        registerPage("Mata Kuliah", "Mata Kuliah", new PanelMasterMataKuliah());
        registerPage("Laporan", "Laporan", new PanelLaporanPersetujuan());
        registerPage("Permintaan Password", "Permintaan Password", new PanelPermintaanPassword(user.getIdUser()));

        // Badge notifikasi jumlah pending permintaan password
        try {
            int pending = new id.ac.kampus.frs.dao.PasswordChangeRequestDAO().listPending().size();
            setSidebarBadge("Permintaan Password", pending);
        } catch (Exception ignored) {}

        showPage("Mahasiswa");
    }

    // (placeholder utility tidak diperlukan lagi karena sudah memakai panel fungsional)
}
