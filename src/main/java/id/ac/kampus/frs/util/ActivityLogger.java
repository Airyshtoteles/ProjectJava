package id.ac.kampus.frs.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ActivityLogger {
    public static void log(int userId, String aktivitas) {
        String sql = "INSERT INTO log_aktivitas (id_user, aktivitas) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, aktivitas);
            ps.executeUpdate();
        } catch (SQLException e) {
            // Swallow logging errors to not block the app
            System.err.println("[LOG ERROR] " + e.getMessage());
        }
    }
}
