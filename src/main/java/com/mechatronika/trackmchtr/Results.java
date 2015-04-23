/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mechatronika.trackmchtr;

import java.awt.event.*;
import javax.swing.*;
/**
 *
 * @author Kinga
 */
public class Results extends JFrame{
    ExportToExcel export;
    JTable table;
    String nameOfExcel;
    
    Results(double [][] velocity, String title)
    {
        setSize(700,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        
        Object [][]numbers = new Object[velocity.length][5];
        String []names = {"Lp", "Mean Velocity [um/s]", "Min Velocity", "Max Velocity", "Median Velocity"};
        for(int i=0; i<velocity.length; i++)
        {           
            numbers[i][0]=i+1;
            numbers[i][1]=velocity[i][0];//mean
            numbers[i][2]=velocity[i][1];//min
            numbers[i][3]=velocity[i][2];//max
            numbers[i][4]=velocity[i][3];//median
        }        
        table = new JTable(numbers, names);         
        panel.add(new JScrollPane(table)); 
        
        nameOfExcel = title;
        
        JButton exportButton = new JButton("Export to Excel");
        exportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                export = new ExportToExcel(table, nameOfExcel);
            }
            
        });
        panel.add(exportButton);
        
        add(panel);
        setVisible(true);
        
        
    }
}
