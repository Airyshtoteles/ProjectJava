package id.ac.kampus.frs.service;

import id.ac.kampus.frs.dao.MahasiswaDAO;
import id.ac.kampus.frs.dao.UserDAO;
import id.ac.kampus.frs.model.Mahasiswa;
import id.ac.kampus.frs.model.User;
import id.ac.kampus.frs.util.ActivityLogger;
import id.ac.kampus.frs.util.PasswordUtil;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();
    private final MahasiswaDAO mahasiswaDAO = new MahasiswaDAO();

    public static class AuthResult {
        public final User user;
        public final Mahasiswa mahasiswa; // nullable unless MAHASISWA
        public AuthResult(User user, Mahasiswa mahasiswa) { this.user = user; this.mahasiswa = mahasiswa; }
    }

    public AuthResult login(String username, String password) throws Exception {
        User user = userDAO.findByUsername(username);
        if (user == null) throw new IllegalArgumentException("User tidak ditemukan");
        String hash = PasswordUtil.sha256(password);
        if (!hash.equals(user.getPasswordHash())) throw new IllegalArgumentException("Password salah");
        ActivityLogger.log(user.getIdUser(), "LOGIN");
        Mahasiswa mhs = null;
        if (user.getRole() == User.Role.MAHASISWA) {
            mhs = mahasiswaDAO.findByUserId(user.getIdUser());
        }
        return new AuthResult(user, mhs);
    }
}
