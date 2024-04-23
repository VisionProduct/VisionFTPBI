package com.vision.controller;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class InsertTextFileToOracle {

  public static Connection getConnection() throws Exception {
    String driver = "oracle.jdbc.driver.OracleDriver";
    String url = "jdbc:oracle:thin:@localhost:1521:oracle";
    String username = "username";
    String password = "password";
    Class.forName(driver);
    Connection conn = DriverManager.getConnection(url, username, password);
    return conn;
  }

  public static void main(String[] args)throws Exception {
    String id = "001";
    String fileName = "fileName.txt";
    
    FileInputStream fis = null;
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      conn = getConnection();
      conn.setAutoCommit(false);
      File file = new File(fileName);
      fis = new FileInputStream(file);
      pstmt = conn.prepareStatement("insert into DataFiles(id, fileName, fileBody) values (?, ?, ?)");
      pstmt.setString(1, id);
      pstmt.setString(2, fileName);
      pstmt.setAsciiStream(3, fis, (int) file.length());
      pstmt.executeUpdate();
      conn.commit();
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      e.printStackTrace();
    } finally {
      pstmt.close();
      fis.close();
      conn.close();
    }
  }
}
