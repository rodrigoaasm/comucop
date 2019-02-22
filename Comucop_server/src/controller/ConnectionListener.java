/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ClientConRecord;
import model.ClientRegistration;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 *
 * @author Rodrigo Maia
 */
public class ConnectionListener implements Runnable{
    
    public final static int SLEEPING = 0;
    public final static int WAITING_FOR_AUTH = 1;
    public final static int LISTINING = 2;
    
    private Controller ctrServer;
    private int status;
    private ClientRegistration client;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private LinkedList qPackToBeSent;
    private JSONParser parser;
    
    public ConnectionListener(Controller ctrServer) {
        this.ctrServer = ctrServer;
        this.status = SLEEPING;
        qPackToBeSent = new LinkedList();
        parser = new JSONParser();//Instaciando o conversor JSON
    }    
    
    @Override
    public void run() {
        while(true){
            System.err.println("Status -->"+ status);
            if(status == SLEEPING){
               try { Thread.sleep(20);} catch (InterruptedException ex) {}  
            }else{
                if(this.client.getClientSock().isClosed()){//Se o cliente fechar a conexão retira ele do serviço
                    System.err.println("Conexão perdida");
                    closeCon();
                }
                else{
                    this.listener();//começa escutar a conexão
                }
            }
        }
    }

    public void wakeUp(ClientRegistration client) {
        this.client = client;
        try {
            inputStream = new DataInputStream(client.getClientSock().getInputStream());//Pegando serviço de entrada de stream do socket 
            outputStream = new DataOutputStream(client.getClientSock().getOutputStream());//Pegando serviço de saida de stream do socket 
        } catch (IOException ex) {
            Logger.getLogger(ConnectionListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.status = WAITING_FOR_AUTH;
    }
    
    public void listener(){         
        try{        
            System.err.println("Aguardando..");
            if(inputStream.available()> 0){ //Se houver dados na entrada                                     
                JSONObject jsonReq = (JSONObject) parser.parse(inputStream.readUTF());//Lendo Json de requisição do cliente 
                routerWork(jsonReq); //Fazendo a triagem do pacote
            }
            
            if(!qPackToBeSent.isEmpty()){//Se tiver dados a serem enviados na fila 
                
            }
        } catch (ParseException ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            closeCon();     
        } 
    }  
    
    public void routerWork(JSONObject jsonreq) throws IOException{
                
        String type = (String)jsonreq.get("type");
        if(type.compareTo("login")==0){
            System.err.println("Login Requisitado");
            this.status = LISTINING;
        }else if(status == WAITING_FOR_AUTH){
            System.err.println("Req. Fechamento");
            closeCon();//Fechando conexão com o client             
        }else{
            System.err.println("Estamos operando");   
        }
    }
    
    public void closeCon(){
        System.err.println("Fechando comunicação");
        if(status != SLEEPING){
            try {
                this.status = SLEEPING; // mudnaod status de operação 
                this.client.getClientSock().close();//Fechando conexão
                this.ctrServer.getListClients().remove(client);//Removendo registor de client
                this.client = null;
                this.qPackToBeSent.clear();
                this.ctrServer.addQSleepingListeners(this);//Se adicionando a fila de ociosas                 
            } catch (IOException ex) {
                Logger.getLogger(ConnectionListener.class.getName()).log(Level.SEVERE, null, ex);
            }     
             
        }
    }
}        
        
        
       /* JSONObject jsonResp = new JSONObject();//Monta resposta em JSon
        jsonResp.put("type","first");                
        //  jsonResp.put("key", clientConRec.getKeysServer().getPublicKey().getEncoded());
                
                DataOutputStream outputStream = null;
                try {
                    outputStream = new DataOutputStream(clientConRec.getSockClient().getOutputStream()); //recebendo stream de saída
                    outputStream.writeUTF(jsonResp.toJSONString());//Escreve na stream
                    outputStream.flush();//Envia stream para o client
                    ctrServer.getmReceiver().createListennerCon(clientConRec);//chama gerenciador de recebimentos para aguardar a resposta do cliente 
                    
                } catch (IOException ex) {
                    try {
                        clientConRec.close();
                    } catch (IOException ex1) {}
                }              
            }
        };    
        threadPool.submit(simpleSendingThread);//Colocando thread na fila de execução*/
    
    
    /*Método responsavel por envio de json para um determinado cliente
   public void sendJSON(ClientConRecord clientConRec,JSONObject json) {
       Runnable simpleSendingThread;//Instanciando thread de envio
        simpleSendingThread = new Runnable() {
            @Override
            public void run(){  
                DataOutputStream outputStream = null;
                try {
                    outputStream = new DataOutputStream(clientConRec.getSockClient().getOutputStream()); //recebendo stream de saída
                    outputStream.writeUTF(json.toJSONString());//Escreve na stream
                    outputStream.flush();//Envia stream para o client
                    outputStream = null;
                    if(json.remove("type","login") && json.remove("status","0")){//Se este json representar uma falha na autenticação fecha a conexão                        
                        clientConRec.getSockClient().close();
                    }               
                } catch (IOException ex) {
                    ctrServer.getClients().remove(clientConRec);
                    try {
                        clientConRec.close();
                    } catch (IOException ex1) {}
                }              
            }
        };    
        threadPool.submit(simpleSendingThread);//Colocando thread na fila de execução
   }
   
   //Método que da inicio a verificação de vida dos clientes
    private void startServiceAlive() {  
        Runnable simpleSendingThread;//Instanciando thread de envio
        simpleSendingThread = new Runnable() {
            @Override
            public void run(){  
                ArrayList<ClientConRecord> indexClose = new ArrayList<>();
                while(serviceAlive){
                    try {
                        Thread.sleep(10000);//Tempo de gatilho                        
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ConnectionListener.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    DataOutputStream outputStream = null;
                    indexClose.clear();
                    for(ClientConRecord ccr : ctrServer.getClients()){                        
                        try {                            
                            outputStream = new DataOutputStream(ccr.getSockClient().getOutputStream()); //recebendo stream de saída                            
                            outputStream.writeUTF(jsonAlive.toJSONString());//Escreve na stream
                            outputStream.flush();//Envia stream para o client
                            outputStream = null;              
                        } catch (IOException ex) {//Se o cliente não responder
                            indexClose.add(ccr);//guarda ele em uma lista para ser fechado
                        }              
                    }
                    
                    //Passando pela lista de clientes que não responderam para fecha-los
                    for(ClientConRecord i : indexClose){
                        ctrServer.getClients().remove(i);
                        try {
                            i.close();
                        } catch (IOException ex1) {}
                    }
                }
            }
        };
        threadPool.submit(simpleSendingThread);
        
   }*/



