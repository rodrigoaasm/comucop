/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import cripth.MyRSAKey;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ClientConRecord;
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
                }else {//Se o cliente não possui uma conexão persistente inicia a autenticação
                    ClientConRecord clientConRec = new ClientConRecord(client,15,MyRSAKey.newInstance());//Instaciando registro de conexão
                    ctrServer.getClients().add(clientConRec);//Guardando registro de conexão
                    ctrServer.getmSend().first(clientConRec);//Efetuando primeira comunicação para conexão.
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
                    if(jsonReq.remove("type", "login")){
                        System.out.println((String)jsonReq.get("login")+(String)jsonReq.get("password"));
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
