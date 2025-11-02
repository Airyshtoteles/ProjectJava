package id.ac.kampus.frs.ui;

import id.ac.kampus.frs.model.User;

import javax.swing.*;
import java.awt.*;

/** Dashboard Admin bertema modern (placeholder navigasi dasar). */
public class ModernAdminDashboardFrame extends BaseDashboardFrame {
    public ModernAdminDashboardFrame(User user) {
        super("Dashboard Admin");
        setUserInfo(user.getUsername());
        registerPage("Pengaturan", "Pengaturan", placeholder("Pengaturan sistem (akan diintegrasikan)"));
        registerPage("Master Data", "Master Data", placeholder("Master Mahasiswa/Dosen/Mata Kuliah (akan diintegrasikan)"));
        registerPage("Laporan", "Laporan", placeholder("Laporan Persetujuan dan Permintaan Password (akan diintegrasikan)"));
        showPage("Pengaturan");
    }

    private JComponent placeholder(String text){
        UITheme.CardPanel p = new UITheme.CardPanel(20);
        p.setLayout(new GridBagLayout());
        JLabel l = new JLabel(text); l.setFont(UITheme.uiFont(Font.ITALIC, 14)); l.setForeground(new Color(0,0,0,120));
        p.add(l, new GridBagConstraints());
        return p;
    }
}
