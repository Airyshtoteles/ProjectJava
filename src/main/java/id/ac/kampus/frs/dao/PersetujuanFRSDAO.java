    package id.ac.kampus.frs.dao;

import id.ac.kampus.frs.model.PersetujuanFRS;
import id.ac.kampus.frs.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public List<PersetujuanFRS> listByFrs(int idFrs) throws SQLException {
        String sql = "SELECT id, id_frs, id_dosen, status, catatan, waktu FROM persetujuan_frs WHERE id_frs=? ORDER BY waktu DESC";
        List<PersetujuanFRS> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFrs);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PersetujuanFRS p = new PersetujuanFRS();
                    p.setId(rs.getInt(1));
                    p.setIdFrs(rs.getInt(2));
                    p.setIdDosen(rs.getString(3));
                    p.setStatus(PersetujuanFRS.Status.valueOf(rs.getString(4)));
                    p.setCatatan(rs.getString(5));
                    Timestamp t = rs.getTimestamp(6);
                    if (t != null) p.setWaktu(t.toLocalDateTime());
                    list.add(p);
                }
            }
        }
        return list;
    }
}
