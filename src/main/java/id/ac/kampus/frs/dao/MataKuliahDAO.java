package id.ac.kampus.frs.dao;

import id.ac.kampus.frs.model.MataKuliah;
import id.ac.kampus.frs.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MataKuliahDAO {
    public List<MataKuliah> listBySemester(int semester) throws SQLException {
        String sql = "SELECT kode_mk, nama_mk, sks, semester, prasyarat FROM mata_kuliah WHERE semester = ? ORDER BY kode_mk";
        List<MataKuliah> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, semester);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MataKuliah mk = new MataKuliah();
                    mk.setKodeMk(rs.getString("kode_mk"));
                    mk.setNamaMk(rs.getString("nama_mk"));
                    mk.setSks(rs.getInt("sks"));
                    mk.setSemester(rs.getInt("semester"));
                    mk.setPrasyarat(rs.getString("prasyarat"));
                    list.add(mk);
                }
            }
        }
        return list;
    }

    public MataKuliah findByKode(String kode) throws SQLException {
        String sql = "SELECT kode_mk, nama_mk, sks, semester, prasyarat FROM mata_kuliah WHERE kode_mk = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, kode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    MataKuliah mk = new MataKuliah();
                    mk.setKodeMk(rs.getString("kode_mk"));
                    mk.setNamaMk(rs.getString("nama_mk"));
                    mk.setSks(rs.getInt("sks"));
                    mk.setSemester(rs.getInt("semester"));
                    mk.setPrasyarat(rs.getString("prasyarat"));
                    return mk;
                }
            }
        }
        return null;
    }
}
