/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import cripth.MyRSAKey;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ClientConRecord;
import org.json.simple.JSONObject;

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
        DataInputStream scan;
        
        while(true){
            try {
                client = sockServ.accept();//Aceitando conexão TCP de cliente

                if(this.isEstablishedConWithClient(client)){//Se o cliente já possui uma conexão persistente
                    System.out.println("Esse cara tá conectado");//Ler a requisição do mesmo
                    scan = new DataInputStream(client.getInputStream());
                    System.out.println(scan.readUTF());
                    scan.close();//E fecha conexão não persistente
                    client.close();                
                }else {
                    System.out.println("Conexão de login");
                    ClientConRecord clientConRec = new ClientConRecord(client,15,MyRSAKey.newInstance());//Instaciando registro de conexão
                    ctrServer.getClients().add(clientConRec);//Guardando registro de conexão
                    ctrServer.getmSend().first(clientConRec);
                }
            } catch (IOException ex) {
                Logger.getLogger(ManagerReceiver.class.getName()).log(Level.SEVERE, null, ex);
            }  
        }
    }
    
    public boolean isEstablishedConWithClient(Socket client){
        
        String addr  = new String(client.getInetAddress().getAddress());        
        
        for(ClientConRecord c : ctrServer.getClients()){          
            System.out.println(addr + "==" + new String(c.getSockClient().getInetAddress().getAddress() ));
            if(addr.contains(new String(c.getSockClient().getInetAddress().getAddress()))){
                return true;
            } 
        }
        return false;        
    }
    
}
