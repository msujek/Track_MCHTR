/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mechatronika.trackmchtr;

import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.process.ImageProcessor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.event.*;

/**
 *
 * @author Administrator
 */
public final class GUI extends JFrame {
    
     
    private static JTabbedPane tabPane;
    private static JPanel exportPane;    
    private static JFrame frame1, frame2;
    private static JButton b1,b2,b3,b4;
    JCheckBox colorObj, colorTracks, colorNumbers;
    JTextField pix_min, pix_max;
    JLabel info;
    int min, max;
    JPanel panel, panel2;
    private int z, t;

    Displayer disp;
    Matrix matrix;
    ObjectBox box;
    Results results;
    Tracker tracker;

    CziToPixel  cziAction;
    ExportToExcel exportExcel;
    
    PixelListThread pixelThread;
    
    
    public GUI(final ImagePlus img, final ObjectBox b)
    {
        z=0;
        t=0;
        this.box=b;
        
        //Przycisk ładujący listę pixeli
        b1 = new JButton("Find objects on images");
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                IJ.showMessage("NAAAAH");
                //Chyba niezbyt moge to wrzucić do innego wątku, przynajmniej dokąd nie będzie innej klasy zarządzającej paroma rzeczami...
                cziAction = new CziToPixel(img);                
               // matrix = cziAction.getMatrix();
                
                //displayer
                createDisplayer();
                //disp = new Displayer(box.containers, img.getNSlices(), img.getNFrames());
                //Nowy wątek do wczytania dalszej listy
                pixelThread = new PixelListThread(cziAction, disp, box);
                pixelThread.start(); 
                
                //wrzucanie do ObjectsContainers???
                //containers=new ObjectsContainers()
            }
        });
        
        //kingowe
        panel = new JPanel();
        panel.setLayout(null);             
         
        JButton find_button = new JButton("Find objects on images");
        find_button.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae) {
                showDisplayer();
                info.setText("Objects found");
            }            
        });        
        find_button.setBounds(10, 10, 150, 30);
        panel.add(find_button);
        
        info= new JLabel(" ");
        info.setBounds(160, 10, 200, 30);
        panel.add(info);
        
        JButton preview = new JButton("Preview");
        preview.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                disp.showContours(0,100000000);
            }            
        });
        preview.setBounds(10, 50, 150, 30);
        panel.add(preview);

        pix_min=new JTextField("10");
        pix_min.setBounds(10, 100, 80, 30);
        panel.add(pix_min);
        pix_max = new JTextField("1500"); 
        pix_max.setBounds(100, 100, 80, 30);
        panel.add(pix_max);

        JButton setSize = new JButton("Set size");
        setSize.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
               min=Integer.parseInt(pix_min.getText());
               max=Integer.parseInt(pix_max.getText());
               disp.showContours(min, max);            
            }            
        });
        setSize.setBounds(200, 100, 150, 30);
        panel.add(setSize);
        
        JButton valSize = new JButton("Validate size");
        valSize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                min=Integer.parseInt(pix_min.getText());
                max=Integer.parseInt(pix_max.getText());
                Obj temp;
                for(int i=0; i<box.containers.length; i++)
                {
                    box.containers[i].rejectObjects(min, max);
                    for(int j=0; j<box.containers[i].allObjects.size();j++)
                    {
                        temp=(Obj) box.containers[i].allObjects.get(j);
                        temp.computeParameters();
                    }
                }
                disp.showContours(0,100000000);
                disp.find=colorObj.isSelected();
                disp.repaint();
            }            
        });
        valSize.setBounds(60, 140, 150, 30);
        panel.add(valSize);
        
        JButton trackButton = new JButton("Track objects");
        trackButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae) {
                tracker= new Tracker(box.containers);
                tracker.linkObjects();
                disp.findColours(tracker);
                disp.colorContours(tracker);
                disp.colorTracks(tracker);
                disp.colorNumbers(tracker);
                
                disp.find=colorObj.isSelected();
                disp.repaint();
            }            
        });        
        trackButton.setBounds(60, 190, 150, 30);
        panel.add(trackButton);
        
        colorObj = new JCheckBox("Show objects");
        colorObj.setSelected(true);
        colorObj.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                disp.find=colorObj.isSelected();
                disp.repaint();
            }
        }); 
        colorObj.setBounds(60, 240, 150, 30);
        panel.add(colorObj);
        
        colorTracks = new JCheckBox("Show tracks");
        colorTracks.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                disp.traj=colorObj.isSelected();
                disp.repaint();
            }
        }); 
        colorTracks.setBounds(60, 270, 150, 30);
        panel.add(colorTracks);
        
        colorNumbers = new JCheckBox("Show Ids");
        colorNumbers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                disp.number=colorObj.isSelected();
                disp.repaint();
            }
        }); 
        colorNumbers.setBounds(60, 300, 150, 30);
        panel.add(colorNumbers);
        
        JButton analysis = new JButton("Analysis");
        analysis.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                double [][] velocity = tracker.computeVelocities();
                results = new Results(velocity, img.getShortTitle());
                info.setText("Analysis");
            }            
        });
        analysis.setBounds(60, 350, 150, 30);
        panel.add(analysis);
        
        JButton trackScheme=new JButton("Track Scheme");
        trackScheme.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
               //displayer.trackScheme();
            }            
        });
        trackScheme.setBounds(60, 400, 150,30);
        panel.add(trackScheme);
        //koniec kingowego     

        
        //to raczej niepotrzebne tutaj, JTable w Results
        //przycisk eksportujacy JTable do pliku nazwa_pliku_czu.xlsx
        
        
        setLayout(new BorderLayout());
        
        //główny panel - karty
        tabPane = new JTabbedPane();
        //dodawanie zawartości kolejnych kart
        tabPane.add(b1, ".czi operation");
        //tabPane.add(exportPane, "JTable");
        tabPane.add(panel,"Kinol");
        //add(b1, BorderLayout.SOUTH);
        
    
    add(tabPane, BorderLayout.CENTER);
    //moze zamykac calego Fiji  
    setSize(600,800);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    show();
    pack();  
    }
    
    void createDisplayer()
    {
        frame1=new JFrame("Displayer");        
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        //mała zmiana :) 
        disp = new Displayer(box.containers, cziAction.t_max, cziAction.z_max); 
        //disp = new Displayer(matrices,containers,15, cziAction.z_max);
        disp.actualize(z, t);
        panel.add(disp,BorderLayout.CENTER);
        Container sliders=addSliders();
        panel.add(sliders, BorderLayout.SOUTH);
        
        frame1.add(panel);
        frame1.setSize(520, 600);
        frame1.setResizable(true);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame1.setVisible(true);
    }
    
    void showDisplayer()
    {
        frame1.setVisible(true);
    }
    
    JSlider time_slider, z_slider;
    Container addSliders()
    {
        Container vert1 = Box.createVerticalBox();
        
        Container hor1= Box.createHorizontalBox();
        JLabel time_label = new JLabel("T");
        hor1.add(time_label);
        time_slider = new JSlider(JSlider.HORIZONTAL, 0, cziAction.t_max-1, 0);
        time_slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent ce) {
                t = time_slider.getValue();
                disp.actualize(z, t);
            }
        });
        hor1.add(time_slider);
        vert1.add(hor1);
        
        Container hor2= Box.createHorizontalBox();
        JLabel z_label = new JLabel("Z");
        hor2.add(z_label);
        z_slider = new JSlider(JSlider.HORIZONTAL, 0, cziAction.z_max-1, 0);
        z_slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent ce) {
                z = z_slider.getValue();
                disp.actualize(z, t);
            }
        });
        hor2.add(z_slider);
        vert1.add(hor2);
        
        return(vert1);
    }
    
  
    
    
    
}
