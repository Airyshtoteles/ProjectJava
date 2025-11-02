package id.ac.kampus.frs.ui;

import id.ac.kampus.frs.dao.PasswordChangeRequestDAO;
import id.ac.kampus.frs.util.PasswordUtil;

import javax.swing.*;
import java.awt.*;

public class ChangePasswordDialog extends JDialog {
    public ChangePasswordDialog(Frame owner, int userId) {
        super(owner, "Ajukan Ubah Password", true);
        setSize(400, 220);
        setLocationRelativeTo(owner);
        buildUI(userId);
    }

    private void buildUI(int userId) {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8,8,8,8);
        gc.anchor = GridBagConstraints.WEST;

        JPasswordField pf1 = new JPasswordField(20);
        JPasswordField pf2 = new JPasswordField(20);

        int r=0;
        p.add(new JLabel("Password baru:"), pos(gc,0,r));
        p.add(pf1, pos(gc,1,r++));
        p.add(new JLabel("Ulangi password:"), pos(gc,0,r));
        p.add(pf2, pos(gc,1,r++));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAjukan = new JButton("Ajukan");
        JButton btnBatal = new JButton("Batal");
        actions.add(btnBatal);
        actions.add(btnAjukan);

        btnBatal.addActionListener(e -> dispose());
        btnAjukan.addActionListener(e -> {
            String p1 = new String(pf1.getPassword());
            String p2 = new String(pf2.getPassword());
            if (p1.isBlank() || p2.isBlank()) { JOptionPane.showMessageDialog(this, "Password tidak boleh kosong"); return; }
            if (!p1.equals(p2)) { JOptionPane.showMessageDialog(this, "Konfirmasi password tidak sama"); return; }
            try {
                String hash = PasswordUtil.sha256(p1);
                new PasswordChangeRequestDAO().createRequest(userId, hash);
                JOptionPane.showMessageDialog(this, "Permintaan terkirim. Menunggu persetujuan admin.");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal mengajukan", JOptionPane.ERROR_MESSAGE);
            }
        });

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(p, BorderLayout.CENTER);
        getContentPane().add(actions, BorderLayout.SOUTH);
    }

    private GridBagConstraints pos(GridBagConstraints base, int x, int y) {
        GridBagConstraints c = (GridBagConstraints) base.clone();
        c.gridx = x; c.gridy = y;
        return c;
    }
}
