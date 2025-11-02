package id.ac.kampus.frs.dao;

import id.ac.kampus.frs.model.Jadwal;
import id.ac.kampus.frs.util.DBConnection;

import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class JadwalDAO {
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("H:mm[:ss]");

    private static LocalTime parseTime(String s) {
        if (s == null) return null;
        s = s.trim();
        return LocalTime.parse(s, TIME_FMT);
    }
    public List<Jadwal> listByKodeMk(String kodeMk) throws SQLException {
        String sql = "SELECT id_jadwal, kode_mk, hari, jam_mulai, jam_selesai, ruang FROM jadwal WHERE kode_mk=?";
        List<Jadwal> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, kodeMk);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Jadwal j = new Jadwal();
                    j.setIdJadwal(rs.getInt("id_jadwal"));
                    j.setKodeMk(rs.getString("kode_mk"));
                    j.setHari(rs.getString("hari"));
                    j.setJamMulai(parseTime(rs.getString("jam_mulai")));
                    j.setJamSelesai(parseTime(rs.getString("jam_selesai")));
                    j.setRuang(rs.getString("ruang"));
                    list.add(j);
                }
            }
        }
        return list;
    }

    public boolean isBentrok(String kodeMkA, String kodeMkB) throws SQLException {
        // Simple overlap check: same day and time intervals overlap
        String sql = "SELECT a.hari, a.jam_mulai, a.jam_selesai, b.jam_mulai, b.jam_selesai " +
                "FROM jadwal a JOIN jadwal b ON a.hari=b.hari " +
                "WHERE a.kode_mk=? AND b.kode_mk=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, kodeMkA);
            ps.setString(2, kodeMkB);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalTime aStart = parseTime(rs.getString(2));
                    LocalTime aEnd = parseTime(rs.getString(3));
                    LocalTime bStart = parseTime(rs.getString(4));
                    LocalTime bEnd = parseTime(rs.getString(5));
                    boolean overlap = !aEnd.isBefore(bStart) && !bEnd.isBefore(aStart);
                    if (overlap) return true;
                }
            }
        }
        return false;
    }
}
