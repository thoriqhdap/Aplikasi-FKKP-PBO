/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pkkp;
import java.io.File;
import java.awt.Font;
import java.sql.*;
import java.text.MessageFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.print.PrinterException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.SwingWorker;
/**
 *
 * @author joni
 */
public class lapPesertaLolos extends javax.swing.JFrame {
    Connection Con;
    ResultSet RsLulus;
    Statement stm;
    String tanggal;
    int kuotaI = 0;
    
    DefaultTableModel tableModel = new DefaultTableModel(
            new Object[][]{},
            new String[]{"Kode", "Nama Peserta", "Kab/Kota", "Kecamatan", "Kelurahan ", "Usia", "Phone", "Email",  "IPK", "Nilai Tulis", "Nilai Wawancara","Nilai Akhir","Rangking"
            });
    
    public lapPesertaLolos() {
        initComponents();
        open_db();
        getKuota();
        setVisible(true);
        format_tanggal();
    }
    
    private void open_db() {
        try {
            KoneksiMysql kon = new KoneksiMysql("localhost", "root", "", "pkkp");
            Con = (Connection) kon.getConnection();
        } catch (Exception e) {
            System.out.println("Error : " + e);
        }
    }
    
    private void getKuota(){
        try{
            stm = Con.createStatement();
            RsLulus = stm.executeQuery("SELECT jml_kuota as kuota FROM kuota ORDER BY id DESC LIMIT 1;");
            if (RsLulus.next()) {
            kuotaI = RsLulus.getInt("kuota");
        }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
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
    
    private void baca_data(int kuota) {
        open_db();
        try {
            stm = Con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String query = "SELECT * FROM data_peserta p INNER JOIN administrasi s ON p.no_pkkp=s.no_pkkp INNER JOIN nilai n ON p.no_pkkp=n.no_pkkp ORDER BY \n"
                    + "    n.nilai_akhir DESC, p.ipk DESC LIMIT " +kuota ;
            RsLulus = stm.executeQuery(query);

            tableModel.setRowCount(0);
            int x = 1;
            while (RsLulus.next()) {
                Object[] row = new Object[13];
                row[0] = RsLulus.getString("no_pkkp");
                row[1] = RsLulus.getString("nama");
                row[2] = getNm(RsLulus.getInt("id_kota"), "data_kota", "nama_kota");
                row[3] = getNm(RsLulus.getInt("id_kec"), "data_kec", "nama_kec");
                row[4] = getNm(RsLulus.getInt("id_kel"), "data_kel", "nama_kel");
                row[5] = RsLulus.getInt("usia");
                row[6] = RsLulus.getString("phone");
                row[7] = RsLulus.getString("email");
                row[8] = RsLulus.getDouble("ipk");
                row[9] = RsLulus.getInt("nilai_tulis");
                row[10] = RsLulus.getInt("nilai_wawancara");
                row[11] = RsLulus.getDouble("nilai_akhir");
                row[12] = x;
                x++;

                // Tambahkan baris ke DefaultTableModel
                tableModel.addRow(row);
            }

            // Atur model tabel dengan model yang baru
            tblLulus.setModel(tableModel);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void simpanData() {
        int tKuota = Integer.parseInt(txtK.getText());
        java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis()); // Mendapatkan tanggal saat ini

        String query = "INSERT INTO kuota (thn_pelaksanaan, jml_kuota) VALUES (?, ?)";
        try (PreparedStatement stmt = Con.prepareStatement(query)) {
            stmt.setDate(1, currentDate); // Menyimpan tanggal saat ini
            stmt.setInt(2, tKuota);
            stmt.addBatch();

            int[] result = stmt.executeBatch();
            System.out.println("Batch insert completed. Rows inserted: " + result.length);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void format_tanggal() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        tanggal = sdf.format(cal.getTime());
    }
    
    private MessageFormat createFormat(String source) {
        String text = source;
        if (text != null && text.length() > 0) {
            try {
                return new MessageFormat(text);
            } catch (IllegalArgumentException e) {
                error("Sorry");
            }
        }
        return null;
    }

    private void message(boolean error, String msg) {
        int type = (error ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
        JOptionPane.showMessageDialog(this, msg, "Printing", type);
    }

    private void error(String msg) {
        message(true, msg);
    }

    private class PrintingTask extends SwingWorker<Boolean, Object> {

        private final MessageFormat headerFormat;
        private final MessageFormat footerFormat;
        private final boolean interactive;

        public PrintingTask(MessageFormat header, MessageFormat footer, boolean interactive) {
            this.headerFormat = header;
            this.footerFormat = footer;
            this.interactive = interactive;
        }

        @Override
        protected Boolean doInBackground() throws Exception {
            return text.print(headerFormat, footerFormat, true, null, null, interactive);
        }

        @Override
        protected void done() {
            try {
                boolean completed = get();
                if (completed) {
                    JOptionPane.showMessageDialog(lapPesertaLolos.this,
                            "Printing completed successfully", "Print", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(lapPesertaLolos.this,
                            "Printing canceled", "Print", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(lapPesertaLolos.this,
                        "Error printing: " + e.getMessage(), "Print Error", JOptionPane.ERROR_MESSAGE);
            }
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmdSeleksi = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLulus = new javax.swing.JTable();
        cmdExport = new javax.swing.JButton();
        cmdKeluar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtK = new javax.swing.JTextField();
        cmdCetak = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        text = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 153, 153));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("sansserif", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Laporan Peserta PKKP Lolos");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(242, 242, 242)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        cmdSeleksi.setText("Seleksi Lolos");
        cmdSeleksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSeleksiActionPerformed(evt);
            }
        });

        tblLulus.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Kode ", "Nama Peserta", "Kab/Kota", "IPK", "Nilai Tertulis", "Nilai Wawancara", "Nilai Akhir"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblLulus);

        cmdExport.setText("Export");
        cmdExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExportActionPerformed(evt);
            }
        });

        cmdKeluar.setText("Keluar");
        cmdKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdKeluarActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Montserrat", 0, 12)); // NOI18N
        jLabel3.setText("Jumlah Kuota");

        cmdCetak.setText("Cetak");
        cmdCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCetakActionPerformed(evt);
            }
        });

