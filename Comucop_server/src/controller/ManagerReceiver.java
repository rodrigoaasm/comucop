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
    
    //Metódo responsável por ser a main da thread principal de recebimento
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
                    System.out.println("Esse cliente está conectado.");
                    if(clientConRec.isSocketCon()){//A conexão ainda existe  
                        startTimer(client); //Chama thread que funcionará como cronometro                       
                        scan = new DataInputStream(client.getInputStream());//Então lé a requisição JSON                       
                        JSONObject jsonreq = (JSONObject) jsonParser.parse(scan.readUTF());
                        routerWork(clientConRec, jsonreq);//Redirenciona ao serviço certo
                        scan.close();
                        client.close(); 
                    }else{//Tentando relogin
                        System.out.println("Esse cliente está tentado um relogin.");
                        ctrServer.getClients().remove(clientConRec);//removendo ele da lista de conexão
                        clientConRec = new ClientConRecord(client,MyRSAKey.newInstance());//Instaciando registro de conexão                    
                        ctrServer.getmSend().first(clientConRec);//Efetuando primeira comunicação para conexão.
                    }
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
    
    //Metodo resposável por fazer a triagem das requisições
    public void routerWork(ClientConRecord clientConRec,JSONObject jsonreq) throws IOException{
                
        String type = (String)jsonreq.get("type");//Analisa o tipo da operação
        if(type.compareTo("req-depart") == 0 || type.compareTo("exp-to-contacts") == 0){//Operação de resgate de json
            ctrServer.addQueueDB(new ElemQueue(clientConRec, jsonreq));//Coloca na fila do Gerenciador de banco de dados
        }else if(type.compareTo("mensagem") == 0){//Operação de envio de mensagem
            ctrServer.addQueueMessage(new ElemQueue(clientConRec, jsonreq));//Coloca na fila do Gerenciador de mensagem
        }else if(type.compareTo("logout")==0){//O cliente está tentando efetuar novamente o login          
            ctrServer.getClients().remove(clientConRec);//Removendo antigo registro de conexão da lista de clientes
            clientConRec.getSockClient().close();//Fecha conexão    
            clientConRec = null;//excluir registro de cliente            
        }
    }
    
    //Método responsável por encontrar um cliente online
    public ClientConRecord isEstablishedConWithClient(Socket client){
        
        String addr  = client.getInetAddress().getHostAddress();      
  
        for(ClientConRecord c : ctrServer.getClients()){      //busca cliente na lista de onlines          
            if(addr.contains(c.getSockClient().getInetAddress().getHostAddress())){
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

    //Metodo responsável por instanciar a thread que fará a função de timer
    private void startTimer(Socket client) {
        threadListennerPool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);//Dormi por 5s    
                    if(!client.isClosed()) client.close();//fecha conexão se ainda estiver aberta
                } catch (InterruptedException ex) {
                    Logger.getLogger(ManagerReceiver.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ManagerReceiver.class.getName()).log(Level.SEVERE, null, ex);
                }                
            }
        });
    } 
    
}
