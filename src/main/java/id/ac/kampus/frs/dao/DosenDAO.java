package id.ac.kampus.frs.dao;

import id.ac.kampus.frs.model.Dosen;
import id.ac.kampus.frs.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DosenDAO {
    public List<Dosen> listAll() throws SQLException {
        String sql = "SELECT nidn, nama, id_user FROM dosen ORDER BY nama";
        List<Dosen> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Dosen d = new Dosen();
                d.setNidn(rs.getString("nidn"));
                d.setNama(rs.getString("nama"));
                d.setIdUser(rs.getInt("id_user"));
                list.add(d);
            }
        }
        return list;
    }

    public void insert(Dosen d) throws SQLException {
        String sql = "INSERT INTO dosen (nidn, nama, id_user) VALUES (?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, d.getNidn());
            ps.setString(2, d.getNama());
            ps.setInt(3, d.getIdUser());
            ps.executeUpdate();
        }
    }

    public void update(Dosen d) throws SQLException {
        String sql = "UPDATE dosen SET nama=?, id_user=? WHERE nidn=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, d.getNama());
            ps.setInt(2, d.getIdUser());
            ps.setString(3, d.getNidn());
            ps.executeUpdate();
        }
    }

    public void delete(String nidn) throws SQLException {
        String sql = "DELETE FROM dosen WHERE nidn=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nidn);
            ps.executeUpdate();
        }
    }
}
