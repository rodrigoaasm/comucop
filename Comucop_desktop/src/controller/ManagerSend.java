/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author root
 */
public class ManagerSend {
    
    private Controller ctrApp;
    private InetAddress ipServer;     
    
    public ManagerSend(Controller ctrApp, InetAddress ipServer){
        this.ctrApp = ctrApp;
        this.ipServer = ipServer;
    }
    
    public void establishCon() throws IOException{
        Socket server  = ctrApp.getSocketServer();
        
        server = new Socket(ipServer, 4848);
    }  

    public void sendJSON(String json) throws IOException{
        Socket server  = ctrApp.getSocketServer();
        
        if(server.isConnected()){
            System.err.println(json);
        }else{
            establishCon();
        }
    }
}
