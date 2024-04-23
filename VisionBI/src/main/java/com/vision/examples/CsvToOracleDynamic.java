package com.vision.examples;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.StringJoiner;

import org.apache.commons.lang.StringUtils;

public class CsvToOracleDynamic {
    public static void main(String[] args) {
    	String path = "E:\\Java_Files\\";
        String csvFilePath = path +"21-SEP-23_2_1_ALPHA_SUB_TAB.csv"; // Path to your CSV file
        String jdbcUrl = "jdbc:oracle:thin:@10.16.1.101:1521:VISIONBI"; // Your Oracle JDBC URL
        String username = "VISION_DQ";
        String password = "Vision123";
        String tableName = "ALPHA_SUB_TAB_03_10_23"; // Name for the dynamically created table

        try {
            // Load the Oracle JDBC driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Establish a connection to the Oracle database
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Read the CSV file and extract header columns
            BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));
            String headerLine = reader.readLine();
            String[] columns = headerLine.split(",");
            StringJoiner ruleColmns = new StringJoiner(", ");
            for (String column : columns) {
            	if(StringUtils.contains(column, "_R0")) {
            		ruleColmns.add(column);
            	}
            }
            System.out.println(ruleColmns);
/*
            // Create a table with columns based on the CSV header
            createTable(connection, tableName, columns);

            // Prepare the INSERT statement
            String insertQuery = "INSERT INTO " + tableName + " VALUES (";
            for (int i = 0; i < columns.length; i++) {
                insertQuery += "?,";
            }
            insertQuery = insertQuery.substring(0, insertQuery.length() - 1) + ")";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            // Read and insert data rows
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                for (int i = 0; i < data.length; i++) {
                    preparedStatement.setString(i + 1, data[i]);
                }
                preparedStatement.executeUpdate();
            }

            // Close resources
            reader.close();
            preparedStatement.close();
            connection.close();

            System.out.println("Data inserted successfully into the dynamically created table!");*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createTable(Connection connection, String tableName, String[] columns) throws SQLException {
        StringBuilder createTableQuery = new StringBuilder("CREATE TABLE " + tableName + " (");
        for (String column : columns) {
            createTableQuery.append(column).append(" VARCHAR2(255), "); // Adjust data types and sizes as needed
        }
//        createTableQuery.append("SUM").append(" VARCHAR2(255), "); // Adjust data types and sizes as needed
        createTableQuery.setLength(createTableQuery.length() - 2); // Remove trailing comma and space
        createTableQuery.append(")");
        
        connection.createStatement().executeUpdate(createTableQuery.toString());
    }
}
