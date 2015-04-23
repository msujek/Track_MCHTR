/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mechatronika.trackmchtr;

import ij.IJ;

/**
 *
 * @author User
 */
public class ObjectBox {
    ObjectsContainer [] containers;
    Tracker tracker;
    
    ObjectBox(int t_max)
    {
        containers = new ObjectsContainer[t_max];
        IJ.showMessage("box");
        
    }
    
    void matrixToContainer(Matrix ma, int t)
    {
        ma.openCVOperations();
        containers[t]=ma.createObjects();
    }
}
