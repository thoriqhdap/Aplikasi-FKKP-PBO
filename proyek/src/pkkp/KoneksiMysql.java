/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pkkp;
import java.sql.*;
/**
 *
 * @author joni
 */
public class KoneksiMysql {
    String url, usr, pwd, dbn;
    public KoneksiMysql (String dbn) {
        this.url = "jdbc:mysql://localhost/pkkp";
        this.usr = "root";
        this.pwd = "";
    }
    public KoneksiMysql (String host, String user, String pass,String dbn) {
        this.url = "jdbc:mysql://localhost/pkkp";
        this.usr = user;
        this.pwd = "";
    }
    public Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(this.url, this.usr,this.pwd);
        } catch (ClassNotFoundException e) {
            System.out.println ("Error #1 : " + e.getMessage());
            System.exit(0);
        } catch (SQLException e) {
            System.out.println ("Error #2 : " + e.getMessage());
            System.exit(0);
        }
        return con;
    }
//
//public static void main (String args[]) {
//KoneksiMysql kon = new KoneksiMysql ("pkkp");
//Connection c = kon.getConnection();
//}

}