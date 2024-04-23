package com.vision.examples;

import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


class ExcelLineBreakWrapText {

 public static void main(String[] args) throws Exception {

  Workbook workbook = new XSSFWorkbook();
  Sheet sheet = workbook.createSheet();

  CellStyle wrapStyle = workbook.createCellStyle();
  wrapStyle.setWrapText(true);

  Row row = sheet.createRow(0);

  Cell cell = row.createCell(0); 
  cell.setCellStyle(wrapStyle);
//  cell.setCellValue("Consommation (crédits)\r\nRéalisation (produits)");
  cell.setCellValue("Bala\nSatya\nPraksah");

  sheet.autoSizeColumn(0);

  workbook.write(new FileOutputStream("E:\\Java_Files\\"+"ExcelLineBreakWrapText.xlsx"));
//  workbook.close();

 }
}