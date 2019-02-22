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
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author root
 */
public class ManagerConnection {    
    private Controller ctrApp;
    private InetAddress ipServer; 
    private Socket sockServer;
    private DataOutputStream outputStreamServ;
    private DataInputStream inputStreamServ;    
    private LinkedList<JSONObject> qPackSent;
    private Thread listener;
    
    
    public ManagerConnection(Controller ctrApp, InetAddress ipServer){
        this.ctrApp = ctrApp;
        this.ipServer = ipServer;
        this.qPackSent = new LinkedList<JSONObject>();
    }

    public void setIpServer(String ipServer) throws UnknownHostException {
        this.ipServer =  InetAddress.getByName(ipServer);
    }  
        
    /*Método responsavel por fazer a conexão com servidor*/
    public void establishCon(String user,String password) throws IOException{      
        Runnable r = new Runnable() {
            @Override
            public void run() {                                 
                if(login(user,password)){
                    while(sockServer.isConnected()){
                        System.err.println("Escutando");
                        try {
                            if(inputStreamServ.available()>0){
                                
                            }
                            if(!qPackSent.isEmpty()){
                                JSONObject jsonReq = qPackSent.pop();
                                outputStreamServ.writeUTF(jsonReq.toJSONString());
                                outputStreamServ.flush();
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(ManagerConnection.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        };
        listener = new Thread(r);
        listener.start();
        
    }
    
    public boolean login(String user,String password){
        try {
            sockServer = new Socket(ipServer, 4848);//Instacia socket        
            outputStreamServ = new DataOutputStream(sockServer.getOutputStream());//Envia requisição de login
            inputStreamServ = new DataInputStream(sockServer.getInputStream());
            JSONParser parser = new JSONParser();//Instaciando o conversor JSON

            JSONObject jsonReq = new JSONObject();//Escreve json de login
            jsonReq.put("type","login");
            jsonReq.put("login",user);
            jsonReq.put("password",password);

            outputStreamServ.writeUTF(jsonReq.toJSONString());
            outputStreamServ.flush();

            JSONObject jsonResp = (JSONObject) parser.parse(inputStreamServ.readUTF());
            if(jsonResp.containsKey("status")){
                if(Integer.parseInt((String)jsonResp.get("status"))== 0){
                    System.err.println("Erro no login");
                    finishCon();
                    return false;
                }
            } else{ finishCon(); return false;}  
            
            return true;            
        } catch (Exception ex) {
            return false;
        }
    }
        
    public void finishCon() throws IOException{           
        if(sockServer != null){
             if(sockServer.isConnected()){
                JSONObject jsonreq = new JSONObject();
                jsonreq.put("type","logout");//Escreve o tipo da requisição
                        
                outputStreamServ.writeUTF(jsonreq.toJSONString());
                outputStreamServ.flush(); 
                outputStreamServ.close();
                sockServer.close();//Fecha ambas as conexões               
            }
        }
    }
        
        
        
} 
    
  /*  @Override
    public void run(){
        DataInputStream dIStr = null;          
        
        if(ctrApp.getSocketServer().isConnected()){//Verifica se a conexão ainda existe           
            JSONObject jsonResp= null;            
            try{
                //Preparando stream de entrada e conversor json de resposta
                dIStr = new DataInputStream(ctrApp.getSocketServer().getInputStream());          
                JSONParser parser = new JSONParser();//Instaciando o conversor JSON
                while(ctrApp.getSocketServer().isConnected()){//Enquanto a conexão estiver viva, a escuta-a
                    //Lendo Json de resposta do servidor 
                    try {
                        jsonResp = (JSONObject) parser.parse(dIStr.readUTF());
                        //Analisa de qual operação é a resposta
                        String type = (String)jsonResp.get("type");
                        if(type.compareTo("login")==0){//Operação de login
                            ctrApp.feedbackLogin(jsonResp);
                        }else if(type.compareTo("req-depart")==0){//Operação requisição de departamentos
                            ctrApp.getCtrDep().LeituraJson(jsonResp);
                            ctrApp.getmWin().addDeps();
                        }else if(type.compareTo("exp-to-contacts")==0){//Operação requisição de departamentos    
                            ctrApp.getListaConts().clear();                        
                            ctrApp.toExpCellDepartsReq(jsonResp);
                        }else if(type.compareTo("mensagem")==0){//Operação requisição de departamentos  
                             ctrApp.leituraJsonMsg(jsonResp);
                        }else if(type.compareTo("backup")==0){
                            ctrApp.leituraJsonMsgOff(jsonResp);
                        }
                    }catch (ParseException ex) {                        
                        ctrApp.throwExp(ex.getMessage());
                        ctrApp.conBroke();
                    } 
                }
                ctrApp.conBroke();//Notificando quebra na conexão
            }catch (IOException ex) {                
                ctrApp.throwExp(ex.getMessage());  //Apresentado erro para usuário
                ctrApp.conBroke();//Notificando quebra na conexão
            }       
        }else{
            ctrApp.conBroke();//notificando quebra na conexão
        }     
    }
    
    
   

 
    
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
    }*/


    


