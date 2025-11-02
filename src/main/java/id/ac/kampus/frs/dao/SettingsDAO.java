package id.ac.kampus.frs.dao;

import id.ac.kampus.frs.model.Settings;
import id.ac.kampus.frs.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;

public class SettingsDAO {
    public Settings get() throws SQLException {
        String sql = "SELECT semester_aktif, frs_aktif, tanggal_mulai, tanggal_selesai FROM settings WHERE id=1";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                Settings s = new Settings();
                s.setSemesterAktif(rs.getInt(1));
                s.setFrsAktif(rs.getBoolean(2));
                Timestamp tm = rs.getTimestamp(3);
                Timestamp ts = rs.getTimestamp(4);
                if (tm != null) s.setTanggalMulai(tm.toLocalDateTime());
                if (ts != null) s.setTanggalSelesai(ts.toLocalDateTime());
                return s;
            }
        }
        // If not found, create default
        Settings s = new Settings();
        s.setSemesterAktif(1);
        s.setFrsAktif(true);
        return s;
    }

    public void save(Settings s) throws SQLException {
        final boolean sqlite = DBConnection.isSQLite();
        final String sql = sqlite
                ? "INSERT INTO settings (id, semester_aktif, frs_aktif, tanggal_mulai, tanggal_selesai) VALUES (1,?,?,?,?) " +
                  "ON CONFLICT(id) DO UPDATE SET semester_aktif=excluded.semester_aktif, frs_aktif=excluded.frs_aktif, tanggal_mulai=excluded.tanggal_mulai, tanggal_selesai=excluded.tanggal_selesai"
                : "INSERT INTO settings (id, semester_aktif, frs_aktif, tanggal_mulai, tanggal_selesai) VALUES (1,?,?,?,?) " +
                  "ON DUPLICATE KEY UPDATE semester_aktif=VALUES(semester_aktif), frs_aktif=VALUES(frs_aktif), tanggal_mulai=VALUES(tanggal_mulai), tanggal_selesai=VALUES(tanggal_selesai)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, s.getSemesterAktif());
            ps.setBoolean(2, s.isFrsAktif());
            if (s.getTanggalMulai()==null) ps.setNull(3, Types.TIMESTAMP); else ps.setTimestamp(3, Timestamp.valueOf(s.getTanggalMulai()));
            if (s.getTanggalSelesai()==null) ps.setNull(4, Types.TIMESTAMP); else ps.setTimestamp(4, Timestamp.valueOf(s.getTanggalSelesai()));
            ps.executeUpdate();
        }
    }

    public boolean isWithinWindow() throws SQLException {
        Settings s = get();
        LocalDateTime now = LocalDateTime.now();
        if (!s.isFrsAktif()) return false;
        if (s.getTanggalMulai() != null && now.isBefore(s.getTanggalMulai())) return false;
        if (s.getTanggalSelesai() != null && now.isAfter(s.getTanggalSelesai())) return false;
        return true;
    }
}
