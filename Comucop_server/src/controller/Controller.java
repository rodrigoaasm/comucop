/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import cripth.MyRSAKey;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ClientConRecord;
import view.MainWindow;

/**
 *
 * @author root
 */
public class Controller {
    private MainWindow mWin;
    
    private volatile ArrayList<ClientConRecord> clientsConRec;
    private ManagerSend mSend;
    private ManagerReceiver mReceiver;
 

    public Controller() {        
        this.mWin = new MainWindow(this);
        mWin.setVisible(true);
        
        clientsConRec = new ArrayList<ClientConRecord>();
        mSend = new ManagerSend(this);
        try {
            mReceiver = new ManagerReceiver(this);
            mReceiver.start();
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    
    public  static void main (String args[]){
       Controller c = new Controller();
    }

    public ArrayList<ClientConRecord> getClients() {
        return clientsConRec;
    }

    public ManagerSend getmSend(){ 
        return mSend;
    }

    public ManagerReceiver getmReceiver() {
        return mReceiver;
    }
    
    
    
    
    
    
}
