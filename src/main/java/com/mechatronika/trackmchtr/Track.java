/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mechatronika.trackmchtr;
import java.util.ArrayList;
//import java.util.Vector;

/**
 *
 * @author Kinga
 */
public class Track {
    ArrayList <Obj> vector;
    int trackId;
    Track(int id)
    {
        vector = new ArrayList<Obj>();
        trackId=id;
    }
    
    void add(Obj ob)
    {
        vector.add(ob);
    }
    
    double [] computeVelocity()
    {
        double []velocity = new double[4];//min, max, mean, median
        //calculate whole distance
        double dist=0;
        double time=0;
        double min=Double.MAX_VALUE;
        double max=0;
        for(int i=0; i<vector.size(); i++)
        {
            dist=dist+vector.get(i).distToNext;
            time=time+vector.get(i).timeToNext;
            if(vector.get(i).distToNext/vector.get(i).timeToNext>max)
            {
                max=vector.get(i).distToNext/vector.get(i).timeToNext;
            }
            if(vector.get(i).distToNext/vector.get(i).timeToNext<min)
            {
                min=vector.get(i).distToNext/vector.get(i).timeToNext;
            }
        }
        System.out.println("Przebyty dystans "+ dist+ " w czasie "+ time);
        velocity[0]=min;
        velocity[1]=max;
        velocity[2]=dist/time;
        velocity[3]=(max-min)/2;
        
        return velocity;
    }
}
