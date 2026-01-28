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
public class lapSeleksi extends javax.swing.JFrame {
    Connection Con;
    ResultSet RsSeleksi;
    Statement stm;
    String tanggal;
    
    private Object[][] dataTable = null;
    private String[] header = {"Kode Peserta", "Nama Peserta", "Kab/Kota", "IPK", "SK", "SKCK", "Status"};

    public lapSeleksi() {
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
    
    private void baca_data() {
        open_db();
        try {
            stm = Con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            RsSeleksi = stm.executeQuery("SELECT data_peserta.no_pkkp,data_peserta.nama,data_peserta.ipk,data_peserta.id_kota,data_peserta.skk,data_peserta.skck,administrasi.status FROM data_peserta INNER JOIN administrasi ON data_peserta.no_pkkp=administrasi.no_pkkp WHERE administrasi.status = 'Lolos'");
            ResultSetMetaData meta = RsSeleksi.getMetaData();
            int col = meta.getColumnCount();
            int baris = 0;
            while (RsSeleksi.next()) {
                baris = RsSeleksi.getRow();
            }
            dataTable = new Object[baris][col];
            int x = 0;
            RsSeleksi.beforeFirst();
            while (RsSeleksi.next()) {
                dataTable[x][0] = RsSeleksi.getString("no_pkkp");
                dataTable[x][1] = RsSeleksi.getString("nama");
                dataTable[x][2] = getNm(RsSeleksi.getInt("id_kota"), "data_kota", "nama_kota");
                dataTable[x][3] = RsSeleksi.getFloat("ipk");
                dataTable[x][4] = RsSeleksi.getString("skk");
                dataTable[x][5] = RsSeleksi.getString("skck");
                dataTable[x][6] = RsSeleksi.getString("status");
                x++;
            }
            tblSeleksi.setModel(new DefaultTableModel(dataTable, header));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
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
                    JOptionPane.showMessageDialog(lapSeleksi.this,
                            "Printing completed successfully", "Print", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(lapSeleksi.this,
                            "Printing canceled", "Print", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(lapSeleksi.this,
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSeleksi = new javax.swing.JTable();
        cmdExport = new javax.swing.JButton();
        cmdKeluar = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        text = new javax.swing.JTextArea();
        cmdCetak = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 153, 153));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("sansserif", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Lolos Seleksi Administrasi");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(228, 228, 228))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        tblSeleksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Kode ", "Nama Peserta", "Provinsi", "IPK", "SK", "SKCK"
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

        text.setColumns(20);
        text.setRows(5);
        jScrollPane3.setViewportView(text);

        cmdCetak.setText("Cetak");
        cmdCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCetakActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 762, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cmdExport)
                        .addGap(18, 18, 18)
                        .addComponent(cmdCetak)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cmdKeluar)))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmdExport, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmdKeluar)
                        .addComponent(cmdCetak)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 27, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExportActionPerformed
        try {
            ExportToExcel ex = new ExportToExcel(tblSeleksi, new File("Seleksi Administrasi.xls"));
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
        report.append("Laporan Seleksi Administrasi\nTanggal: ").append(tanggal).append("\n\n");
        report.append(String.format("%-5s %-10s %-15s %-10s %-10s %-10s %-10s\n",
                "No PKKP", "Nama", "Kota", "IPK", "SK", "SKCK", "Status"));
        report.append("-".repeat(100)).append("\n");

        for (int i = 0; i < tblSeleksi.getRowCount(); i++) {
            report.append(String.format("%-5s %-10s %-15s %-10.2f %-10s %-10s %-10s\n",
                    tblSeleksi.getValueAt(i, 0), // No PKKP
                    tblSeleksi.getValueAt(i, 1), // Nama
                    tblSeleksi.getValueAt(i, 2), // Kota
                    tblSeleksi.getValueAt(i, 3), // IPK
                    tblSeleksi.getValueAt(i, 4), // SK
                    tblSeleksi.getValueAt(i, 5), // SKCK
                    tblSeleksi.getValueAt(i, 6) // Status
            ));
        }

        text.setText(report.toString());
        text.setFont(new Font("Monospaced", Font.PLAIN, 12));

        MessageFormat header = new MessageFormat("Laporan Seleksi Administrasi - Halaman {0}");
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
            java.util.logging.Logger.getLogger(lapSeleksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(lapSeleksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(lapSeleksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(lapSeleksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new lapSeleksi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdCetak;
    private javax.swing.JButton cmdExport;
    private javax.swing.JButton cmdKeluar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tblSeleksi;
    private javax.swing.JTextArea text;
    // End of variables declaration//GEN-END:variables
}
