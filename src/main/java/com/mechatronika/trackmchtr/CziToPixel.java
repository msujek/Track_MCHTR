/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mechatronika.trackmchtr;
import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.GenericDialog;
import ij.gui.NewImage;
import ij.plugin.PlugIn;
import ij.plugin.filter.GaussianBlur;
import ij.process.ImageProcessor;
import java.awt.List;
import java.util.ArrayList;

/**
 *
 * @author msujek
 */
public class CziToPixel {
    
    Matrix matrix;
    ImagePlus loadedCzi;
    public int t_max, z_max;
    boolean hasNextFrame;
    private int actT;
    
    CziToPixel(ImagePlus img)
    {
        
        
        loadedCzi = img.duplicate();
        t_max=loadedCzi.getNFrames();

        z_max=loadedCzi.getNSlices();

        hasNextFrame = true;
        
       // IJ.showMessage("NFrames"+loadedCzi.getNFrames());
       // IJ.showMessage("NSlices" + loadedCzi.getNSlices());
       // IJ.showMessage("NChannels" + loadedCzi.getNChannels());
       // IJ.showMessage("NDimensions"+loadedCzi.getNDimensions());
       
        int t =1;
            loadedCzi.setT(t);
            /*matrix = new Matrix(loadedCzi.getNSlices());
            for(int z =0; z<loadedCzi.getNSlices(); z++)
            {
                loadedCzi.setZ(z);
                for(int x =0; x<512; x++)
                    for(int y =0; y<512; y++)
                    {
                        int[] px_table = loadedCzi.getPixel(x, y);
                        
                        matrix.voxel[z][x][y] = (short)px_table[0];
                    }
            }*/
    }
    
    public void loadNextFrame()
    {
        actT = loadedCzi.getT();

        if(actT<t_max)
        {
            loadedCzi.setT(actT+1);
            matrix = new Matrix(z_max);

            for(int z =0; z<z_max; z++)
            {
                loadedCzi.setZ(z+1);
                for(int x =0; x<512; x++)
                    for(int y =0; y<512; y++)
                    {
                        
                        int[] px_table = loadedCzi.getPixel(x, y);
                     
                        matrix.voxel[z][x][y] = (short)px_table[0];
                    }
            }
            IJ.showMessage("actT:"+actT);

        }
        
        else
        {
            loadedCzi.close();
            IJ.showMessage("fail");
            hasNextFrame = false;
        } 
    }
    
    public boolean nextFrameAvalible()
    {
        return hasNextFrame;
    }
    
    public Matrix getMatrix()
    {
        return matrix;
    }
    
    public int getTime()
    {
        return actT-1;
    }
    


    
}
