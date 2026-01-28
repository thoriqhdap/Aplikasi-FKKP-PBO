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
public class lapPeserta extends javax.swing.JFrame {
    Connection Con;
    ResultSet RsPes;
    Statement stm;
    String tanggal;
    
    private Object[][] dataTable = null;
    private String[] header = {"No PKKP", "Nama", "Kab/Kota", "Kecamatan", "Kelurahan", "Usia", "IPK", "Phone", "Email", "SK", "SKCK"};
   
    
    public lapPeserta() {
        initComponents();
        open_db();
        baca_data();
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
            tblLapPes.setModel(new DefaultTableModel(dataTable, header));
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
                    JOptionPane.showMessageDialog(lapPeserta.this,
                            "Printing completed successfully", "Print", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(lapPeserta.this,
                            "Printing canceled", "Print", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(lapPeserta.this,
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
        cmdExport = new javax.swing.JButton();
        cmdKeluar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblLapPes = new javax.swing.JTable();
        cmdCetak = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        text = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 153, 153));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("sansserif", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Laporan Peserta PKKP");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(286, 286, 286)
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

        tblLapPes.setModel(new javax.swing.table.DefaultTableModel(
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
                "Kode", "Nama", "Provinsi", "Pendidikan", "Usia", "Telepon", "Email", "SK", "SKCK", "IPK"
            }
        ));
        tblLapPes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblLapPesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblLapPes);

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
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cmdExport)
                        .addGap(18, 18, 18)
                        .addComponent(cmdCetak)
                        .addGap(632, 632, 632)
                        .addComponent(cmdKeluar))
                    .addComponent(jScrollPane3))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdExport)
                    .addComponent(cmdKeluar)
                    .addComponent(cmdCetak))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExportActionPerformed
        try {
            ExportToExcel ex = new ExportToExcel(tblLapPes, new File("PesertaPKKP.xls"));
            JOptionPane.showMessageDialog(null, "Sukses Export data.....");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_cmdExportActionPerformed

    private void cmdKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdKeluarActionPerformed
        dispose();
    }//GEN-LAST:event_cmdKeluarActionPerformed

    private void tblLapPesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLapPesMouseClicked
        
    }//GEN-LAST:event_tblLapPesMouseClicked

    private void cmdCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCetakActionPerformed
        StringBuilder report = new StringBuilder();
        report.append("Laporan Peserta\nTanggal: ").append(tanggal).append("\n\n");
        report.append(String.format("%-5s %-6s %-15s %-10s %-15s %-5s %-5s %-5s %-10s %-5s %-5s\n",
                "No PKKP", "Nama", "Kota", "Kecamatan", "Kelurahan", "Usia", "IPK", "Telepon", "Email", "SK", "SKCK"));
        report.append("-".repeat(160)).append("\n");

        for (int i = 0; i < tblLapPes.getRowCount(); i++) {
            report.append(String.format("%-5s %-6s %-15s %-10s %-15s %-5d %-5s %-5s %-15s %-5s %-5s\n",
                    tblLapPes.getValueAt(i, 0), // No PKKP
                    tblLapPes.getValueAt(i, 1), // Nama
                    tblLapPes.getValueAt(i, 2), // Kota
                    tblLapPes.getValueAt(i, 3), // Kecamatan
                    tblLapPes.getValueAt(i, 4), // Kelurahan
                    tblLapPes.getValueAt(i, 5), // Usia
                    tblLapPes.getValueAt(i, 6), // IPK
                    tblLapPes.getValueAt(i, 7), // Telepon
                    tblLapPes.getValueAt(i, 8), // Email
                    tblLapPes.getValueAt(i, 9), // SK
                    tblLapPes.getValueAt(i, 10) // SKCK
            ));
        }

        text.setText(report.toString());
        text.setFont(new Font("Monospaced", Font.PLAIN, 10));

        MessageFormat header = new MessageFormat("Laporan Peserta - Halaman {0}");
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
            java.util.logging.Logger.getLogger(lapPeserta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(lapPeserta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(lapPeserta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(lapPeserta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new lapPeserta().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdCetak;
    private javax.swing.JButton cmdExport;
    private javax.swing.JButton cmdKeluar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tblLapPes;
    private javax.swing.JTextArea text;
    // End of variables declaration//GEN-END:variables
}
