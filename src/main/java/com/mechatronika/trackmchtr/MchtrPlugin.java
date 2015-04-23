/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mechatronika.trackmchtr;

import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;


/**
 *
 * @author Administrator
 */
public class MchtrPlugin implements PlugIn {
    
     @Override
     public void run(String arg) {

         
             ImagePlus img2 = WindowManager.getCurrentImage();  // ImageJ get currently open image
             ImageProcessor ip2 = img2.getProcessor();
             
             ObjectBox box = new ObjectBox(img2.getNFrames());
             GUI MainWindow = new GUI(img2, box);
             
             

     } 
}
