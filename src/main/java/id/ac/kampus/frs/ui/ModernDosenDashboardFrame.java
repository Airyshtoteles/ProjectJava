package id.ac.kampus.frs.ui;

import id.ac.kampus.frs.model.User;

import javax.swing.*;
import java.awt.*;

/** Dashboard Dosen bertema modern (placeholder navigasi dasar). */
public class ModernDosenDashboardFrame extends BaseDashboardFrame {
    private final String nidn;
    public ModernDosenDashboardFrame(User user, String nidn) {
        super("Dashboard Dosen");
        this.nidn = nidn;
        setUserInfo("NIDN: " + nidn + " • " + user.getUsername());

        registerPage("Pengajuan", "Pengajuan", placeholder("Daftar pengajuan FRS bimbingan (akan diintegrasikan)"));
        registerPage("Detail", "Detail", placeholder("Detail FRS yang dipilih (akan diintegrasikan)"));
        showPage("Pengajuan");
    }

    private JComponent placeholder(String text){
        UITheme.CardPanel p = new UITheme.CardPanel(20);
        p.setLayout(new GridBagLayout());
        JLabel l = new JLabel(text); l.setFont(UITheme.uiFont(Font.ITALIC, 14)); l.setForeground(new Color(0,0,0,120));
        p.add(l, new GridBagConstraints());
        return p;
    }
}
