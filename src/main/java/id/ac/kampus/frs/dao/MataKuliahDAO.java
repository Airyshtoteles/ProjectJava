package id.ac.kampus.frs.dao;

import id.ac.kampus.frs.model.MataKuliah;
import id.ac.kampus.frs.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MataKuliahDAO {
    public List<MataKuliah> listAll() throws SQLException {
        String sql = "SELECT kode_mk, nama_mk, sks, semester, prasyarat FROM mata_kuliah ORDER BY kode_mk";
        List<MataKuliah> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
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
        return list;
    }

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

    public void insert(MataKuliah mk) throws SQLException {
        String sql = "INSERT INTO mata_kuliah (kode_mk, nama_mk, sks, semester, prasyarat) VALUES (?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, mk.getKodeMk());
            ps.setString(2, mk.getNamaMk());
            ps.setInt(3, mk.getSks());
            ps.setInt(4, mk.getSemester());
            if (mk.getPrasyarat() == null || mk.getPrasyarat().isEmpty()) ps.setNull(5, Types.VARCHAR); else ps.setString(5, mk.getPrasyarat());
            ps.executeUpdate();
        }
    }

    public void update(MataKuliah mk) throws SQLException {
        String sql = "UPDATE mata_kuliah SET nama_mk=?, sks=?, semester=?, prasyarat=? WHERE kode_mk=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, mk.getNamaMk());
            ps.setInt(2, mk.getSks());
            ps.setInt(3, mk.getSemester());
            if (mk.getPrasyarat() == null || mk.getPrasyarat().isEmpty()) ps.setNull(4, Types.VARCHAR); else ps.setString(4, mk.getPrasyarat());
            ps.setString(5, mk.getKodeMk());
            ps.executeUpdate();
        }
    }

    public void delete(String kode) throws SQLException {
        String sql = "DELETE FROM mata_kuliah WHERE kode_mk=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, kode);
            ps.executeUpdate();
        }
    }
}
