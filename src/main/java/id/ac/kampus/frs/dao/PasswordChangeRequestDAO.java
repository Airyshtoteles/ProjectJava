package id.ac.kampus.frs.dao;

import id.ac.kampus.frs.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PasswordChangeRequestDAO {
    public void createRequest(int userId, String newPasswordHash) throws SQLException {
        String sql = "INSERT INTO password_change_request (user_id, new_password_hash) VALUES (?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, newPasswordHash);
            ps.executeUpdate();
        }
    }

    public List<Row> listPending() throws SQLException {
        String sql = "SELECT r.id, r.user_id, u.username, u.role, r.requested_at FROM password_change_request r " +
                "JOIN user u ON u.id_user = r.user_id WHERE r.status='PENDING' ORDER BY r.requested_at ASC";
        List<Row> out = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Row r = new Row();
                r.id = rs.getInt(1);
                r.userId = rs.getInt(2);
                r.username = rs.getString(3);
                r.role = rs.getString(4);
                r.requestedAt = rs.getTimestamp(5);
                out.add(r);
            }
        }
        return out;
    }

    public void approve(int requestId, int adminUserId) throws SQLException {
        // Update user's password_hash from request, then mark APPROVED
        String select = "SELECT user_id, new_password_hash FROM password_change_request WHERE id=? AND status='PENDING'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement psSel = con.prepareStatement(select)) {
            psSel.setInt(1, requestId);
            try (ResultSet rs = psSel.executeQuery()) {
                if (!rs.next()) throw new SQLException("Permintaan tidak ditemukan atau sudah diproses");
                int userId = rs.getInt(1);
                String hash = rs.getString(2);
                try (PreparedStatement psUpdUser = con.prepareStatement("UPDATE user SET password_hash=? WHERE id_user=?");
                     PreparedStatement psUpdReq = con.prepareStatement("UPDATE password_change_request SET status='APPROVED', approved_by=?, approved_at=CURRENT_TIMESTAMP WHERE id=?")) {
                    psUpdUser.setString(1, hash);
                    psUpdUser.setInt(2, userId);
                    psUpdUser.executeUpdate();

                    psUpdReq.setInt(1, adminUserId);
                    psUpdReq.setInt(2, requestId);
                    psUpdReq.executeUpdate();
                }
            }
        }
    }

    public void reject(int requestId, int adminUserId, String reason) throws SQLException {
        String sql = "UPDATE password_change_request SET status='REJECTED', approved_by=?, approved_at=CURRENT_TIMESTAMP, rejected_reason=? WHERE id=? AND status='PENDING'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, adminUserId);
            ps.setString(2, reason);
            ps.setInt(3, requestId);
            int updated = ps.executeUpdate();
            if (updated == 0) throw new SQLException("Permintaan tidak ditemukan atau sudah diproses");
        }
    }

    public static class Row {
        public int id;
        public int userId;
        public String username;
        public String role;
        public java.sql.Timestamp requestedAt;
    }
}
