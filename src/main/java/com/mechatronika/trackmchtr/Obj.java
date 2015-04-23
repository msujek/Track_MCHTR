/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mechatronika.trackmchtr;


import static com.mechatronika.trackmchtr.ObjectsContainer.pix_x;
import static com.mechatronika.trackmchtr.ObjectsContainer.pix_y;
import static com.mechatronika.trackmchtr.ObjectsContainer.pix_z;
import java.util.ArrayList;
//import java.util.Vector;
import org.bytedeco.javacpp.opencv_core;

/**
 *
 * @author Kinga
 */
public class Obj {
    ArrayList vector;
    int objectId;
    opencv_core.Point3d centre, pixelCentre;
    int howMany;
    double martinMax;
    byte taken;
    byte takenForward, takenBackward;
    Track myTrack;
    Obj next;
    double distToNext, timeToNext;
    int time;
    int feretCube;

    Obj(int time, Coord point, int id)
    {
         vector = new ArrayList<Coord>();
         vector.add(point);
         objectId=id;
         taken=0;
         next=null;
         distToNext=0;
         timeToNext=0;
         this.time=time;
         takenForward=0;
         takenBackward=0;
         myTrack=null;
    }            

    void computeParameters()
    {
        Coord temp;
        int min_x=10000000, min_y=1000000, min_z=100000000; 
        int max_x=0, max_y=0, max_z=0;
        for (int a=0; a<vector.size(); a++)
        {
            temp=(Coord) vector.get(a);
            if(temp.x<min_x)
            {
                min_x=temp.x;
            }
            if(temp.x>max_x)
            {
                max_x=temp.x;
            }
            if(temp.y<min_y)
            {
                min_x=temp.y;
            }
            if(temp.y>max_y)
            {
                max_x=temp.y;
            }
            if(temp.z<min_z)
            {
                min_z=temp.z;
            }
            if(temp.z>max_z)
            {
                min_z=temp.z;
            }
        }

        int feretX, feretY, feretZ;
        feretX=max_x-min_x;
        feretY=max_y-min_y;
        feretZ=max_z-min_z;
        feretCube=feretX*feretY*feretZ;

        double i=0, j=0, k=0;
        double pom;
        for (int a=0; a<vector.size(); a++)
        {
            temp=(Coord)vector.get(a);
            pom=temp.x-min_x;
            i=i+pom;
            pom=temp.y-min_y;
            j=j+pom;
            pom=temp.z-min_z;
            k=k+pom;
        }

        double ray_x, ray_y, ray_z;
        ray_x=i/vector.size();//x
        ray_y=j/vector.size();//y
        ray_z=k/vector.size();//z              

        pixelCentre = new opencv_core.Point3d(ray_x+min_x, ray_y+min_y,ray_z+min_z); //x,y,z   
        centre=new opencv_core.Point3d(pixelCentre.x()*pix_x,pixelCentre.y()*pix_y,pixelCentre.z()*pix_z);
        howMany = vector.size();//ile ma pikseli

        martinMax=0;
        double pom2;
        for(int g=0; g<vector.size(); g++)
        {
            temp=(Coord) vector.get(g);
            pom2=Math.pow(temp.x-pixelCentre.x(), 2)+Math.pow(temp.y-pixelCentre.y(),2)+Math.pow(temp.z-pixelCentre.z(),2);
            if(pom2 > martinMax)
            {
                martinMax=pom2;
            }
        }               
    }
}
