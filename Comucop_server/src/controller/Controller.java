/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import view.MainWindow;

/**
 *
 * @author root
 */
public class Controller {
    private MainWindow mWin;

    public Controller() {        
        this.mWin = new MainWindow(this);
        mWin.setVisible(true);
    }  
    
    public  static void main (String args[]){
       Controller c = new Controller();
    }
}
