/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;

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
    
    public void establishCon(){
       
        Runnable threadCon = new Runnable() {
            @Override
            public void run() {
                try {  
                    Socket serverPer = new Socket(ipServer, 4848);
                    if(serverPer.isConnected()){
                        DataInputStream dIStr = new DataInputStream(serverPer.getInputStream());
                        dIStr.readUTF();
                        JSONObject jsonReq = new JSONObject();
                        jsonReq.put("type","login");
                        jsonReq.put("login","rodrigoasmaia@gmail.com");
                        jsonReq.put("password","qweasd123");
                        DataOutputStream dOStr = new DataOutputStream(serverPer.getOutputStream());
                        dOStr.writeUTF(jsonReq.toJSONString());
                        dOStr.flush();                        
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ManagerSend.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        new Thread(threadCon).start();

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
