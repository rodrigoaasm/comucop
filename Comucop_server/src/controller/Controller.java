/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import network.ConnectionManager;
import model.ClientRegistration;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;


/**
 *
 * @author root
 */
public class Controller {

   // private MainWindow mWin;
    private ConnectionManager mReceiver; 
    private volatile HashMap<Integer,ClientRegistration> listClients;
    private volatile LinkedList<Runnable> qSleepingListeners; 
    private volatile ManagerDB mDB;
    private ManagerMsg mMsg;
    private LinkedList<JSONObject> queueManMessage;
    private ResourceMonitor rMonitor;

    public Controller() {        
        listClients = new HashMap<>();//instaciando lista de clientes
        qSleepingListeners = new LinkedList();//Instaciando fila de listeners ociosos
        mDB = new ManagerDB(this);
        queueManMessage = new LinkedList();

        try {
            mReceiver = new ConnectionManager(this);//Instaciando gereciador de recebimento e iniciando threads
            mReceiver.start();
            mMsg = new ManagerMsg(this);
            mMsg.start();
            rMonitor = new ResourceMonitor(this);
            rMonitor.start();
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*Método principal do servidor*/
    public static void main(String args[]) {
        Controller c = new Controller();
    }

    public ConnectionManager getConnectionManager() {
        return mReceiver;
    }

    public HashMap<Integer,ClientRegistration> getListClients() {
        return listClients;
    }

    public void addListClients(Integer cod,ClientRegistration cr) {
        this.listClients.put(cod, cr);
    }
    
    public LinkedList<Runnable> getqSleepingListeners() {
        return qSleepingListeners;
    }

    public void addQSleepingListeners(Runnable r) {
        System.err.println("ADD Queeue..");
        this.qSleepingListeners.add(r);
    }
   
    public ManagerDB getmDB() {
        return mDB;
    }
    
    /*Retorna fila de mensagens a serem redirecionadas*/
    public Queue<JSONObject> getQueueManMessage() {
        return queueManMessage;
    }
    
    /*Adiciona requisições a fila do gerenciador de banco*/
    public void getQueueManMessage(JSONObject json) {
        queueManMessage.add(json);
    }


}
