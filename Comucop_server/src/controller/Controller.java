/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.ClientRegistration;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ClientConRecord;
import model.ElemQueue;
import view.MainWindow;

/**
 *
 * @author root
 */
public class Controller {

    private MainWindow mWin;
    private ConnectionManager mReceiver; 
    private volatile ArrayList<ClientRegistration> listClients;
    private volatile LinkedList<Runnable> qSleepingListeners; 

    public Controller() {
        this.mWin = new MainWindow(this);
        mWin.setVisible(true);
        
        listClients = new ArrayList<ClientRegistration>();//instaciando lista de clientes
        qSleepingListeners = new LinkedList();//Instaciando fila de listeners ociosos
        /*queueManMesage = new LinkedList();

        mSend = new ConnectionListener(this);//Instaciando Gerenciador de envios
        */
        try {
            mReceiver = new ConnectionManager(this);//Instaciando gereciador de recebimento e iniciando threads
            mReceiver.start();
           /* mDb = new ManagerDB(this);
            mDb.start();
            mMsg = new ManagerMsg(this);
            mMsg.start();*/
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*Método principal do servidor*/
    public static void main(String args[]) {
        Controller c = new Controller();
    }
    
    public ArrayList<ClientRegistration> getListClients() {
        return listClients;
    }

    public void addListClients(ClientRegistration cr) {
        this.listClients.add(cr);
    }
    
    public LinkedList<Runnable> getqSleepingListeners() {
        return qSleepingListeners;
    }

    public void addQSleepingListeners(Runnable r) {
        System.err.println("ADD Queeue..");
        this.qSleepingListeners.add(r);
    }
   
    
    

    
    /*Retorna gerenciador de envios
    public ConnectionListener getmSend() {
        return mSend;
    }*/

    /*Retorna gerenciador de recebimentos
    public ConnectionManager getmReceiver() {
        return mReceiver;
    }*/

    /*Retorna fila de requisições de banco
    public Queue<ElemQueue> getQueueManDB() {
        return queueManDB;
    }*/

    /*Adiciona requisições a fila do gerenciador de banco
    void addQueueDB(ElemQueue elemQueue) {
        queueManDB.add(elemQueue);
    }*/
    
    /*Retorna fila de mensagens a serem redirecionadas
    public Queue<ElemQueue> getQueueManMessage() {
        return queueManMesage;
    }*/

    /*Adiciona mensagens  na fila do gerenciador redirecionamento
    void addQueueMessage(ElemQueue elemQueue){
        queueManMesage.add(elemQueue);
    }*/

 

   



}