        text.setColumns(20);
        text.setRows(5);
        jScrollPane3.setViewportView(text);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(txtK, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(cmdSeleksi))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(cmdExport)
                            .addGap(18, 18, 18)
                            .addComponent(cmdCetak)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmdKeluar))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 899, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING)))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdSeleksi))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdExport)
                    .addComponent(cmdKeluar)
                    .addComponent(cmdCetak))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 21, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdSeleksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSeleksiActionPerformed
        simpanData();
        int tk = Integer.parseInt(txtK.getText());
        baca_data(tk);
    }//GEN-LAST:event_cmdSeleksiActionPerformed

    private void cmdExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExportActionPerformed
        try {
            ExportToExcel ex = new ExportToExcel(tblLulus, new File("Daftar peserta yang lolos.xls"));
            JOptionPane.showMessageDialog(null, "Sukses Export data.....");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_cmdExportActionPerformed

    private void cmdKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdKeluarActionPerformed
        dispose();
    }//GEN-LAST:event_cmdKeluarActionPerformed

    private void cmdCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCetakActionPerformed
        StringBuilder report = new StringBuilder();
        report.append("Laporan Peserta Lolos\nTanggal: ").append(tanggal).append("\n\n");
        report.append(String.format("%-5s %-6s %-12s %-10s %-15s %-5s %-7s %-15s %-5s %-6s %-10s %-5s\n",
                "No PKKP", "Nama", "Kota", "Kecamatan", "Kelurahan", "Usia", "Telepon", "Email", "IPK", "NTulis", "NWawancara", "NAkhir"));
        report.append("-".repeat(200)).append("\n");

        for (int i = 0; i < tblLulus.getRowCount(); i++) {
            report.append(String.format("%-5s %-6s %-12s %-10s %-15s %-5d %-7s %-15s %-5.2f %-6d %-10d %-5.2f\n",
                    tblLulus.getValueAt(i, 0), // No PKKP
                    tblLulus.getValueAt(i, 1), // Nama
                    tblLulus.getValueAt(i, 2), // Kota
                    tblLulus.getValueAt(i, 3), // Kecamatan
                    tblLulus.getValueAt(i, 4), // Kelurahan
                    tblLulus.getValueAt(i, 5), // Usia
                    tblLulus.getValueAt(i, 6), // Telepon
                    tblLulus.getValueAt(i, 7), // Email
                    tblLulus.getValueAt(i, 8), // IPK
                    tblLulus.getValueAt(i, 9), // Nilai Tulis
                    tblLulus.getValueAt(i, 10), // Nilai Wawancara
                    tblLulus.getValueAt(i, 11) // Nilai Akhir
            ));
        }

        text.setText(report.toString());
        text.setFont(new Font("Monospaced", Font.PLAIN, 11));

        MessageFormat header = new MessageFormat("Laporan Peserta Lolos - Halaman {0}");
        MessageFormat footer = new MessageFormat("Halaman {0}");

        PrintingTask task = new PrintingTask(header, footer, true);
        task.execute();
    }//GEN-LAST:event_cmdCetakActionPerformed

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
            java.util.logging.Logger.getLogger(lapPesertaLolos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(lapPesertaLolos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(lapPesertaLolos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(lapPesertaLolos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new lapPesertaLolos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdCetak;
    private javax.swing.JButton cmdExport;
    private javax.swing.JButton cmdKeluar;
    private javax.swing.JButton cmdSeleksi;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tblLulus;
    private javax.swing.JTextArea text;
    private javax.swing.JTextField txtK;
    // End of variables declaration//GEN-END:variables
}
