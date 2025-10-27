package id.ac.kampus.frs.dao;

import id.ac.kampus.frs.model.User;
import id.ac.kampus.frs.util.DBConnection;

import java.sql.*;

public class UserDAO {
    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT id_user, username, password_hash, role FROM user WHERE username = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setIdUser(rs.getInt("id_user"));
                    u.setUsername(rs.getString("username"));
                    u.setPasswordHash(rs.getString("password_hash"));
                    u.setRole(User.Role.valueOf(rs.getString("role")));
                    return u;
                }
            }
        }
        return null;
    }
}
