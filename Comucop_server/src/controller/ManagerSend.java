/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.parser.JSONParser;
import model.ClientConRecord;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Rodrigo Maia
 */
public class ManagerSend {
    
    private Controller ctrServer;
    private ExecutorService threadPool;

    public ManagerSend(Controller ctrServer) {
        this.ctrServer = ctrServer;
        threadPool = Executors.newFixedThreadPool(10);
    }
    
    
    //* Método  responsável por criar e colocar na fila de execução as thread de envio de primeiro contanto*/
    public void first(ClientConRecord clientConRec ){    
        
        Runnable simpleSendingThread;//Instanciando thread de envio de primeiro contato
        simpleSendingThread = new Runnable() {
            @Override
            public void run(){
                JSONObject jsonResp = new JSONObject();//Monta resposta em JSon
                jsonResp.put("type","first");
                jsonResp.put("pin",15);
                jsonResp.put("key", clientConRec.getKeysServer().getPublicKey().getEncoded());
                
                DataOutputStream outputStream = null;
                try {
                    outputStream = new DataOutputStream(clientConRec.getSockClient().getOutputStream()); //recebendo stream de saída
                    outputStream.writeUTF(jsonResp.toJSONString());//Escreve na stream
                    outputStream.flush();//Envia stream para o client
                    ctrServer.getmReceiver().createListennerCon(clientConRec);//chama gerenciador de recebimentos para aguardar a resposta do cliente 
                    
                } catch (IOException ex) {
                    Logger.getLogger(ManagerSend.class.getName()).log(Level.SEVERE, null, ex);
                }              
            }
        };    
        threadPool.submit(simpleSendingThread);//Colocando thread na fila de execução
    }
    
}