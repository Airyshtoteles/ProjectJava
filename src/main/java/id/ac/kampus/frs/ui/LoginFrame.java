package id.ac.kampus.frs.ui;

import id.ac.kampus.frs.model.User;
import id.ac.kampus.frs.service.AuthService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final JTextField tfUsername = new JTextField();
    private final JPasswordField pfPassword = new JPasswordField();
    private final JComboBox<String> cbRole = new JComboBox<>(new String[]{"MAHASISWA", "DOSEN", "ADMIN"});
    private final AuthService authService = new AuthService();

    public LoginFrame() {
        setTitle("Aplikasi FRS Online • Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(440, 280);
        setLocationRelativeTo(null);
        buildUI();
    }

    private void buildUI() {
    JPanel form = new JPanel(new GridBagLayout());
    form.setBorder(BorderFactory.createEmptyBorder(12,16,12,16));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8,8,8,8);
        gc.anchor = GridBagConstraints.WEST;

        int r = 0;
        gc.gridx = 0; gc.gridy = r; form.add(new JLabel("Role"), gc);
        gc.gridx = 1; gc.gridy = r++; gc.fill = GridBagConstraints.HORIZONTAL; form.add(cbRole, gc);

        gc.gridx = 0; gc.gridy = r; gc.fill = GridBagConstraints.NONE; form.add(new JLabel("Username"), gc);
        gc.gridx = 1; gc.gridy = r++; gc.fill = GridBagConstraints.HORIZONTAL; form.add(tfUsername, gc);

        gc.gridx = 0; gc.gridy = r; gc.fill = GridBagConstraints.NONE; form.add(new JLabel("Password"), gc);
        gc.gridx = 1; gc.gridy = r++; gc.fill = GridBagConstraints.HORIZONTAL; form.add(pfPassword, gc);

        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(e -> doLogin());
        gc.gridx = 1; gc.gridy = r; gc.anchor = GridBagConstraints.EAST; gc.fill = GridBagConstraints.NONE;
        form.add(btnLogin, gc);

        // Make ENTER key trigger login
        getRootPane().setDefaultButton(btnLogin);

        add(form);
    }

    private void doLogin() {
        String username = tfUsername.getText().trim();
        String password = new String(pfPassword.getPassword());
        String roleExpected = (String) cbRole.getSelectedItem();
        try {
            AuthService.AuthResult result = authService.login(username, password);
            User user = result.user;
            if (!user.getRole().name().equals(roleExpected)) {
                JOptionPane.showMessageDialog(this, "Role tidak sesuai.", "Gagal Login", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Route to dashboard
            SwingUtilities.invokeLater(() -> {
                dispose();
                switch (user.getRole()) {
                    case MAHASISWA -> new MahasiswaDashboardFrame(user, result.mahasiswa).setVisible(true);
                    case DOSEN -> new DosenDashboardFrame(user).setVisible(true);
                    case ADMIN -> new AdminDashboardFrame(user).setVisible(true);
                }
            });
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Gagal Login", JOptionPane.ERROR_MESSAGE);
        }
    }
}
