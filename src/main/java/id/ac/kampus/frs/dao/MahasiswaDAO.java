package id.ac.kampus.frs.dao;

import id.ac.kampus.frs.model.Mahasiswa;
import id.ac.kampus.frs.util.DBConnection;

import java.sql.*;

public class MahasiswaDAO {
    public Mahasiswa findByUserId(int userId) throws SQLException {
        String sql = "SELECT nim, nama, jurusan, semester, id_user FROM mahasiswa WHERE id_user = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Mahasiswa m = new Mahasiswa();
                    m.setNim(rs.getString("nim"));
                    m.setNama(rs.getString("nama"));
                    m.setJurusan(rs.getString("jurusan"));
                    m.setSemester(rs.getInt("semester"));
                    m.setIdUser(rs.getInt("id_user"));
                    return m;
                }
            }
        }
        return null;
    }
}
