/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mechatronika.trackmchtr;

import ij.IJ;
import java.nio.*;
import java.util.ArrayList;
import org.bytedeco.javacpp.opencv_core;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_16U;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import org.bytedeco.javacpp.opencv_core.IplImage;
import static org.bytedeco.javacpp.opencv_core.cvConvertScale;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvSize;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import org.bytedeco.javacv.*;


/**
 *
 * @author msujek
 */
public class Matrix {    
   public short[][][] voxel;
   byte [][][]checked;
   opencv_core.IplImage temp;
   IplImage temp2;
   ByteBuffer buffer;
   int index;
   ObjectsContainer container;
   Obj newObject;
   int time;
   Coord help;
   int slice;

    Matrix(int slices)
    {
        slice = slices;
        voxel = new short [slices][512][512]; 
        checked=new byte[slices][512][512];
        for(int i=0; i<slices; i++)
        {
            for(int j=0; j<512; j++)
            {
                for(int k=0; k<512; k++)
                {
                    checked[i][j][k]=0;
                }
            }
        }
        container = new ObjectsContainer();
        
        //temp=cvCreateImage(cvSize(512,512), IPL_DEPTH_16U,1);
        temp=cvCreateImage(cvSize(512,512), IPL_DEPTH_8U,1);
    }
    
    //pierwsze do wywolania
    void openCVOperations()
    {
        for(int a=0; a<slice; a++)
        {
            createIplImage(a); 
            //cvConvertScale(temp, temp2, 255,3);
            //cvConvertScale(temp, temp2);
            cvSmooth(temp, temp, CV_GAUSSIAN,3,3,0,0);
            cvCanny(temp, temp , 10, 30);
            iplImageToMatrix(a);
        }
    }
    
    void createIplImage(int a)
    {
        
        buffer = temp.getByteBuffer();        
        for(int i=0; i<512; i++)
        {
            for(int j=0; j<512; j++)
            {
                index = i * temp.widthStep() + j * temp.nChannels();
                buffer.put(index, (byte) (voxel[a][i][j]/255));
                
            }
        } 
    }
    
    void iplImageToMatrix(int a)
    {
        buffer = temp.getByteBuffer();
        
        for(int i=0; i<temp.width(); i++)
        {
            for(int j=0; j<temp.height(); j++)
            {
                index = i * temp.widthStep() + j * temp.nChannels();
                voxel[a][i][j]=(short) (buffer.get(index) & 0xFF);
            }
        }    
    }
    
    //drugie do wywolania (do tablicy kontenerów)
    ObjectsContainer createObjects()
    {        
        Coord temp;
        for (int i=1; i<slice;i++)
        {                       
            for(int j=512-1; j>=1; j--)
            {
                for (int k=512-1; k>=1;k--)
                {
                    if(voxel[i][j][k]==255)
                    {
                        if(checkBoard(i,j,k)==1)
                        {
                            if(checked[i][j][k]==0)
                            {
                                checked[i][j][k]=1;
                                newObject = new Obj(time,new Coord(i,j,k), container.allObjects.size());
                                for(int u=0; u<newObject.vector.size(); u++)
                                {
                                    //System.out.println("size"+newObject.vector.size());
                                    temp=(Coord) newObject.vector.get(u);
                                    addPoints(newObject, temp.z, temp.x, temp.y);
                                }                                
                                container.allObjects.add(newObject);                                                            
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Mamy "+container.allObjects.size()+ " obiekcikow.");
        return container;
    }
    
    void addPoints(Obj ob, int a, int b, int c)
    {
        //System.out.println("addPoints "+ a + " " + b + " "+ c);// z x y
        for (int i=a-1; i<=a+1; i++)
        {
            for(int j=b-1; j<=b+1; j++)
            {
                for (int k=c-1; k<=c+1; k++)
                {
                    if(!(j==b && k==c && i==a))
                    {
                        if(checkBoard(i,j,k)==1)
                        {
                            if (checked[i][j][k]==0 && voxel[i][j][k]==255)
                            {                                
                                checked[i][j][k]=1;
                                ob.vector.add(new Coord(i,j,k)); 
                            }
                        }
                    }
                }
            }
        }
    }
    
    byte checkBoard(int i, int j, int k)
    {
        if(i>=0 && j>=0 && k>=0 && i<slice && j<512-1 && k<512-1) {
            return 1;}
        else {
            return 0;}
    }
}
