package id.ac.kampus.frs.dao;

import id.ac.kampus.frs.model.Jadwal;
import id.ac.kampus.frs.util.DBConnection;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class JadwalDAO {
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
                    j.setJamMulai(rs.getTime("jam_mulai").toLocalTime());
                    j.setJamSelesai(rs.getTime("jam_selesai").toLocalTime());
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
                    LocalTime aStart = rs.getTime(2).toLocalTime();
                    LocalTime aEnd = rs.getTime(3).toLocalTime();
                    LocalTime bStart = rs.getTime(4).toLocalTime();
                    LocalTime bEnd = rs.getTime(5).toLocalTime();
                    boolean overlap = !aEnd.isBefore(bStart) && !bEnd.isBefore(aStart);
                    if (overlap) return true;
                }
            }
        }
        return false;
    }
}
