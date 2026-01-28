/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pkkp;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author joni
 */
public class transNilai extends javax.swing.JFrame {
    Connection Con;
    Statement stm;
    ResultSet RsNilai;
    Boolean edit = false;

    private boolean isEditing = false;
    private Object[][] dataTable = null;
//    private final String[] header = {"Kode", "Nama Peserta", "kabko", "IPK", "Nilai Tulis", "%", "Nilai Wawancara", "%", "Nilai Akhir"};

    public transNilai() {
        initComponents();
        open_db();
        baca_data();
        aktif(false);
        setTombol(true);
        setupComboBox();
    }
    
    private void open_db() {
        try {
            KoneksiMysql kon = new KoneksiMysql("localhost", "root", "", "pkkp");
            Con = kon.getConnection();
            //System.out.println("Berhasil ");
        } catch (Exception e) {
            System.out.println("Error : " + e);
        }
    }
    
    private String getNm(int id, String table, String nm) {
//        String nama = "";
//        try {
//            stm = Con.createStatement();
//            ResultSet sql = stm.executeQuery("select " + nm + " as nama_kota from " + table + " where id=" + id);
//            if (sql.next()) {
//                nama = sql.getString("nama_kota");
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, e);
//        }
//        return nama;

        String nama = "";
        String sql = "SELECT " + nm + " AS nama_output FROM `" + table + "` WHERE id = " + id;

