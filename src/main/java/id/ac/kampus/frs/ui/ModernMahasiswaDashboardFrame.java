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
    public ModernMahasiswaDashboardFrame(User user, Mahasiswa mhs) {
        super("Dashboard Mahasiswa");
        this.user = user; this.mhs = mhs;
        setUserInfo(mhs.getNama() + " • NIM: " + mhs.getNim());

        // Daftarkan halaman
        registerPage("Profil", "Profil", buildProfilPage());
        registerPage("Isi FRS", "Isi FRS", placeholder("Halaman Isi FRS (akan diintegrasikan)"));
        registerPage("Status", "Status", placeholder("Status pengajuan FRS (akan diintegrasikan)"));

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

    private JComponent placeholder(String text){
        UITheme.CardPanel p = new UITheme.CardPanel(20);
        p.setLayout(new GridBagLayout());
        JLabel l = new JLabel(text); l.setFont(UITheme.uiFont(Font.ITALIC, 14)); l.setForeground(new Color(0,0,0,120));
        p.add(l, new GridBagConstraints());
        return p;
    }
}
