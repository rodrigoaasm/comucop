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
    
    /*Método responsavel por fazer a conexão com servidor*/
    public void establishCon(String user,String password){       
        Runnable threadCon = new Runnable() {//Inicia thread para comunição
            @Override
            public void run() {
                try {  
                    Socket serverPer = new Socket(ipServer, 4848);//Instacia socket
                    ctrApp.setSocketServer(serverPer);//Guarda socket com servidor
                    if(serverPer.isConnected()){
                        DataInputStream dIStr = new DataInputStream(serverPer.getInputStream());
                        dIStr.readUTF();//Lê mensagem de confirmação de conexão com servidor
                        JSONObject jsonReq = new JSONObject();//Escreve json de login
                        jsonReq.put("type","login");
                        jsonReq.put("login",user);
                        jsonReq.put("password",password);
                        DataOutputStream dOStr = new DataOutputStream(serverPer.getOutputStream());//Envia requisição de login
                        dOStr.writeUTF(jsonReq.toJSONString());
                        ctrApp.startReceiver();//Inicia gerenciador de recebimentos
                        dOStr.flush();                        
                    }
                } catch (IOException ex) {
                    ctrApp.throwExp(ex.getMessage());
                }
            }
        };
        new Thread(threadCon).start();//Inicia thread
    } 
    
    /*Método para fechar a conexão com servidor*/ 
    public void finishCon() throws IOException{
        Socket serverPer  = ctrApp.getSocketServer(); 
        //Avalia se ainda está conectado com servidor
        if(serverPer != null){
            if(serverPer.isConnected()){
                    Socket serverNonPer = new Socket(ipServer, 4848);//Inicia socket da conexão não persistente
                    JSONObject jsonreq = new JSONObject();
                    jsonreq.put("type","logout");//Escreve o tipo da requisição
                    DataOutputStream out = new DataOutputStream(serverNonPer.getOutputStream());
                    out.writeUTF(jsonreq.toJSONString());
                    out.flush(); 
                    out.close();//Fecha ambas as conexões 
                    serverNonPer.close();        
            }
        }
    }
 
    /*Método responsavel por transmitir o json*/
    public void sendJSON(String json){                   
        Runnable threadCon = new Runnable() {
            @Override
            public void run() {
                Socket serverPer  = ctrApp.getSocketServer();  //Recebe conexão persistente com servidor    
                if(serverPer.isConnected()){  //Avalia se está autenticado                       
                    Socket serverNonPer;
                    try {
                        serverNonPer = new Socket(ipServer, 4848);  //inicia conexão para requisição             
                        DataOutputStream out = new DataOutputStream(serverNonPer.getOutputStream());
                        out.writeUTF(json);//escreve json 
                        out.flush(); 
                        out.close();
                        serverNonPer.close();   //Fecha conexão de requisição 
                    } catch (IOException ex) {
                        ctrApp.throwExp(ex.getMessage());
                        ctrApp.conBroke();
                    }
                }else{
                    ctrApp.conBroke();
                }
            }
        };
        new Thread(threadCon).start();//Inicia thread escrita acima
    }
}

    