        // Gunakan try-with-resources di sini juga
        try (Statement stmt = Con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                nama = rs.getString("nama_output");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            e.printStackTrace();
        }
        return nama;
    }
    
    private String getNamaKota(int id_kota) {
        return getNm(id_kota, "data_kota", "nama_kota");
    }
    
    public void setField() {
//        int row = tblNilai.getSelectedRow();
//        if (row != -1) {
//            txtKode.setText((String) tblNilai.getValueAt(row, 0));
//            txtNama.setText((String) tblNilai.getValueAt(row, 1));
//            txtKota.setText((String) tblNilai.getValueAt(row, 2));
//            Double ipk = (Double) tblNilai.getValueAt(row, 3);
//            txtIpk.setText(String.valueOf(ipk));
//            txtNilTul.setText(tblNilai.getValueAt(row, 4).toString());
//            txtNilTulPer.setText(tblNilai.getValueAt(row, 5).toString());
//            txtNilWa.setText(tblNilai.getValueAt(row, 6).toString());
//            txtNilWaPer.setText(tblNilai.getValueAt(row, 7).toString());
//            NilaiAkhir();
//        }

        int row = tblNilai.getSelectedRow();
        if (row != -1) {
           // Helper untuk mengambil data dari tabel dan menangani nilai NULL
           // Fungsi 'getValue' ini mencegah error jika cell di tabel kosong (null)
           java.util.function.Function<Integer, String> getValue = col -> {
               Object val = tblNilai.getValueAt(row, col);
               return val == null ? "" : val.toString();
           };

           txtKode.setText(getValue.apply(0));
           txtNama.setText(getValue.apply(1));
           txtKota.setText(getValue.apply(2));
           txtIpk.setText(getValue.apply(3));
           txtNilTul.setText(getValue.apply(4));
           txtNilTulPer.setText(getValue.apply(5));
           txtNilWa.setText(getValue.apply(6));
           txtNilWaPer.setText(getValue.apply(7));
           txtNilAkh.setText(getValue.apply(8));
       }
    }
    
    public void baca_data() {
//        try {
//            stm = Con.createStatement();
//            RsNilai = stm.executeQuery("SELECT * FROM nilai");
//            DefaultTableModel dtm = (DefaultTableModel) tblNilai.getModel();
//            dtm.setRowCount(0);
//            while (RsNilai.next()) {
//                Object[] row = new Object[header.length];
//                for (int i = 0; i < header.length; i++) {
//                    row[i] = RsNilai.getObject(i + 1);
//                }
//                dtm.addRow(row);
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, e);
//        }

        // Sesuaikan header ini dengan semua data yang ingin ditampilkan
        String[] header = {"No. PKKP", "Nama", "Kab/Kota", "IPK", "N. Tulis", "% Tulis", "N. Wawancara", "% Wawan.", "N. Akhir"};
        DefaultTableModel dtm = new DefaultTableModel(null, header);
        tblNilai.setModel(dtm); // Set model baru ke JTable

        // Query sekarang sangat sederhana, hanya SELECT dari tabel nilai
        String sql = "SELECT no_pkkp, nama, kabko, ipk, nilai_tulis, niltuper, nilai_wawancara, nilwaper, nilai_akhir FROM `nilai`";

        try (Statement stmt = Con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Object[] rowData = {
                    rs.getString("no_pkkp"),
                    rs.getString("nama"),
                    rs.getString("kabko"),
                    rs.getObject("ipk"), // getObject agar bisa handle NULL
                    rs.getObject("nilai_tulis"),
                    rs.getObject("niltuper"),
                    rs.getObject("nilai_wawancara"),
                    rs.getObject("nilwaper"),
                    rs.getObject("nilai_akhir")
                };
                dtm.addRow(rowData);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void setupComboBox() {
//        cmbKode.removeAllItems();
//        cmbKode.addItem("Pilih Peserta");
//        try (ResultSet rs = stm.executeQuery("SELECT no_pkkp FROM administrasi")) {
//            while (rs.next()) {
//                cmbKode.addItem(rs.getString("no_pkkp"));
//            }
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error loading peserta: " + e.getMessage());
//        }
//
//        cmbKode.addActionListener(e -> {
//            String selectedItem = (String) cmbKode.getSelectedItem();
//            if (!"Pilih Peserta".equals(selectedItem)) {
//                pilih(selectedItem);
//            }
//        });

cmbKode.removeAllItems();
    cmbKode.addItem("Pilih Peserta");

    // Query untuk mengambil semua nomor peserta yang ada
    String sql = "SELECT no_pkkp FROM data_peserta";

    // Gunakan try-with-resources untuk membuat Statement dan ResultSet
    // Ini akan otomatis menutup resource setelah selesai, sangat aman.
        try (Statement stmt = Con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                cmbKode.addItem(rs.getString("no_pkkp"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error memuat data peserta ke ComboBox: " + e.getMessage());
            e.printStackTrace();
        } catch (NullPointerException e) {
            // Ini untuk menangkap jika 'Con' (koneksi) itu sendiri yang null
            JOptionPane.showMessageDialog(null, "Koneksi ke database gagal. Pastikan database aktif.");
            e.printStackTrace();
        }

        // Listener ini sudah benar, tidak perlu diubah
        cmbKode.addActionListener(e -> {
            if (cmbKode.getSelectedIndex() > 0) { // Cek berdasarkan index lebih aman
                String selectedItem = (String) cmbKode.getSelectedItem();
                pilih(selectedItem);
            } else {
                // Jika memilih "Pilih Peserta", kosongkan form
                kosong();
            }
        });
    }
    
    private void pilih(String kodePeserta) {
        String query = "SELECT * FROM data_peserta WHERE no_pkkp = ?";
        try (PreparedStatement pstmt = Con.prepareStatement(query)) {
            pstmt.setString(1, kodePeserta);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    txtKode.setText(rs.getString("no_pkkp"));
                    txtNama.setText(rs.getString("nama"));
                    String namaKota = getNamaKota(rs.getInt("id_kota"));
                    txtKota.setText(namaKota);
                    txtIpk.setText(String.valueOf(rs.getDouble("ipk")));
                    isEditing = true;
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error mengambil data: " + ex.getMessage());
        }
    }
    
    private void NilaiAkhir() {
        try {
            double nilaiTulis = Double.parseDouble(txtNilTul.getText());
            double nilaiWawancara = Double.parseDouble(txtNilWa.getText());
            double persentaseTulis = Integer.parseInt(txtNilTulPer.getText()) / 100.0;
            double persentaseWawancara = Integer.parseInt(txtNilWaPer.getText()) / 100.0;

            // Pastikan jumlah persentase adalah 1 (100%)
            if ((persentaseTulis + persentaseWawancara) != 1.0) {
                throw new IllegalArgumentException("Jumlah persentase harus 100%");
            }

            double nilaiAkhir = (nilaiTulis * persentaseTulis) + (nilaiWawancara * persentaseWawancara);
            txtNilAkh.setText(String.valueOf(nilaiAkhir));
        } catch (NumberFormatException e) {
            // Handle error jika nilai tulis, wawancara, atau persentase bukan angka
            txtNilAkh.setText("Input tidak valid");
        } catch (IllegalArgumentException e) {
            // Handle error jika persentase tidak valid
            txtNilAkh.setText(e.getMessage());
        }
    }
    

    public void kosong() {
        txtKode.setText("");
        txtNama.setText("");
        txtKota.setText("");
        txtIpk.setText("");
        txtNilTul.setText("");
        txtNilWa.setText("");
        txtNilAkh.setText("");
    }
    
    private void aktif(boolean x) {
        txtKode.setEditable(x);
        txtNama.setEditable(x);
        txtKota.setEditable(x);
        txtIpk.setEditable(x);
        txtNilTul.setEditable(x);
        txtNilWa.setEditable(x);
    }

    private void setTombol(boolean t) {
        cmdTambah.setEnabled(t);
        cmdBatal.setEnabled(!t);
        cmdSimpan.setEnabled(!t);
        cmdHapus.setEnabled(t);
        cmdKeluar.setEnabled(t);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel8 = new javax.swing.JLabel();
        cmdSimpan = new javax.swing.JButton();
        cmbKode = new javax.swing.JComboBox<>();
        cmdKeluar = new javax.swing.JButton();
        txtNama = new javax.swing.JTextField();
        cmdBatal = new javax.swing.JButton();
        txtIpk = new javax.swing.JTextField();
        cmdHapus = new javax.swing.JButton();
        txtNilTul = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtNilWa = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtKode = new javax.swing.JTextField();
        txtNilAkh = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtKota = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblNilai = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cmdTambah = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmdKoreksi = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtNilTulPer = new javax.swing.JTextField();
        txtNilWaPer = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel8.setText("Kab/Kota");

        cmdSimpan.setText("Simpan");
        cmdSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSimpanActionPerformed(evt);
            }
        });

        cmbKode.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));
        cmbKode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbKodeActionPerformed(evt);
            }
        });

        cmdKeluar.setText("Keluar");
        cmdKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdKeluarActionPerformed(evt);
            }
        });

        cmdBatal.setText("Batal");
        cmdBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBatalActionPerformed(evt);
            }
        });

        cmdHapus.setText("Hapus");
        cmdHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdHapusActionPerformed(evt);
            }
        });

        txtNilTul.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNilTulKeyReleased(evt);
            }
        });

        jLabel9.setText("Pilih Data");

        jLabel2.setText("Kode Peserta");

        txtNilWa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNilWaKeyReleased(evt);
            }
        });

        jLabel3.setText("Nama Peserta");

        jLabel4.setText("IPK");

        jLabel5.setText("Nilai Tulis");

        tblNilai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Kode", "Nama Peserta", "Kab/Kota", "IPK", "Nilai Tulis", "%", "Nilai Wawancara", "%", "Nilai Akhir"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblNilai.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                tblNilaiAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        tblNilai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNilaiMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblNilai);

        jLabel6.setText("Nilai Wawancara");

        jLabel7.setText("Nilai Akhir");

        cmdTambah.setText("Tambah");
        cmdTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdTambahActionPerformed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(102, 102, 102));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("sansserif", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Penilaian Peserta PKKP");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(241, 241, 241)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        cmdKoreksi.setText("Koreksi");
        cmdKoreksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdKoreksiActionPerformed(evt);
            }
        });

        jLabel10.setText("%");

        jLabel11.setText("%");

        txtNilTulPer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNilTulPerKeyReleased(evt);
            }
        });

        txtNilWaPer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNilWaPerKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel9))
                        .addGap(46, 46, 46)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtNilTul, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel11)
                                .addGap(18, 18, 18)
                                .addComponent(txtNilTulPer, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cmbKode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtKode, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txtNilAkh, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                            .addComponent(txtNilWa, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(jLabel10)
                                            .addGap(18, 18, 18)
                                            .addComponent(txtNilWaPer, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(102, 102, 102)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel8)
                                            .addComponent(jLabel4))
                                        .addGap(45, 45, 45)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtIpk, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtKota)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addComponent(cmdSimpan)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(cmdHapus))
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addComponent(cmdTambah)
                                                .addGap(18, 18, 18)
                                                .addComponent(cmdKoreksi)))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cmdBatal)
                                            .addComponent(cmdKeluar)))))))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 734, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbKode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtKode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtKota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtIpk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtNilTul, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(txtNilTulPer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel6)
                                .addComponent(txtNilWa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel10))
                            .addComponent(txtNilWaPer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNilAkh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addGap(16, 16, 16)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmdTambah)
                            .addComponent(cmdKoreksi)
                            .addComponent(cmdBatal))
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmdSimpan)
                            .addComponent(cmdHapus)
                            .addComponent(cmdKeluar))))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSimpanActionPerformed
