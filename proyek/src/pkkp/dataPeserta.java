/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pkkp;
import java.sql.*;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author joni
 */
public class dataPeserta extends javax.swing.JFrame {
    Connection Con;
    ResultSet RsPes;
    Statement stm;
    Boolean ada = false;
    String sSKCK;
    String sSK;
    int iKab = 0;
    int iKec = 0;
    Boolean edit = false;
    private Object[][] dataTable = null;
    private String[] header = {"No PKKP", "Nama", "Kab/Kota", "Kecamatan", "Kelurahan", "Usia", "IPK", "Phone", "Email", "SK", "SKCK"};


    public dataPeserta() {
        initComponents();
        open_db();
        baca_data();
        aktif(false);
        setTombol(true);
        Sk();
        Skck();
        detail("data_kota", "nama_kota");
        detail("data_kec", "nama_kec");
        detail("data_kel", "nama_kel");
    }

    private void Sk() {
        cmbSK.removeAllItems();
        cmbSK.addItem("Ada");
        cmbSK.addItem("Tidak Ada");
    }
    
    private void Skck() {
        cmbSKCK.removeAllItems();
        cmbSKCK.addItem("Ada");
        cmbSKCK.addItem("Tidak Ada");
    }
    
    private void setField(){
        int row=tblPeserta.getSelectedRow();
        txtKode_pes.setText((String)tblPeserta.getValueAt(row,0));
        txtNama.setText((String)tblPeserta.getValueAt(row,1));
        cmdKabKo.setSelectedItem((String) tblPeserta.getValueAt(row, 2));
        cmdKec.setSelectedItem((String) tblPeserta.getValueAt(row, 3));
        cmdKel.setSelectedItem((String) tblPeserta.getValueAt(row, 4));
        String usia=Integer.toString((Integer)tblPeserta.getValueAt(row,5));
        txtUsia.setText(usia);
        String ipk=Double.toString((Double)tblPeserta.getValueAt(row,6));
        txtIPK.setText(ipk);
        txtTelp.setText((String)tblPeserta.getValueAt(row,7));
        txtEmail.setText((String)tblPeserta.getValueAt(row,8));
        cmbSK.setSelectedItem((String)tblPeserta.getValueAt(row,9));
        cmbSKCK.setSelectedItem((String)tblPeserta.getValueAt(row,10));
        
    }
    
    //method membuka database server, user, pass, database disesuaikan
    private void open_db(){ 
        try{
            KoneksiMysql kon = new KoneksiMysql ("localhost","root","","pkkp");
            Con = kon.getConnection();
            //System.out.println("Berhasil ");
        }catch (Exception e) {
            System.out.println("Error : "+e);
        }
    }
    
