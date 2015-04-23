/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mechatronika.trackmchtr;

import ij.IJ;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JComponent;

/**
 *
 * @author Mv
 */
public class Displayer extends JComponent{

    boolean find, number, traj;    
    int z, t;
    int z_max, t_max, height, width;    
    BufferedImage [][] allImages, numberImages, findingsImages, tracksImages;
    Matrix matrix;
    Color [] colours;
    Graphics2D gr;
    ObjectsContainer[]containers;
    
    Displayer(ObjectsContainer[]c, int a, int b)
    {
        z=0;
        t=0;
        this.z_max=b;
        this.t_max =a;
        width=512;
        height=512;
        containers=c;
        
        allImages = new BufferedImage[z_max][t_max];//[z][t]
        IJ.showMessage("Tworzenie obrazkow");
        
        for(int i=0; i<t_max; i++)//czas
        {
            for(int j=0; j<z_max; j++)//os z
            {
                allImages[j][i] = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_GRAY);
                //IJ.showMessage("Huehue "+ i + " " + j);
            }
        }
        
        repaint();        
    }
    
    void addLastFrame(Matrix matrix, int i)
    {         
        for(int j=0; j<z_max; j++)//os z
        {
            int[] iArray=new int[1];
            WritableRaster raster;
            raster=allImages[j][i].getRaster();
            for(int k=0; k<width; k++)//os x
            {
                for(int l=0; l<height; l++)//os y                    {
                {                            
                    iArray[0]=matrix.voxel[j][k][l];
                    raster.setPixel(l, k, iArray);
                }
            }
        }
        repaint();
    }
    /*
    void showChanges()
    {
        for(int i=0; i<t_max; i++)//czas
        {
            for(int j=0; j<z_max; j++)//os z
            {
                gr=findingsImages[j][i].createGraphics();
                gr.setColor(Color.WHITE);
                for(int k=0; k<width; k++)//os x
                {
                    for(int l=0; l<height; l++)//os y
                    {                           
                        if(matrices.get(i).voxel[j][k][l]==255)//tutaj na 16bitowy
                        {
                            gr.fillRect(l, k, 1, 1);                            
                        }                                               
                    }
                }
            }
        }
        repaint();
    }*/
    
    void showContours(int min, int max)
    {        
        clearAll(findingsImages);
        Obj temp;
        Coord temp2;
        for(int i=0; i<t_max; i++)
        {
            for(int j=0; j<containers[i].allObjects.size(); j++)
            {
                temp=(Obj) containers[i].allObjects.get(j);
                if(temp.vector.size() > min && temp.vector.size()<max)
                {
                    for(int k=0; k<temp.vector.size(); k++)
                    {
                        temp2=(Coord) temp.vector.get(k);
                        //findingsImages[temp2.z][i].setRGB(temp2.x, temp2.y, 0);
                        gr=findingsImages[temp2.z][i].createGraphics();
                        gr.setColor(Color.WHITE);
                        gr.fillRect(temp2.y, temp2.x, 1,1);
                    } 
                }                               
                //gr=findingsImages[temp.vector.][].createGraphics();
                //gr.setColor(Color.WHITE);                
            }
        }
        repaint();
    }
    
    void findColours(Tracker tracker)
    {
        Random generator=new Random();
        colours = new Color[tracker.allTracks.size()];
        int r,g,b,a;
        for(int i=0; i<colours.length; i++)
        {            
            r= generator.nextInt(255);
            if(r<70)
            {
                r=r+50;
            }
            g=generator.nextInt(255);
            if(g<70)
            {
                g=g+50;
            }
            b=generator.nextInt(255);
            if(b<70)
            {
                b=b+50;
            }
            a=255;
            colours[i]=new Color(r,g,b,a);
        }
        
    }
    
    void colorContours(Tracker tracker)
    {
        clearAll(findingsImages);
        Obj temp;
        Coord coord;
        for(int i=0; i<tracker.allTracks.size(); i++)
        {
            //System.out.println("Rozmiar "+tracker.allTracks.size());
            for(int j=0; j<tracker.allTracks.get(i).vector.size(); j++)
            {
                temp=tracker.allTracks.get(i).vector.get(j);
                for(int k=0; k<temp.vector.size(); k++)
                {
                    //jeden Coord - temp.vector.get(j).vector.get(k).
                    //temp.vetor.get(k).x,y,z oraz czas-temp.time
                    coord=(Coord) temp.vector.get(k);
                    gr=(Graphics2D) findingsImages[coord.z][temp.time].getGraphics();
                    gr.setColor(colours[i]);
                    gr.fillRect(coord.y, coord.x, 1, 1);
                }
            }
        }
        repaint();
    }
    
    void colorTracks(Tracker tracker)
    {
        clearAll(tracksImages);
        Obj temp;
        Coord coord;
        for(int i=0; i<tracker.allTracks.size(); i++)
        {
            //System.out.println("Rozmiar "+tracker.allTracks.size());
            for(int j=0; j<tracker.allTracks.get(i).vector.size(); j++)
            {
                System.out.println(i + " " + j);
                temp=tracker.allTracks.get(i).vector.get(j);//obj
                if(temp.next!=null)
                {
                    for(int l=0; l<z_max; l++)
                    {
                        for(int k=0; k<t_max; k++)
                        {
                            gr=(Graphics2D)tracksImages[l][k].getGraphics();
                            gr.setColor(colours[i]);
                            gr.drawLine((int)temp.pixelCentre.y(), (int)temp.pixelCentre.x(), (int)temp.next.pixelCentre.y(), (int)temp.next.pixelCentre.x());
                        }
                    }
                }
            }
        }
        repaint();
    }
    
    void colorNumbers(Tracker tracker)
    {
        clearAll(numberImages);
        Obj temp;
        Coord coord;
        String text;
        double x_pom, y_pom;
        for(int i=0; i<tracker.allTracks.size(); i++)
        {
            //System.out.println("Rozmiar "+tracker.allTracks.size());
            for(int j=0; j<tracker.allTracks.get(i).vector.size(); j++)
            {
                System.out.println(i + " " + j);
                temp=tracker.allTracks.get(i).vector.get(j);//obiekt
                //for(int k=0; k<temp.vector.size(); k++)
                //{
                    //jeden Coord - temp.vector.get(j).vector.get(k).
                    //temp.vetor.get(k).x,y,z oraz czas-temp.time
                    //coord=(Coord) temp.vector.get(k);
                if(temp.myTrack!=null){
                    text = String.valueOf(temp.myTrack.trackId);
                    for(int l=0; l<z_max; l++)
                    {
                        gr=(Graphics2D) numberImages[l][temp.time].getGraphics();
                        gr.setColor(colours[i]);
                        if(temp.pixelCentre.x()>250)
                        {
                            x_pom=temp.pixelCentre.x()-20;
                        }
                        else
                        {
                            x_pom=temp.pixelCentre.x()+10;
                        }
                        if(temp.pixelCentre.y()>250)
                        {
                            y_pom=temp.pixelCentre.y()-20;
                        }
                        else
                        {
                            y_pom=temp.pixelCentre.y()+10;
                        }
                        gr.drawString(text, (int)y_pom, (int)x_pom);
                    }
                }
                //}
            }
        }
        repaint();
    }
    
    void actualize(int z, int t)
    {
        this.z=z;
        this.t=t;
        repaint();
        //System.out.println("Zmiana na "+ z + " " + t);
    }
    
     void clearAll(BufferedImage[][]layer)
    {
        for(int i=0; i<z_max; i++)
        {
            for(int j=0; j<t_max; j++)
            {
                gr= layer[i][j].createGraphics();
                gr.setBackground(new Color(0,0,0,0));
                gr.clearRect(0, 0, layer[i][j].getWidth(), layer[i][j].getHeight());
            }
        }
        repaint();
    }

    public void paintComponent(Graphics g)
    {    
        //IJ.showMessage("paint");
        if(numberImages==null)
        {
            numberImages = new BufferedImage[z_max][t_max];
            for(int i=0; i<z_max; i++)
            {
                for(int j=0; j<t_max; j++)
                {
                    numberImages[i][j]= new BufferedImage(getSize().width, getSize().height,BufferedImage.TYPE_INT_ARGB);
                }
            }            
        }
        if(tracksImages==null)
        {
            tracksImages = new BufferedImage[z_max][t_max];
            for(int i=0; i<z_max; i++)
            {
                for(int j=0; j<t_max; j++)
                {
                  tracksImages[i][j]= new BufferedImage(getSize().width, getSize().height,BufferedImage.TYPE_INT_ARGB);
              
                }
            }            
        }
        if(findingsImages==null)
        {
            findingsImages= new BufferedImage[z_max][t_max];
            for(int i=0; i<z_max; i++)
            {
                for(int j=0; j<t_max; j++)
                {
                 findingsImages[i][j]= new BufferedImage(getSize().width, getSize().height,BufferedImage.TYPE_INT_ARGB);              
                }
            }
        }
        
        g.drawImage(allImages[z][t], 0, 0, null);
        if(find)
            g.drawImage(findingsImages[z][t], 0, 0, null);
        if(number)
            g.drawImage(numberImages[z][t], 0, 0, null);
        if(traj)
            g.drawImage(tracksImages[z][t], 0, 0, null);
    }
}
