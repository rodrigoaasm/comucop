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
    private ControllerDep ctrDep;

    public Controller() {        
        this.mWin = new MainWindow(this);
        mWin.setVisible(true);
        ctrDep = new ControllerDep(this);
    }  
    
    public ControllerDep getCtrDep() {
        return ctrDep;
    }
    
    public  static void main (String args[]){
       Controller c = new Controller();
    }
}
