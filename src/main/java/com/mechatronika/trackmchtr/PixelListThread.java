/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mechatronika.trackmchtr;

import ij.IJ;
import ij.*;

/**
 *
 * @author Sujek
 */
public class PixelListThread extends Thread{
    
    private Displayer d;
    private CziToPixel cziPixel;
    private ObjectBox box;
    private Matrix matrix;
    
    PixelListThread(CziToPixel cziAction, Displayer disp, ObjectBox box)
    {
        d = disp;
        cziPixel = cziAction;
       this.box=box;
    }
    
    public void run()
    {            
        while(cziPixel.nextFrameAvalible())
        {   
            cziPixel.loadNextFrame();
            matrix = cziPixel.getMatrix(); 
            
            d.addLastFrame(matrix, cziPixel.getTime());
                     
            box.matrixToContainer(matrix, cziPixel.getTime());

            try {
                Thread.sleep(100);
            } catch(InterruptedException e) {
                return;
            }
                    
        }
        IJ.showMessage("kuniec");
        
    }
    
}
