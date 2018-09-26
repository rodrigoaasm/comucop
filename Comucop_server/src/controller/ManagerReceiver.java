/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import cripth.MyRSAKey;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ClientConRecord;
import model.ElemQueue;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author root
 */
public class ManagerReceiver extends Thread {
    
    private Controller ctrServer;
    private ServerSocket sockServ;
    private ExecutorService threadListennerPool;
    
    public ManagerReceiver(Controller ctrServer) throws IOException{
        this.ctrServer = ctrServer;
        sockServ = new ServerSocket(4848);
        threadListennerPool = Executors.newFixedThreadPool(20);     
    }
    
    @Override
    public void run(){
        Socket client = null;
         ClientConRecord clientConRec;
        DataInputStream scan;
        JSONParser jsonParser = new JSONParser();
        
        while(true){
            try {
                client = sockServ.accept();//Aceitando conexão TCP de cliente

                if((clientConRec = this.isEstablishedConWithClient(client)) != null){//Se o cliente já possui uma conexão persistente
                    if(clientConRec.getSockClient().isClosed())
                        System.out.println("controller.ManagerReceiver.run() --> tá fechaodo socket");
                    scan = new DataInputStream(client.getInputStream());//Então lé a requisição JSON
                    JSONObject jsonreq = (JSONObject) jsonParser.parse(scan.readUTF());
                    routerWork(clientConRec, jsonreq);//Redirenciona ao serviço certo
                    scan.close();//E fecha conexão não persistente
                    client.close();                
                }else {//Se o cliente não possui uma conexão persistente inicia a autenticação
                    clientConRec = new ClientConRecord(client,MyRSAKey.newInstance());//Instaciando registro de conexão                    
                    ctrServer.getmSend().first(clientConRec);//Efetuando primeira comunicação para conexão.
                }
            } catch (IOException ex) {
                Logger.getLogger(ManagerReceiver.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(ManagerReceiver.class.getName()).log(Level.SEVERE, null, ex);
            }  
        }
    }
    
    public void routerWork(ClientConRecord clientConRec,JSONObject jsonreq){
        String type = (String)jsonreq.get("type");//Analisa o tipo da operação
        if(type.compareTo("req-depart") == 0){//Operação de resgate de json
            ctrServer.addQueueDB(new ElemQueue(clientConRec, jsonreq));
        }  
    }
    
    public ClientConRecord isEstablishedConWithClient(Socket client){
        
        String addr  = new String(client.getInetAddress().getAddress());        
        
        for(ClientConRecord c : ctrServer.getClients()){                
            if(addr.contains(new String(c.getSockClient().getInetAddress().getAddress()))){
                return c;
            } 
        }
        return null;        
    }

    //* Método  responsável por criar e colocar na fila de execução as threads que aguardaram o pacote de autenticação dos clientes*/
    public void createListennerCon(ClientConRecord clientConRec) {
        
        Runnable simpleListennerCon = new Runnable() { // Instaciando thread para escutar conexão
            @Override
            public void run() {
               //Escutando a conexão na espera de resposta                 
                DataInputStream inputStream;                
                try{                         
                    inputStream = new DataInputStream(clientConRec.getSockClient().getInputStream());//Pegando serviço de entrada de stream do socket
                                       
                    JSONParser parser = new JSONParser();//Instaciando o conversor JSON
                    JSONObject jsonReq = (JSONObject) parser.parse(inputStream.readUTF());//Lendo Json de requisição do cliente 
                    String type = (String)jsonReq.get("type");
                    if(type.compareTo("login")==0){
                        ctrServer.addQueueDB(new ElemQueue(clientConRec,jsonReq));
                    }      
                } catch (IOException ex) {
                    Logger.getLogger(ManagerReceiver.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(ManagerReceiver.class.getName()).log(Level.SEVERE, null, ex);
                }           
            }
        };   
        threadListennerPool.submit(simpleListennerCon);//Colocando thread na fila de execução
        
    }
    
}