//        String tKode = txtKode.getText();
//        String tNama = txtNama.getText();
//        double nilaiIPK = Double.parseDouble(txtIpk.getText());
//        String tProv = txtKota.getText();
//
//        int tTul = Integer.parseInt(txtNilTul.getText());
//        int tTulP = Integer.parseInt(txtNilTulPer.getText());
//        int tWan = Integer.parseInt(txtNilWa.getText());
//        int tWanP = Integer.parseInt(txtNilWaPer.getText());
//        double tAkh = Double.parseDouble(txtNilAkh.getText());
//        try {
//            if (edit) {
//                stm.executeUpdate("UPDATE nilai SET nama='" + tNama + "', kabko='" + tProv + "', ipk='" + nilaiIPK + "', nilai_tulis='" + tTul + "', niltulper='" + tTulP + "', nilai_wawancara='" + tWan + "', nilwaper='" + tWanP + "', nilai_akhir='" + tAkh + "' WHERE no_pkkp='" + tKode + "'");
//            } else {
//                stm.executeUpdate("INSERT INTO nilai VALUES('" + tKode + "', '" + tNama + "', '" + tProv + "', '" + nilaiIPK + "', '" + tTul + "', '" + tTulP + "', '" + tWan + "', '" + tWanP + "', '" + tAkh + "')");
//            }
//            baca_data();
//            kosong();
//            aktif(false);
//            setTombol(true);
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
//        }

        String tKode = txtKode.getText();
        String tNama = txtNama.getText();
        String tKota = txtKota.getText();
        double tIpk = Double.parseDouble(txtIpk.getText());
        double tTul = Double.parseDouble(txtNilTul.getText());
        double tTulP = Double.parseDouble(txtNilTulPer.getText());
        double tWan = Double.parseDouble(txtNilWa.getText());
        double tWanP = Double.parseDouble(txtNilWaPer.getText());
        double tAkh = Double.parseDouble(txtNilAkh.getText());

        try {
            if (edit) { // Mode UPDATE
                String sql = "UPDATE `nilai` SET `nama`=?, `kabko`=?, `ipk`=?, `nilai_tulis`=?, `niltuper`=?, `nilai_wawancara`=?, `nilwaper`=?, `nilai_akhir`=? WHERE `no_pkkp`=?";
                try (PreparedStatement pstmt = Con.prepareStatement(sql)) {
                    pstmt.setString(1, tNama);
                    pstmt.setString(2, tKota);
                    pstmt.setDouble(3, tIpk);
                    pstmt.setDouble(4, tTul);
                    pstmt.setDouble(5, tTulP);
                    pstmt.setDouble(6, tWan);
                    pstmt.setDouble(7, tWanP);
                    pstmt.setDouble(8, tAkh);
                    pstmt.setString(9, tKode); // no_pkkp untuk klausa WHERE
                    pstmt.executeUpdate();
                }
            } else { // Mode INSERT
                // Perhatikan urutan kolom harus sama persis dengan urutan tanda tanya (?)
                String sql = "INSERT INTO `nilai` (no_pkkp, nama, kabko, ipk, nilai_tulis, niltuper, nilai_wawancara, nilwaper, nilai_akhir) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt = Con.prepareStatement(sql)) {
                    pstmt.setString(1, tKode);
                    pstmt.setString(2, tNama);
                    pstmt.setString(3, tKota);
                    pstmt.setDouble(4, tIpk);
                    pstmt.setDouble(5, tTul);
                    pstmt.setDouble(6, tTulP);
                    pstmt.setDouble(7, tWan);
                    pstmt.setDouble(8, tWanP);
                    pstmt.setDouble(9, tAkh);
                    pstmt.executeUpdate();
                }
            }
            baca_data(); // Muat ulang data ke tabel
            kosong();
            aktif(false);
            setTombol(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error saat menyimpan: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Pastikan semua field nilai terisi angka yang benar.");
        }
    }//GEN-LAST:event_cmdSimpanActionPerformed

    private void cmbKodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbKodeActionPerformed

    }//GEN-LAST:event_cmbKodeActionPerformed

    private void cmdKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdKeluarActionPerformed
        dispose();
    }//GEN-LAST:event_cmdKeluarActionPerformed

    private void cmdHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdHapusActionPerformed
        try{
            String sql="delete from nilai where no_pkkp='" + txtKode.getText()+ "'";
            stm.executeUpdate(sql);
            baca_data();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_cmdHapusActionPerformed

    private void txtNilTulKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNilTulKeyReleased
        NilaiAkhir();
    }//GEN-LAST:event_txtNilTulKeyReleased

    private void txtNilWaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNilWaKeyReleased
        NilaiAkhir();
    }//GEN-LAST:event_txtNilWaKeyReleased

    private void tblNilaiAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tblNilaiAncestorAdded
        setField();
    }//GEN-LAST:event_tblNilaiAncestorAdded

    private void cmdTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTambahActionPerformed
        aktif(true);
        setTombol(false);
        kosong();
        edit = false;
    }//GEN-LAST:event_cmdTambahActionPerformed

    private void tblNilaiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNilaiMouseClicked
        setField();
    }//GEN-LAST:event_tblNilaiMouseClicked

    private void cmdBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBatalActionPerformed
        aktif(false);
        setTombol(true);
    }//GEN-LAST:event_cmdBatalActionPerformed

    private void cmdKoreksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdKoreksiActionPerformed
        edit=true;
        aktif(true);
        setTombol(false);
        txtKode.setEditable(false);
        txtNama.setEditable(false);
        txtKota.setEditable(false);
        txtIpk.setEditable(false);
    }//GEN-LAST:event_cmdKoreksiActionPerformed

    private void txtNilTulPerKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNilTulPerKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNilTulPerKeyReleased

    private void txtNilWaPerKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNilWaPerKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNilWaPerKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(transNilai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(transNilai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(transNilai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(transNilai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new transNilai().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbKode;
    private javax.swing.JButton cmdBatal;
    private javax.swing.JButton cmdHapus;
    private javax.swing.JButton cmdKeluar;
    private javax.swing.JButton cmdKoreksi;
    private javax.swing.JButton cmdSimpan;
    private javax.swing.JButton cmdTambah;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblNilai;
    private javax.swing.JTextField txtIpk;
    private javax.swing.JTextField txtKode;
    private javax.swing.JTextField txtKota;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtNilAkh;
    private javax.swing.JTextField txtNilTul;
    private javax.swing.JTextField txtNilTulPer;
    private javax.swing.JTextField txtNilWa;
    private javax.swing.JTextField txtNilWaPer;
    // End of variables declaration//GEN-END:variables
}
