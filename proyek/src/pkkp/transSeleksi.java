/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pkkp;
import java.io.File;
import java.sql.*;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author joni
 */
public class transSeleksi extends javax.swing.JFrame {
    Connection Con;
    ResultSet RsSeleksi;
    Statement stm;
    String sSKCK;
    String sSK;
    private DefaultTableModel tableModel;
    
    private Object[][] dataTable = null;
    private String[] header = {"Kode Peserta", "Nama Peserta", "Kab/Kota", "IPK", "SK", "SKCK", "Status"};

    public transSeleksi() {
        initComponents();
        open_db();
        setField();
        Sk();
        Skck();
        setLocationRelativeTo(null);
    }
    
     private void open_db() {
        try {
            KoneksiMysql kon = new KoneksiMysql("localhost", "root", "", "pkkp");
            Con = (Connection) kon.getConnection();
        } catch (Exception e) {
            System.out.println("Error : " + e);
        }
    }
     
    private void setField() {
        String[] columnNames = {"Kode", "Nama Peserta", "Kab/Kota", "IPK", "Surat Kesehatan", "SKCK", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tblSeleksi.setModel(tableModel);
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
    
    private void cariData() {
        String ipk = txtIpk.getText();
        String sk = cmbSK.getSelectedItem().toString();
        String skck = cmbSKCK.getSelectedItem().toString();

        tableModel.setRowCount(0); // Bersihkan tabel

        try {
            String query = "SELECT * FROM data_peserta WHERE ipk >= ? AND skk = ? AND skck = ?";
            PreparedStatement pstmt = Con.prepareStatement(query);
            pstmt.setDouble(1, Double.parseDouble(ipk));
            pstmt.setDouble(1, Double.parseDouble(ipk));
            pstmt.setString(2, sk);
            pstmt.setString(3, skck);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Object[] row = {
                    rs.getString("no_pkkp"),
                    rs.getString("nama"),
                    getNm(rs.getInt("id_kota"), "data_kota", "nama_kota"),
                    rs.getDouble("ipk"),
                    rs.getString("skk"),
                    rs.getString("skck")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saat mencari data: " + e.getMessage());
        }
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
    
    private void simpanData() {
        String deleteQuery = "DELETE FROM administrasi";
        try (PreparedStatement deleteStmt = Con.prepareStatement(deleteQuery)) {
            deleteStmt.executeUpdate();
            System.out.println("All data deleted from administrasi.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String insertQuery = "INSERT INTO administrasi (no_pkkp, status) VALUES (?, ?)";

        try (PreparedStatement stmt = Con.prepareStatement(insertQuery)) {
            int rowCount = tableModel.getRowCount();

            for (int i = 0; i < rowCount; i++) {
                String no_pkkp = (String) tableModel.getValueAt(i, 0);
                String status = "Lolos";

                stmt.setString(1, no_pkkp);
                stmt.setString(2, status);
                stmt.addBatch();
            }

            int[] result = stmt.executeBatch();
            System.out.println("Batch insert completed. Rows inserted: " + result.length);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void baca_data() {
        try {
            stm = Con.createStatement();
            RsSeleksi = stm.executeQuery("SELECT data_peserta.no_pkkp,data_peserta.nama,data_peserta.ipk,data_peserta.id_kota,data_peserta.skk,data_peserta.skck,administrasi.status FROM data_peserta INNER JOIN administrasi ON data_peserta.no_pkkp=administrasi.no_pkkp");

            tableModel.setRowCount(0);

            while (RsSeleksi.next()) {
                Object[] row = new Object[11];
                row[0] = RsSeleksi.getString("no_pkkp");
                row[1] = RsSeleksi.getString("nama");
                row[2] = getNm(RsSeleksi.getInt("id_kota"), "data_kota", "nama_kota");
                row[3] = RsSeleksi.getDouble("ipk");
                row[4] = RsSeleksi.getString("skk");
                row[5] = RsSeleksi.getString("skck");
                row[6] = RsSeleksi.getString("status");

                // Tambahkan baris ke DefaultTableModel
                tableModel.addRow(row);
            }

            // Atur model tabel dengan model yang baru
            tblSeleksi.setModel(tableModel);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cmdKeluar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSeleksi = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtIpk = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        cmdSeleksi = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cmbSK = new javax.swing.JComboBox<>();
        cmbSKCK = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        cmdKeluar.setText("Keluar");
        cmdKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdKeluarActionPerformed(evt);
            }
        });

        tblSeleksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Kode Peserta", "Nama Peserta", "Provinsi", "IPK", "SK", "SKCK"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.String.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblSeleksi);

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("sansserif", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Seleksi Administrasi PKKP");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(250, 250, 250)
                .addComponent(jLabel1)
                .addContainerGap(278, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel2.setText("IPK");

        cmdSeleksi.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        cmdSeleksi.setText("Seleksi");
        cmdSeleksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSeleksiActionPerformed(evt);
            }
        });

        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel3.setText("SK");

        jLabel4.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel4.setText("SKCK");

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 762, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(txtIpk, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cmbSK, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(cmbSKCK, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGap(18, 18, 18)
                            .addComponent(cmdSeleksi, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnSimpan))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(686, 686, 686)
                            .addComponent(cmdKeluar))))
                .addGap(26, 26, 26))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtIpk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdSeleksi)
                    .addComponent(btnSimpan)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(cmbSK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbSKCK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cmdKeluar)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdKeluarActionPerformed
        dispose();
    }//GEN-LAST:event_cmdKeluarActionPerformed

    private void cmdSeleksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSeleksiActionPerformed
        // TODO add your handling code here:
        cariData();
    }//GEN-LAST:event_cmdSeleksiActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        // TODO add your handling code here:
        simpanData();
        baca_data();
    }//GEN-LAST:event_btnSimpanActionPerformed

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
            java.util.logging.Logger.getLogger(transSeleksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(transSeleksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(transSeleksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(transSeleksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new transSeleksi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSimpan;
    private javax.swing.JComboBox<String> cmbSK;
    private javax.swing.JComboBox<String> cmbSKCK;
    private javax.swing.JButton cmdKeluar;
    private javax.swing.JButton cmdSeleksi;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblSeleksi;
    private javax.swing.JTextField txtIpk;
    // End of variables declaration//GEN-END:variables
}
