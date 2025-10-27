package id.ac.kampus.frs.dao;

import id.ac.kampus.frs.model.PersetujuanFRS;
import id.ac.kampus.frs.util.DBConnection;

import java.sql.*;

public class PersetujuanFRSDAO {
    public void insertApproval(int idFrs, String nidn, PersetujuanFRS.Status status, String catatan) throws SQLException {
        String sql = "INSERT INTO persetujuan_frs (id_frs, id_dosen, status, catatan) VALUES (?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFrs);
            ps.setString(2, nidn);
            ps.setString(3, status.name());
            ps.setString(4, catatan);
            ps.executeUpdate();
        }
    }
}
