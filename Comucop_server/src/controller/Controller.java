/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.Socket;
import java.util.ArrayList;
import view.MainWindow;

/**
 *
 * @author root
 */
public class Controller {
    private MainWindow mWin;
    
    private ArrayList<Socket> clients;

    public Controller() {        
        this.mWin = new MainWindow(this);
        mWin.setVisible(true);
        
        clients = new ArrayList<Socket>();
    }  
    
    public  static void main (String args[]){
       Controller c = new Controller();
    }

    public ArrayList<Socket> getClients() {
        return clients;
    }
    
    
}
