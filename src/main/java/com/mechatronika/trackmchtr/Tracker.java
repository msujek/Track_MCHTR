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
public class Tracker {
    ObjectsContainer[] ma;
    float [][] costMa;      
    Track track;
    ArrayList <Track> allTracks;
    float sqDist;
    int difVol;
    ArrayList <Obj> pom1, pom2;
    static double timeInterval=15;
    
    Tracker(ObjectsContainer [] c)
    {
        ma=c;
        allTracks= new ArrayList <Track>();
    }
    
    void linkObjects()
    {
        double min; 
        int min_i=0, min_j=0;
        Obj temp, temp2;
        boolean condition;
        //liczę koszt komorek z a do komorek z a+1
        for(int a=0; a<ma.length-1; a++)
        {
            //macierz kosztów
            costMa=new float[ma[a].allObjects.size()][ma[a+1].allObjects.size()];
            for(int i=0; i<ma[a].allObjects.size(); i++)
            {                
                temp=(Obj)ma[a].allObjects.get(i);                
                //znajdujemy koszty
                for(int j=0; j<ma[a+1].allObjects.size(); j++)
                {                    
                    sqDist=(float) computePixSquareDist((Obj)ma[a].allObjects.get(i),(Obj)ma[a+1].allObjects.get(j));                    
                    difVol=diffVolume((Obj)ma[a].allObjects.get(i),(Obj)ma[a+1].allObjects.get(j));                    
                    
                    if(difVol<0.5*temp.howMany && sqDist<0.001*Math.pow(temp.martinMax,2))
                    {
                        costMa[i][j]=sqDist;                        
                    }
                    else
                    {
                        costMa[i][j]=-1;
                    }
                }
            }
            condition=true;
            while(condition)
            {
                condition=false;
                min=Integer.MAX_VALUE;
                for(int i=0; i<ma[a].allObjects.size(); i++)
                {                
                    temp=(Obj)ma[a].allObjects.get(i);                
                    //znajdujemy koszty
                    for(int j=0; j<ma[a+1].allObjects.size(); j++)
                    { 
                        temp2=(Obj)ma[a+1].allObjects.get(j);
                        if(costMa[i][j]<min && costMa[i][j]!=-1&& !(temp.takenForward==1 || temp2.takenBackward==1))
                        {
                            condition=true;
                            min=costMa[i][j];
                            min_i=i;
                            min_j=j;                            
                        }
                    }
                }
                
                if(condition==true)
                {
                    temp=(Obj)ma[a].allObjects.get(min_i);
                    temp2=(Obj)ma[a+1].allObjects.get(min_j);
                    temp.takenForward=1;                
                    temp2.takenBackward=1;
                    temp.next=temp2;  
                    temp.distToNext=computeDist(temp, temp2);
                    temp.timeToNext=timeInterval;
                    if(temp.myTrack==null)
                    {
                        track=new Track(allTracks.size());
                        track.add(temp);
                        temp.myTrack=track;
                        allTracks.add(track);
                    }
                    temp.myTrack.add(temp2);
                    temp2.myTrack=temp.myTrack;
                }
            } 
            if(a>0 && a<ma.length-1)
            {
                catchHole(a);
            }            
        }
        for(int k=allTracks.size()-1; k>=0; k--)
        {
            if(allTracks.get(k).vector.size()<3)
            {
                System.out.println("Usuwanie tracka");
                allTracks.remove(k);
            }
        }
        
    }
    
    void catchHole(int a)
    {
        Obj temp, temp2;
        boolean condition;
        double min;
        int min_i=0, min_j=0;
        pom1=new ArrayList<Obj>();
        pom2=new ArrayList<Obj>();
        for(int i=0; i<ma[a-1].allObjects.size(); i++)
        {
            temp=(Obj) ma[a-1].allObjects.get(i);
            if(temp.takenForward==0)
            {
                pom1.add(temp);
            }
        }
        for(int i=0; i<ma[a+1].allObjects.size(); i++)
        {
            temp=(Obj) ma[a+1].allObjects.get(i);
            if(temp.takenBackward==0)
            {
                pom2.add(temp);
            }
        }
        costMa=new float[pom1.size()][pom2.size()];
        for(int i=0; i<costMa.length; i++)
        {
            for(int j=0; j<costMa[i].length; j++)
            {                   
                sqDist=(float) computePixSquareDist(pom1.get(i), pom2.get(j));
                difVol=diffVolume(pom1.get(i),pom2.get(j));                    
                if(difVol<0.6*pom1.get(i).howMany && sqDist<0.005*Math.pow(pom2.get(j).martinMax,2))
                {
                    costMa[i][j]=sqDist;                        
                }
                else
                {
                    costMa[i][j]=-1;
                }
            }
        }
        condition=true;
        while(condition==true)
        {
            condition=false;
            min=Integer.MAX_VALUE;
            for(int i=0; i<pom1.size(); i++)
            {                
                temp=(Obj)pom1.get(i);                
                //znajdujemy koszty
                for(int j=0; j<pom2.size(); j++)
                { 
                    temp2=(Obj)pom2.get(j);
                    if(costMa[i][j]<min && costMa[i][j]!=-1&& !(temp.takenForward==1 || temp2.takenBackward==1))
                    {
                        condition=true;
                        min=costMa[i][j];
                        min_i=i;
                        min_j=j;                            
                    }
                }
            }

            if(condition==true)
            {
                temp=(Obj)pom1.get(min_i);
                temp2=(Obj)pom2.get(min_j);
                temp.takenForward=1;                
                temp2.takenBackward=1;
                temp.next=temp2;  
                temp.distToNext=computeDist(temp, temp2);
                temp.timeToNext=2*timeInterval;
                if(temp.myTrack==null)
                {
                        track=new Track(allTracks.size());
                        track.add(temp);                        
                        temp.myTrack=track;
                        allTracks.add(track);                        
                }
                temp.myTrack.add(temp2);
                temp2.myTrack=temp.myTrack;

            }
        }            
    }
    
    double computePixSquareDist(Obj ob1, Obj ob2)
    {
        return Math.pow(ob1.pixelCentre.x() - ob2.pixelCentre.x(), 2)+Math.pow(ob1.pixelCentre.y() - ob2.pixelCentre.y(), 2)+Math.pow(ob1.pixelCentre.z() - ob2.pixelCentre.z(), 2);        
    }
    
    double computeDist(Obj ob1, Obj ob2)
    {
        return Math.sqrt(Math.pow(ob1.centre.x() - ob2.centre.x(), 2)+Math.pow(ob1.centre.y() - ob2.centre.y(), 2)+Math.pow(ob1.centre.z() - ob2.centre.z(), 2));        
    }
    
    int diffVolume(Obj ob1, Obj ob2)
    {
        return Math.abs(ob1.howMany-ob2.howMany);
    }
    
    double [][] computeVelocities()
    {

        double [][] vel = new double[allTracks.size()][4];
        //0-mean, 1-min, 2-max, 3-median
        for(int i=0; i<allTracks.size(); i++)
        {
            vel[i]=allTracks.get(i).computeVelocity();
        }
        return vel;
    }
}