    //method baca data dari Mysql dimasukkan ke table pada form
    private void baca_data() {
        try {
            stm = Con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            RsPes = stm.executeQuery("select * from data_peserta");
            ResultSetMetaData meta = RsPes.getMetaData();
            int col = meta.getColumnCount();
            int baris = 0;
            RsPes.last();
            baris = RsPes.getRow(); 
            dataTable = new Object[baris][col];
            int x = 0;
            RsPes.beforeFirst();
            while (RsPes.next()) {
                dataTable[x][0] = RsPes.getString("no_pkkp");
                dataTable[x][1] = RsPes.getString("nama");
                dataTable[x][2] = getNm(RsPes.getInt("id_kota"), "data_kota", "nama_kota");
                dataTable[x][3] = getNm(RsPes.getInt("id_kec"), "data_kec", "nama_kec");
                dataTable[x][4] = getNm(RsPes.getInt("id_kel"), "data_kel", "nama_kel");
                dataTable[x][5] = RsPes.getInt("usia");
                dataTable[x][6] = RsPes.getDouble("ipk");
                dataTable[x][7] = RsPes.getString("phone");
                dataTable[x][8] = RsPes.getString("email");
                dataTable[x][9] = RsPes.getString("skk");
                dataTable[x][10] = RsPes.getString("skck");
                
                x++;
            }
            tblPeserta.setModel(new DefaultTableModel(dataTable, header));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    //untuk mengkosongkan isian data
    private void kosong() {
        txtKode_pes.setText("");
        txtNama.setText("");
        txtUsia.setText("");
        txtIPK.setText("");
        txtTelp.setText("");
        txtEmail.setText("");
    }
    //mengset aktif tidak isian data
    private void aktif(boolean x) {
        txtKode_pes.setEditable(x);
        txtNama.setEditable(x);
        cmdKabKo.setEnabled(x);
        cmdKec.setEnabled(x);
        cmdKel.setEnabled(x);
        txtUsia.setEditable(x);
        txtIPK.setEditable(x);
        txtTelp.setEditable(x);
        txtEmail.setEditable(x);
        cmbSK.setEnabled(x);
        cmbSKCK.setEnabled(x);
       
    }
    //mengset tombol on/off
    private void setTombol(boolean t) {
        cmdTambah.setEnabled(t);
        cmdKoreksi.setEnabled(t);
        cmdHapus.setEnabled(t);
        cmdSimpan.setEnabled(!t);
        cmdBatal.setEnabled(!t);
        cmdKeluar.setEnabled(t);
    }
    
    private void detail(String table, String nm) {
        try (ResultSet rs = stm.executeQuery("SELECT " + nm + " FROM " + table)) {
            if (table == "data_kota") {
                while (rs.next()) {
                    cmdKabKo.addItem(rs.getString(nm));
                }
            } else if (table == "data_kec") {
                while (rs.next()) {
                    cmdKec.addItem(rs.getString(nm));
                }
            } else if (table == "data_kel") {
                while (rs.next()) {
                    cmdKel.addItem(rs.getString(nm));
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading barang : " + e.getMessage());
        }
    }
    
    private void detailKabKo() {
        String sql = "SELECT nama_kec FROM data_kec WHERE id_kota=" + iKab;
        cmdKec.removeAllItems();
        try (ResultSet rs = stm.executeQuery(sql)) {
            while (rs.next()) {
                cmdKec.addItem(rs.getString("nama_kec"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading barang : " + e.getMessage());
        }
    }
    
    private void detailKec() {
        String sql = "SELECT nama_kel FROM data_kel WHERE id_kec=" + iKec;
        cmdKel.removeAllItems();
        try (ResultSet rs = stm.executeQuery(sql)) {
            while (rs.next()) {
                cmdKel.addItem(rs.getString("nama_kel"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading barang : " + e.getMessage());
        }
    }
    
    private String getNm(int id, String table, String nm) {
        String nama = "";
        try {
            stm = Con.createStatement();
            ResultSet sql = stm.executeQuery("select " + nm + " as nama_kota from " + table + " where id=" + id);
            if (sql.next()) {
                nama = sql.getString("nama_kota");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return nama;
    }
    
    private int getId(String nama, String table, String idT, String n) {
        int id = 0;
        try {
            stm = Con.createStatement();
            ResultSet sql = stm.executeQuery("select " + idT + " as id from " + table + " where " + n + "='" + nama + "'");
            if (sql.next()) {
                id = sql.getInt("id");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return id;
    }
    
    private int getIdKel(String nama, int n) {
        int id = 0;
        try {
            stm = Con.createStatement();
            ResultSet sql = stm.executeQuery("SELECT id FROM `data_kel` WHERE `nama_kel`='" + nama + "' AND `id_kec` =" + n);
            if (sql.next()) {
                id = sql.getInt("id");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return id;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPeserta = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtKode_pes = new javax.swing.JTextField();
        txtNama = new javax.swing.JTextField();
        txtUsia = new javax.swing.JTextField();
        txtTelp = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        cmbSK = new javax.swing.JComboBox<>();
        cmbSKCK = new javax.swing.JComboBox<>();
        txtIPK = new javax.swing.JTextField();
        cmdTambah = new javax.swing.JButton();
        cmdKoreksi = new javax.swing.JButton();
        cmdBatal = new javax.swing.JButton();
        cmdSimpan = new javax.swing.JButton();
        cmdHapus = new javax.swing.JButton();
        cmdKeluar = new javax.swing.JButton();
        cmdKabKo = new javax.swing.JComboBox<>();
        cmdKec = new javax.swing.JComboBox<>();
        cmdKel = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 153));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("sansserif", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Data Peserta");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(456, 456, 456))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jLabel2.setText("Nomor PKKP");

        jLabel5.setText("Nama Peserta");

        jLabel7.setText("Telepon");

        jLabel8.setText("Usia");

        jLabel9.setText("Email");

        tblPeserta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Kode", "Nama", "KTP Provinsi", "Pendidikan", "Usia", "Telepon", "Email", "SK", "SKCK", "IPK"
            }
        ));
        tblPeserta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPesertaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPeserta);

        jLabel12.setText("Surat Kesehatan (SK)");

        jLabel13.setText("Surat Keterangan Catatan Kepolisian (SKCK)");

        jLabel14.setText("Nilai IPK");

        txtKode_pes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKode_pesActionPerformed(evt);
            }
        });

        txtNama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNamaActionPerformed(evt);
            }
        });

        txtUsia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsiaActionPerformed(evt);
            }
        });

        txtTelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelpActionPerformed(evt);
            }
        });

        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });

        cmbSK.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih", "Ada", "Tidak Ada" }));
        cmbSK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSKActionPerformed(evt);
            }
        });

        cmbSKCK.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih", "Ada", "Tidak Ada" }));
        cmbSKCK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSKCKActionPerformed(evt);
            }
        });

        txtIPK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIPKActionPerformed(evt);
            }
        });

        cmdTambah.setText("Tambah");
        cmdTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdTambahActionPerformed(evt);
            }
        });

        cmdKoreksi.setText("Koreksi");
        cmdKoreksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdKoreksiActionPerformed(evt);
            }
        });

        cmdBatal.setText("Batal");
        cmdBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBatalActionPerformed(evt);
            }
        });

        cmdSimpan.setText("Simpan");
        cmdSimpan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cmdSimpanMouseClicked(evt);
            }
        });
        cmdSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSimpanActionPerformed(evt);
            }
        });

        cmdHapus.setText("Hapus");
        cmdHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdHapusActionPerformed(evt);
            }
        });

        cmdKeluar.setText("Keluar");
        cmdKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdKeluarActionPerformed(evt);
            }
        });

        cmdKabKo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Kab/Kota" }));
        cmdKabKo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmdKabKoItemStateChanged(evt);
            }
        });

        cmdKec.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Kecamatan" }));
        cmdKec.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmdKecItemStateChanged(evt);
            }
        });

        cmdKel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Kelurahan" }));

        jLabel15.setText("Kab/Kota");

        jLabel16.setText("Kecamatan");

        jLabel10.setText("Kelurahan");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel5))
                                .addGap(45, 45, 45)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtNama, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                                    .addComponent(txtKode_pes)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(45, 45, 45)
                                .addComponent(cmdKabKo, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cmdKel, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                                    .addGap(44, 44, 44)
                                    .addComponent(cmdKec, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel14))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtIPK)
                                        .addComponent(txtUsia, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)))))
                        .addGap(247, 247, 247)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel12))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbSK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmbSKCK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(121, 121, 121))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(cmdTambah)
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(cmdHapus)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(cmdKeluar))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(cmdKoreksi)
                                                .addGap(18, 18, 18)
                                                .addComponent(cmdBatal))))
                                    .addComponent(cmdSimpan)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                            .addComponent(jLabel9)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                            .addGap(122, 122, 122)
                                            .addComponent(txtTelp, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jLabel7))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1000, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtKode_pes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(cmbSK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(cmbSKCK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtTelp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmdTambah)
                            .addComponent(cmdKoreksi)
                            .addComponent(cmdBatal))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmdSimpan)
                            .addComponent(cmdHapus)
                            .addComponent(cmdKeluar)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(cmdKabKo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmdKec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmdKel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(txtIPK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(txtUsia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                .addGap(14, 14, 14))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtKode_pesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKode_pesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKode_pesActionPerformed

    private void txtNamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaActionPerformed

    private void txtUsiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsiaActionPerformed

    private void txtTelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelpActionPerformed

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailActionPerformed

    private void txtIPKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIPKActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIPKActionPerformed

    private void cmdTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTambahActionPerformed
        aktif(true);
        setTombol(false);
        kosong();
        edit=false;
    }//GEN-LAST:event_cmdTambahActionPerformed

    private void cmdKoreksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdKoreksiActionPerformed
        edit=true;
        aktif(true);
        setTombol(false);
        txtKode_pes.setEditable(false);
    }//GEN-LAST:event_cmdKoreksiActionPerformed

    private void cmdBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBatalActionPerformed
        aktif(false);
        setTombol(true);
    }//GEN-LAST:event_cmdBatalActionPerformed

    private void cmdSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSimpanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdSimpanActionPerformed

    private void cmdHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdHapusActionPerformed
        try{
            String sql="delete from data_peserta where no_pkkp='" + txtKode_pes.getText()+ "'";
            stm.executeUpdate(sql);
            baca_data();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_cmdHapusActionPerformed

    private void cmdKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdKeluarActionPerformed
        dispose();
    }//GEN-LAST:event_cmdKeluarActionPerformed

    private void cmbSKCKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSKCKActionPerformed
        JComboBox cSKCK = (javax.swing.JComboBox)evt.getSource();
        //Membaca Item Yang Terpilih — > String
        sSKCK = (String)cSKCK.getSelectedItem();
    }//GEN-LAST:event_cmbSKCKActionPerformed

    private void cmbSKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSKActionPerformed
        JComboBox cSK = (javax.swing.JComboBox)evt.getSource();
        //Membaca Item Yang Terpilih — > String
        sSK = (String)cSK.getSelectedItem();
    }//GEN-LAST:event_cmbSKActionPerformed

    private void cmdSimpanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmdSimpanMouseClicked
        String tKode = txtKode_pes.getText();
        String tNama = txtNama.getText();
        String tKabKo = (String) cmdKabKo.getSelectedItem();
        int tIdKab = getId(tKabKo, "data_kota", "id", "nama_kota");
        String tiKec = (String) cmdKec.getSelectedItem();
        int tKec = getId(tiKec, "data_kec", "id", "nama_kec");
        String tiKel = (String) cmdKel.getSelectedItem();
        int tKel = getIdKel(tiKel, tKec);
        int tUsia = Integer.parseInt(txtUsia.getText());
        double tIPK = Double.parseDouble(txtIPK.getText());
        String tTelp = txtTelp.getText();
        String tEmail = txtEmail.getText();
        String tSK = cmbSK.getSelectedItem().toString();
        String tSKCK = cmbSKCK.getSelectedItem().toString();
       
        String sql;
        if (edit) {
            sql = "UPDATE data_peserta SET " +
                  "nama=?, id_kota=?, id_kec=?, id_kel=?, usia=?, ipk=?, phone=?, email=?, skk=?, skck=? " +
                  "WHERE no_pkkp=?";
        } else {
            sql = "INSERT INTO data_peserta (no_pkkp, nama, id_kota, id_kec, id_kel, usia, ipk, phone, email, skk, skck) " +
              "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }
        
        try (PreparedStatement pstmt = Con.prepareStatement(sql)) {
        if (edit) {
            pstmt.setString(1, tNama);
            pstmt.setInt(2, tIdKab);
            pstmt.setInt(3, tKec);
            pstmt.setInt(4, tKel);
            pstmt.setInt(5, tUsia);
            pstmt.setDouble(6, tIPK);
            pstmt.setString(7, tTelp);
            pstmt.setString(8, tEmail);
            pstmt.setString(9, tSK);
            pstmt.setString(10, tSKCK);
            pstmt.setString(11, tKode); 
        } else {
            pstmt.setString(1, tKode);
            pstmt.setString(2, tNama);
            pstmt.setInt(3, tIdKab);
            pstmt.setInt(4, tKec);
            pstmt.setInt(5, tKel);
            pstmt.setInt(6, tUsia);
            pstmt.setDouble(7, tIPK);
            pstmt.setString(8, tTelp);
            pstmt.setString(9, tEmail);
            pstmt.setString(10, tSK);
            pstmt.setString(11, tSKCK);
        }


        pstmt.executeUpdate();
        tblPeserta.setModel(new DefaultTableModel(dataTable, header));
            baca_data();
            aktif(false);
            setTombol(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_cmdSimpanMouseClicked

    private void tblPesertaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPesertaMouseClicked
        setField();
    }//GEN-LAST:event_tblPesertaMouseClicked

    private void cmdKabKoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmdKabKoItemStateChanged
        // TODO add your handling code here:
        iKab = getId((String) cmdKabKo.getSelectedItem(), "data_kota", "id", "nama_kota");
        detailKabKo();
    }//GEN-LAST:event_cmdKabKoItemStateChanged

    private void cmdKecItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmdKecItemStateChanged
        // TODO add your handling code here:
        iKec = getId((String) cmdKec.getSelectedItem(), "data_kec", "id", "nama_kec");
        detailKec();
    }//GEN-LAST:event_cmdKecItemStateChanged

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
            java.util.logging.Logger.getLogger(dataPeserta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(dataPeserta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(dataPeserta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dataPeserta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new dataPeserta().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbSK;
    private javax.swing.JComboBox<String> cmbSKCK;
    private javax.swing.JButton cmdBatal;
    private javax.swing.JButton cmdHapus;
    private javax.swing.JComboBox<String> cmdKabKo;
    private javax.swing.JComboBox<String> cmdKec;
    private javax.swing.JComboBox<String> cmdKel;
    private javax.swing.JButton cmdKeluar;
    private javax.swing.JButton cmdKoreksi;
    private javax.swing.JButton cmdSimpan;
    private javax.swing.JButton cmdTambah;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblPeserta;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtIPK;
    private javax.swing.JTextField txtKode_pes;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtTelp;
    private javax.swing.JTextField txtUsia;
    // End of variables declaration//GEN-END:variables
}
