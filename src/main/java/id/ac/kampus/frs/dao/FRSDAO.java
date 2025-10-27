package id.ac.kampus.frs.dao;

import id.ac.kampus.frs.model.FRS;
import id.ac.kampus.frs.model.FRS.Status;
import id.ac.kampus.frs.model.DetailFRS;
import id.ac.kampus.frs.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FRSDAO {
    public FRS getOrCreateDraft(String nim, int semester) throws SQLException {
        String select = "SELECT * FROM frs WHERE nim=? AND semester=? ORDER BY id_frs DESC LIMIT 1";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(select)) {
            ps.setString(1, nim);
            ps.setInt(2, semester);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapFrs(rs);
                }
            }
        }
        // create new draft
        String insert = "INSERT INTO frs (nim, semester, total_sks, status) VALUES (?,?,0,'DRAFT')";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nim);
            ps.setInt(2, semester);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    FRS frs = new FRS();
                    frs.setIdFrs(id);
                    frs.setNim(nim);
                    frs.setSemester(semester);
                    frs.setTotalSks(0);
                    frs.setStatus(Status.DRAFT);
                    return frs;
                }
            }
        }
        throw new SQLException("Failed to create draft FRS");
    }

    public List<DetailFRS> listDetails(int idFrs) throws SQLException {
        String sql = "SELECT id_detail, id_frs, kode_mk FROM detail_frs WHERE id_frs=?";
        List<DetailFRS> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFrs);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetailFRS d = new DetailFRS();
                    d.setIdDetail(rs.getInt("id_detail"));
                    d.setIdFrs(rs.getInt("id_frs"));
                    d.setKodeMk(rs.getString("kode_mk"));
                    list.add(d);
                }
            }
        }
        return list;
    }

    public void replaceDetails(int idFrs, List<String> kodeMks) throws SQLException {
        String del = "DELETE FROM detail_frs WHERE id_frs=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(del)) {
            ps.setInt(1, idFrs);
            ps.executeUpdate();
        }
        String ins = "INSERT INTO detail_frs (id_frs, kode_mk) VALUES (?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(ins)) {
            for (String kode : kodeMks) {
                ps.setInt(1, idFrs);
                ps.setString(2, kode);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    public void updateSummary(int idFrs, int totalSks, Status status, LocalDateTime tanggalPengajuan) throws SQLException {
        String sql = "UPDATE frs SET total_sks=?, status=?, tanggal_pengajuan=? WHERE id_frs=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, totalSks);
            ps.setString(2, status.name());
            if (tanggalPengajuan == null) ps.setNull(3, Types.TIMESTAMP); else ps.setTimestamp(3, Timestamp.valueOf(tanggalPengajuan));
            ps.setInt(4, idFrs);
            ps.executeUpdate();
        }
    }

    private FRS mapFrs(ResultSet rs) throws SQLException {
        FRS f = new FRS();
        f.setIdFrs(rs.getInt("id_frs"));
        f.setNim(rs.getString("nim"));
        f.setSemester(rs.getInt("semester"));
        f.setTotalSks(rs.getInt("total_sks"));
        f.setStatus(Status.valueOf(rs.getString("status")));
        Timestamp t = rs.getTimestamp("tanggal_pengajuan");
        if (t != null) f.setTanggalPengajuan(t.toLocalDateTime());
        f.setLockedByAdmin(rs.getBoolean("locked_by_admin"));
        return f;
    }
}
