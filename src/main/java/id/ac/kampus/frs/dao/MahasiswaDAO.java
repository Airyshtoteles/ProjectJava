package id.ac.kampus.frs.dao;

import id.ac.kampus.frs.model.Mahasiswa;
import id.ac.kampus.frs.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MahasiswaDAO {
    public List<Mahasiswa> listAll() throws SQLException {
        String sql = "SELECT nim, nama, jurusan, semester, id_user, nidn_wali FROM mahasiswa ORDER BY nim";
        List<Mahasiswa> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Mahasiswa m = new Mahasiswa();
                m.setNim(rs.getString("nim"));
                m.setNama(rs.getString("nama"));
                m.setJurusan(rs.getString("jurusan"));
                m.setSemester(rs.getInt("semester"));
                m.setIdUser(rs.getInt("id_user"));
                // nidn_wali is not in model; ignoring for now or can extend model
                list.add(m);
            }
        }
        return list;
    }

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

    public List<Mahasiswa> listByWali(String nidnWali) throws SQLException {
        String sql = "SELECT nim, nama, jurusan, semester, id_user FROM mahasiswa WHERE nidn_wali = ? ORDER BY nama";
        List<Mahasiswa> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nidnWali);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Mahasiswa m = new Mahasiswa();
                    m.setNim(rs.getString("nim"));
                    m.setNama(rs.getString("nama"));
                    m.setJurusan(rs.getString("jurusan"));
                    m.setSemester(rs.getInt("semester"));
                    m.setIdUser(rs.getInt("id_user"));
                    list.add(m);
                }
            }
        }
        return list;
    }

    public void insert(Mahasiswa m, String nidnWali) throws SQLException {
        String sql = "INSERT INTO mahasiswa (nim, nama, jurusan, semester, id_user, nidn_wali) VALUES (?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getNim());
            ps.setString(2, m.getNama());
            ps.setString(3, m.getJurusan());
            ps.setInt(4, m.getSemester());
            ps.setInt(5, m.getIdUser());
            ps.setString(6, nidnWali);
            ps.executeUpdate();
        }
    }

    public void update(Mahasiswa m, String nidnWali) throws SQLException {
        String sql = "UPDATE mahasiswa SET nama=?, jurusan=?, semester=?, id_user=?, nidn_wali=? WHERE nim=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getNama());
            ps.setString(2, m.getJurusan());
            ps.setInt(3, m.getSemester());
            ps.setInt(4, m.getIdUser());
            ps.setString(5, nidnWali);
            ps.setString(6, m.getNim());
            ps.executeUpdate();
        }
    }

    public void delete(String nim) throws SQLException {
        String sql = "DELETE FROM mahasiswa WHERE nim=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nim);
            ps.executeUpdate();
        }
    }
}
