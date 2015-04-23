/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mechatronika.trackmchtr;

import java.util.ArrayList;
//import java.util.Vector;
import org.bytedeco.javacpp.opencv_core;

/**
 *
 * @author Kinga
 */
public class ObjectsContainer {
    int time;    
    public static ArrayList<Obj> allObjects;
    static float pix_x=(float) 0.1323;
    static float pix_y=(float) 0.1323;
    static float pix_z=(float) 0.6;
    
    ObjectsContainer()
    {
        allObjects=new ArrayList<Obj>();
    }
    
    void rejectObjects(int min, int max)
    {
        Obj temp;
        for(int i=allObjects.size()-1; i>=0; i--)
        {
            temp=(Obj) allObjects.get(i);
            if(temp.vector.size()>max || temp.vector.size()<min)
            {
                allObjects.remove(i);
            }
        }
        System.out.println("A teraz mamy "+allObjects.size()+" obiektow");
    }    
}
