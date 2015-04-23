/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mechatronika.trackmchtr;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.model.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.util.*;
import org.apache.poi.xssf.usermodel.*;
import ij.*;
import java.util.logging.Level;
import java.util.logging.Logger;







/**
 *
 * @author Sujek
 */
public class ExportToExcel {
    
    private static JTable table;
    
    //COS NIE CHCE SIE TWORZYC
    
    ExportToExcel(JTable tableToExport, String path) //throws IOException
    {
        table = tableToExport;
        //String pt = "C:\\Users\\Sujek\\Desktop\\Fiji.app\\bleh\\jt.xlsx";
        String pt = path + ".xlsx";
        try {            
            writeToExcel(pt);            
        } catch (IOException ex) {
            Logger.getLogger(ExportToExcel.class.getName()).log(Level.SEVERE, null, ex);
            IJ.showMessage(ex.getMessage());
        }
        //IJ.showMessage(pt);
        
        /*import ij.io.OpenDialog;
        OpenDialog od = new OpenDialog("Open Image File...", arg);
        String dir = od.getDirectory();
        String name = od.getFileName();
        String id = dir + name;*/ 
        //Uzycie alternatywy filechoosera
        //IJ.showMessage(pt);
        
        /*import ij.io.OpenDialog;
        OpenDialog od = new OpenDialog("Open Image File...", arg);
        String dir = od.getDirectory();
        String name = od.getFileName();
        String id = dir + name;*/ 
        //Uzycie alternatywy filechoosera
        
    }
    
    
    private static void writeToExcel(String path) throws FileNotFoundException, IOException {
    new WorkbookFactory();
    Workbook wb = new XSSFWorkbook(); //Excell workbook
    Sheet sheet = wb.createSheet(); //WorkSheet //wb.createSheet("sheetName");
    Row row = sheet.createRow(2); //Row created at line 3
    TableModel model = table.getModel(); //Table model


    Row headerRow = sheet.createRow(0); //Create row at line 0
    for(int headings = 0; headings < model.getColumnCount(); headings++){ //For each column
        headerRow.createCell(headings).setCellValue(model.getColumnName(headings));//Write column name
    }

    for(int rows = 0; rows < model.getRowCount(); rows++){ //For each table row
        for(int cols = 0; cols < table.getColumnCount(); cols++){ //For each table column
            row.createCell(cols).setCellValue(model.getValueAt(rows, cols).toString()); //Write value
        }

        //Set the row to the next one in the sequence 
        row = sheet.createRow((rows + 3));         
    }
    wb.write(new FileOutputStream(path));//Save the file  
    IJ.showMessage("Excel file created!");
}
    
}
