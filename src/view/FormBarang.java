package view;

import dao.BarangDAO;
import model.Barang;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FormBarang extends JFrame {

    private JTextField txtKode, txtNama, txtStok;
    private JComboBox<String> cmbKategori;
    private JTable tabel;
    private DefaultTableModel model;

    private BarangDAO dao = new BarangDAO();

    public FormBarang() {
        setTitle("Aplikasi Inventori Barang Laboratorium");
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buatForm();
        tampilData();
    }

    private void buatForm() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel judul = new JLabel(
                "PENGELOLAAN DATA BARANG LABORATORIUM",
                JLabel.CENTER
        );

        judul.setFont(new Font("Arial", Font.BOLD, 18));

        panel.add(judul, BorderLayout.NORTH);

        // PANEL INPUT
        JPanel input = new JPanel(new GridLayout(4, 2, 10, 10));

        txtKode = new JTextField();
        txtNama = new JTextField();
        txtStok = new JTextField();

        cmbKategori = new JComboBox<>(new String[]{
            "Komputer",
            "Jaringan",
            "Alat Ukur"
        });

        input.add(new JLabel("Kode Barang"));
        input.add(txtKode);

        input.add(new JLabel("Nama Barang"));
        input.add(txtNama);

        input.add(new JLabel("Kategori"));
        input.add(cmbKategori);

        input.add(new JLabel("Stok"));
        input.add(txtStok);

        // PANEL TOMBOL
        JButton btnSimpan = new JButton("Simpan");
        JButton btnUbah = new JButton("Ubah");
        JButton btnHapus = new JButton("Hapus");
        JButton btnBersihkan = new JButton("Bersihkan");

        JPanel tombol = new JPanel(new FlowLayout());

        tombol.add(btnSimpan);
        tombol.add(btnUbah);
        tombol.add(btnHapus);
        tombol.add(btnBersihkan);

        JPanel bagianAtas = new JPanel(new BorderLayout(10, 10));
        bagianAtas.add(input, BorderLayout.CENTER);
        bagianAtas.add(tombol, BorderLayout.SOUTH);

        panel.add(bagianAtas, BorderLayout.CENTER);

        // TABEL DATA
        model = new DefaultTableModel(
                new String[]{
                    "Kode Barang",
                    "Nama Barang",
                    "Kategori",
                    "Stok"
                }, 0
        );

        tabel = new JTable(model);
        JScrollPane scroll = new JScrollPane(tabel);

        panel.add(scroll, BorderLayout.SOUTH);

        add(panel);

        // EVENT TOMBOL SIMPAN
        btnSimpan.addActionListener(e -> simpanData());

        // EVENT TOMBOL UBAH
        btnUbah.addActionListener(e -> ubahData());

        // EVENT TOMBOL HAPUS
        btnHapus.addActionListener(e -> hapusData());

        // EVENT TOMBOL BERSIHKAN
        btnBersihkan.addActionListener(e -> bersihkan());

        // EVENT KLIK TABEL
        tabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int baris = tabel.getSelectedRow();

                if (baris != -1) {
                    txtKode.setText(model.getValueAt(baris, 0).toString());
                    txtNama.setText(model.getValueAt(baris, 1).toString());
                    cmbKategori.setSelectedItem(
                            model.getValueAt(baris, 2).toString()
                    );
                    txtStok.setText(model.getValueAt(baris, 3).toString());
                }
            }
        });
    }

    // VALIDASI INPUT
    private Barang ambilInput() {

        String kode = txtKode.getText().trim();
        String nama = txtNama.getText().trim();
        String kategori = cmbKategori.getSelectedItem().toString();
        String stokText = txtStok.getText().trim();

        if (kode.isEmpty() || nama.isEmpty() || stokText.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Kode, nama, dan stok wajib diisi!"
            );
            return null;
        }

        if (kode.length() > 10) {
            JOptionPane.showMessageDialog(
                    this,
                    "Kode barang maksimal 10 karakter!"
            );
            return null;
        }

        if (nama.length() > 50) {
            JOptionPane.showMessageDialog(
                    this,
                    "Nama barang maksimal 50 karakter!"
            );
            return null;
        }

        try {
            int stok = Integer.parseInt(stokText);

            if (stok < 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Stok tidak boleh kurang dari 0!"
                );
                return null;
            }

            return new Barang(kode, nama, kategori, stok);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Stok harus berupa angka!"
            );
            return null;
        }
    }

    // SIMPAN DATA
    private void simpanData() {

        Barang b = ambilInput();

        if (b == null) return;

        if (dao.insert(b)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Data berhasil disimpan!"
            );

            tampilData();
            bersihkan();

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Data gagal disimpan! Periksa kode barang atau koneksi."
            );
        }
    }

    // TAMPILKAN DATA
    private void tampilData() {

        model.setRowCount(0);

        List<Barang> data = dao.getAll();

        for (Barang b : data) {
            model.addRow(new Object[]{
                b.getKodeBarang(),
                b.getNamaBarang(),
                b.getKategori(),
                b.getStok()
            });
        }
    }

    // UBAH DATA
    private void ubahData() {

        if (txtKode.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Pilih data yang akan diubah!"
            );
            return;
        }

        Barang b = ambilInput();

        if (b == null) return;

        int konfirmasi = JOptionPane.showConfirmDialog(
                this,
                "Apakah Anda yakin ingin mengubah data?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION
        );

        if (konfirmasi == JOptionPane.YES_OPTION) {

            if (dao.update(b)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Data berhasil diubah!"
                );

                tampilData();
                bersihkan();

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Data gagal diubah!"
                );
            }
        }
    }

    // HAPUS DATA
    private void hapusData() {

        String kode = txtKode.getText().trim();

        if (kode.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Pilih data yang akan dihapus!"
            );
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(
                this,
                "Yakin ingin menghapus barang dengan kode " + kode + "?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION
        );

        if (konfirmasi == JOptionPane.YES_OPTION) {

            if (dao.delete(kode)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Data berhasil dihapus!"
                );

                tampilData();
                bersihkan();

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Data gagal dihapus!"
                );
            }
        }
    }

    // BERSIHKAN FORM
    private void bersihkan() {

        txtKode.setText("");
        txtNama.setText("");
        txtStok.setText("");

        cmbKategori.setSelectedIndex(0);

        tabel.clearSelection();
        txtKode.requestFocus();
    }

}