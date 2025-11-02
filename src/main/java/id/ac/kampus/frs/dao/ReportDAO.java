package id.ac.kampus.frs.dao;

import id.ac.kampus.frs.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {
    public List<ApprovalRow> listApprovals(Integer limit) throws SQLException {
        String sql = "SELECT p.waktu, p.status, p.catatan, f.id_frs, f.semester, m.nim, m.nama, d.nidn, d.nama " +
                "FROM persetujuan_frs p " +
                "JOIN frs f ON f.id_frs=p.id_frs " +
                "JOIN mahasiswa m ON m.nim=f.nim " +
                "JOIN dosen d ON d.nidn=p.id_dosen " +
                "ORDER BY p.waktu DESC" + (limit != null ? " LIMIT ?" : "");
        List<ApprovalRow> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (limit != null) ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ApprovalRow r = new ApprovalRow();
                    r.waktu = rs.getTimestamp(1);
                    r.status = rs.getString(2);
                    r.catatan = rs.getString(3);
                    r.idFrs = rs.getInt(4);
                    r.semester = rs.getInt(5);
                    r.nim = rs.getString(6);
                    r.namaMhs = rs.getString(7);
                    r.nidn = rs.getString(8);
                    r.namaDosen = rs.getString(9);
                    list.add(r);
                }
            }
        }
        return list;
    }

    public static class ApprovalRow {
        public java.sql.Timestamp waktu;
        public String status;
        public String catatan;
        public int idFrs;
        public int semester;
        public String nim;
        public String namaMhs;
        public String nidn;
        public String namaDosen;
    }
}
