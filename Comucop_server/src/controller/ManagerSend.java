/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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

    public ManagerSend(Controller ctrServer) {
        this.ctrServer = ctrServer;
    }
    
    
    @SuppressWarnings("unchecked")
    public void first(ClientConRecord clientConRec ){    
        
        Runnable simpleSendingThread;
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
                    
                    //Escutando a conexão na espera de resposta
                 //   clientConRec.getSockClient().wait(8000);
                    DataInputStream inputStream = new DataInputStream(clientConRec.getSockClient().getInputStream());
                    JSONParser parser = new JSONParser();//Instaciando o conversor JSON
                    JSONObject jsonReq = (JSONObject) parser.parse(inputStream.readUTF());//Lendo Json de requisição do cliente 
                   // if(jsonReq.remove("type", "login")){
                        System.out.println((String)jsonReq.get("login")+(String)jsonReq.get("password"));
                  //  }                   
                } catch (IOException ex) {
                    Logger.getLogger(ManagerSend.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(ManagerSend.class.getName()).log(Level.SEVERE, null, ex);
             /*   } catch (InterruptedException ex) {
                    Logger.getLogger(ManagerSend.class.getName()).log(Level.SEVERE, null, ex);
                */}
             
                
            }
        };    
        new Thread(simpleSendingThread).start();
    }
    
}
