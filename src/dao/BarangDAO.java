package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Barang;
import view.Koneksi;

public class BarangDAO {

    // SIMPAN DATA
    public boolean insert(Barang barang) {
        String sql = "INSERT INTO barang VALUES (?, ?, ?, ?)";

        try (Connection conn = Koneksi.getConnection()) {
            if (conn == null) return false;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, barang.getKodeBarang());
                ps.setString(2, barang.getNamaBarang());
                ps.setString(3, barang.getKategori());
                ps.setInt(4, barang.getStok());

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("Gagal menyimpan: " + e.getMessage());
            return false;
        }
    }

    // TAMPILKAN DATA
    public List<Barang> getAll() {
        List<Barang> data = new ArrayList<>();

        String sql = "SELECT * FROM barang";

        try (Connection conn = Koneksi.getConnection()) {
            if (conn == null) return data;

            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {

                while (rs.next()) {
                    Barang b = new Barang(
                        rs.getString("kode_barang"),
                        rs.getString("nama_barang"),
                        rs.getString("kategori"),
                        rs.getInt("stok")
                    );

                    data.add(b);
                }
            }

        } catch (SQLException e) {
            System.out.println("Gagal mengambil data: " + e.getMessage());
        }

        return data;
    }

    // UBAH DATA
    public boolean update(Barang barang) {
        String sql = "UPDATE barang SET nama_barang=?, "
                   + "kategori=?, stok=? WHERE kode_barang=?";

        try (Connection conn = Koneksi.getConnection()) {
            if (conn == null) return false;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, barang.getNamaBarang());
                ps.setString(2, barang.getKategori());
                ps.setInt(3, barang.getStok());
                ps.setString(4, barang.getKodeBarang());

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("Gagal mengubah: " + e.getMessage());
            return false;
        }
    }

    // HAPUS DATA
    public boolean delete(String kodeBarang) {
        String sql = "DELETE FROM barang WHERE kode_barang=?";

        try (Connection conn = Koneksi.getConnection()) {
            if (conn == null) return false;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, kodeBarang);

                return ps.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("Gagal menghapus: " + e.getMessage());
            return false;
        }
    }
}