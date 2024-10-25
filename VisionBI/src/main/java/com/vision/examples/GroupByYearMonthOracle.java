package com.vision.examples;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupByYearMonthOracle {

    public static class CurveData {
        String country;
        String leBook;
        String ftpCurveId;
        String effectiveDate;

        public CurveData(String country, String leBook, String ftpCurveId, String effectiveDate) {
            this.country = country;
            this.leBook = leBook;
            this.ftpCurveId = ftpCurveId;
            this.effectiveDate = effectiveDate;
        }

        @Override
        public String toString() {
            return "COUNTRY: " + country + ", LE_BOOK: " + leBook + ", FTP_CURVE_ID: " + ftpCurveId+ ", EFFECTIVE_DATE : " + effectiveDate;
        }
    }

    public static void main(String[] args) {
        // JDBC URL, username, and password for Oracle database
        String jdbcUrl = "jdbc:oracle:thin:@10.16.1.106:1521:VISDB";  // Replace with your Oracle DB URL
        String username = "VISION_devuser";
        String password = "vision123";
        
        // SQL query to select the relevant fields from FTP_CURVES table
        String query = "SELECT COUNTRY, LE_BOOK, FTP_CURVE_ID, "
        		+ "TO_CHAR(EFFECTIVE_DATE, 'DD-MM-YYYY') EFFECTIVE_DATE "
        		+ "FROM FTP_CURVES where FTP_CURVE_ID = 'A_LOAN_0001_NOMINAL' ORDER BY EFFECTIVE_DATE DESC ";
        
        List<CurveData> curveDataList = new ArrayList<>();
        List<LocalDate> dates = new ArrayList<>();

        // Date format of EFFECTIVE_DATE string (assumed format: 'yyyy-MM-dd')
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Establish Oracle Database Connection
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            // Loop through the result set and parse EFFECTIVE_DATE, populate CurveData list
            while (resultSet.next()) {
                String country = resultSet.getString("COUNTRY");
                String leBook = resultSet.getString("LE_BOOK");
                String ftpCurveId = resultSet.getString("FTP_CURVE_ID");
                String dateString = resultSet.getString("EFFECTIVE_DATE");
                
                // Parse EFFECTIVE_DATE string to LocalDate
                LocalDate localDate = LocalDate.parse(dateString, dateFormatter);

                // Add the data to the lists
                dates.add(localDate);
                curveDataList.add(new CurveData(country, leBook, ftpCurveId, dateString));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Group the CurveData list by year and month using the dates
        Map<Integer, Map<Integer, Map<Integer, List<CurveData>>>> groupedData = new HashMap<>();

        for (int i = 0; i < dates.size(); i++) {
            LocalDate date = dates.get(i);
            int year = date.getYear();
            int month = date.getMonthValue();
            int day = date.getDayOfMonth();

            // Create nested maps if not present
            groupedData
                .computeIfAbsent(year, k -> new HashMap<>())
                .computeIfAbsent(month, k -> new HashMap<>())
                .computeIfAbsent(day, k -> new ArrayList<>())
                .add(curveDataList.get(i));
        }
        groupedData.keySet().stream().sorted().forEach(year -> {
            System.out.println("Year: " + year);
            groupedData.get(year).keySet().stream().sorted() // Sort by month
                .forEach(month -> {
                    System.out.println("  Month: " + month);
                    groupedData.get(year).get(month).keySet().stream()
                        .sorted() // Sort by day
                        .forEach(day -> {
                            System.out.println("    Day: " + day);
                            groupedData.get(year).get(month).get(day).forEach(curveData -> 
                                System.out.println("      " + curveData));
                        });
                });
        });
    
       System.out.println("");
       System.out.println("");
    
    }
}
