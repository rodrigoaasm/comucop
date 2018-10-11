/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;



import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ClientConRecord;
import org.json.simple.JSONObject;


/**
 *
 * @author Rodrigo Maia
 */
public class ManagerSend {
    
    private Controller ctrServer;
    private ExecutorService threadPool;
    private JSONObject jsonAlive;
    private boolean serviceAlive;
    
    public ManagerSend(Controller ctrServer) {
        this.ctrServer = ctrServer;
        threadPool = Executors.newFixedThreadPool(10);
        jsonAlive = new JSONObject();//Mensagem de vida
        jsonAlive.put("type", "alive");
        startServiceAlive();
        serviceAlive = true;
    }
    
    
    //* Método  responsável por criar e colocar na fila de execução as thread de envio de primeiro contanto*/
    public void first(ClientConRecord clientConRec ){    
        
        Runnable simpleSendingThread;//Instanciando thread de envio de primeiro contato
        simpleSendingThread = new Runnable() {
            @Override
            public void run(){
                JSONObject jsonResp = new JSONObject();//Monta resposta em JSon
                jsonResp.put("type","first");                
                jsonResp.put("key", clientConRec.getKeysServer().getPublicKey().getEncoded());
                
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
        threadPool.submit(simpleSendingThread);//Colocando thread na fila de execução
    }
    
    /*Método responsavel por envio de json para um determinado cliente*/
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
                        Logger.getLogger(ManagerSend.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    DataOutputStream outputStream = null;
                    indexClose.clear();
                    for(ClientConRecord ccr : ctrServer.getClients()){
                        System.out.println("Rodando..");
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
        
   }
}
