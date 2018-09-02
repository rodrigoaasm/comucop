/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.DataOutputStream;
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
    
    public Socket establishCon() throws IOException{
        Socket serverPer = new Socket(ipServer, 4848);
        return serverPer;
    }  

    public void sendJSON(String json) throws IOException{
        Socket serverPer  = ctrApp.getSocketServer();        
        System.err.println("Tentado enviar");
        
        if(serverPer.isConnected()){
                System.err.println("Conexão existe!");
                Socket serverNonPer = new Socket(ipServer, 4848);
                DataOutputStream out = new DataOutputStream(serverNonPer.getOutputStream());
                out.writeUTF(json);
                out.flush(); 
                out.close();
                serverNonPer.close();        
        }else{
             System.err.println("Restabelecer !");
          //  establishCon();
        }
    }
}
