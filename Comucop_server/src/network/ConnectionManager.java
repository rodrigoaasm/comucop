/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;


import controller.Controller;
import network.ConnectionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ClientRegistration;

/**
 *
 * @author root
 */
public class ConnectionManager extends Thread {
    
    private Controller ctrServer;
    private ServerSocket sockServ;
    private ExecutorService threadListennerPool;
    private Integer nThreadCreated;
        
    public ConnectionManager(Controller ctrServer) throws IOException{
        this.ctrServer = ctrServer;
        sockServ = new ServerSocket(4848);     
        threadListennerPool = Executors.newFixedThreadPool(20);  
        nThreadCreated = 0;
    }

    public Integer getnThreadCreated() {
        return nThreadCreated;
    }
    
    //Metódo responsável por ser a main da thread principal de recebimento
    @Override
    public void run(){
        Socket client = null;
        while(true){
            try {
                client = sockServ.accept();//Aceitando conexão TCP de cliente        
                
                ConnectionListener listener = null;
                if(ctrServer.getqSleepingListeners().isEmpty()){//Se a fila estiver vazia 
                    listener = new ConnectionListener(ctrServer);//Cria listener que vai escutar a conexão
                    threadListennerPool.submit(listener);    
                    nThreadCreated++;
                }else{//se tiver listener ocioso na fila recicla o mesmo
                    listener = (ConnectionListener) ctrServer.getqSleepingListeners().poll();
                    System.err.println(">" + ctrServer.getqSleepingListeners().size());
                }                    
                ClientRegistration cr = new ClientRegistration(client,listener);//criando registro
                System.err.println("Aceitando conexão");
                listener.wakeUp(cr);//Acordado listener 
               // ctrServer.addListClients(cr);//Registrando cliente no sistema.                            
            } catch (IOException ex) {
                Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
            }               
        }
    }
}
  