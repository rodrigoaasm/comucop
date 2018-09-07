/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import view.MainWindow;

/**
 *
 * @author root
 */
public class Controller {
    
    //Controllers 
    private MainWindow mWin;
    private ControllerDep ctrDep;
    private ControllerFuncionario ctrFunc;
    private ManagerSend mSend;
    
    //Sockets de conexão TCP
    private Socket server;
    private Socket broker;

    public Controller() {        
        this.mWin = new MainWindow(this);
        mWin.setVisible(true);
        ctrDep = new ControllerDep(this);
        ctrFunc = new ControllerFuncionario(this);
        ctrDep.LeituraJson();
        ctrFunc.LeituraJson();
        
        try {
            mSend = new ManagerSend(this, InetAddress.getByName("127.0.0.1"));
        } catch (UnknownHostException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    
    public void feedbackLogin(){
        
    }
    
    public void tryEstablishCon() throws IOException {        
        mSend.establishCon();
    }
    
    public ControllerDep getCtrDep() {
        return ctrDep;
    }
    
    public ControllerFuncionario getCtrFunc(){
        return ctrFunc;
    }
    
    public  static void main (String args[]){
       Controller c = new Controller();
    }

    public Socket getSocketServer() {
        return server;
    }

    public Socket getSocketBroker() {
        return broker;
    }     
    
}
