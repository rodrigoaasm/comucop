/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class ManagerReceiver extends Thread {
    
    private Controller ctrServer;
    private ServerSocket sockServ;
    
    public ManagerReceiver(Controller ctrServer) throws IOException{
        this.ctrServer = ctrServer;
        sockServ = new ServerSocket(4848);
    }
    
    @Override
    public void run(){
        Socket client = null;
        
        try {
            client = sockServ.accept();
            ctrServer.getClients().add(client);
        } catch (IOException ex) {
            Logger.getLogger(ManagerReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
}
