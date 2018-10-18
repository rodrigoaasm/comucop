/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
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

    private ArrayList<ClientConRecord> clientsConRec;
    private volatile Queue<ElemQueue> queueManDB;
    private volatile Queue<ElemQueue> queueManMesage;

    private ManagerSend mSend;
    private ManagerReceiver mReceiver;
    private ManagerDB mDb;
    private ManagerMsg mMsg;

    public Controller() {
        this.mWin = new MainWindow(this);
        mWin.setVisible(true);

        clientsConRec = new ArrayList<ClientConRecord>();//instaciando lista de clientes
        queueManDB = new LinkedList();
        queueManMesage = new LinkedList();

        mSend = new ManagerSend(this);//Instaciando Gerenciador de envios
        try {
            mReceiver = new ManagerReceiver(this);//Instaciando gereciador de recebimento e iniciando threads
            mReceiver.start();
            mDb = new ManagerDB(this);
            mDb.start();
            mMsg = new ManagerMsg(this);
            mMsg.start();
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*Método principal do servidor*/
    public static void main(String args[]) {
        Controller c = new Controller();
    }

    /*Retorna lista de clientes online*/
    public ArrayList<ClientConRecord> getClients() {
        return clientsConRec;
    }

    /*Retorna gerenciador de envios*/
    public ManagerSend getmSend() {
        return mSend;
    }

    /*Retorna gerenciador de recebimentos*/
    public ManagerReceiver getmReceiver() {
        return mReceiver;
    }

    /*Retorna fila de requisições de banco*/
    public Queue<ElemQueue> getQueueManDB() {
        return queueManDB;
    }

    /*Adiciona requisições a fila do gerenciador de banco*/
    void addQueueDB(ElemQueue elemQueue) {
        queueManDB.add(elemQueue);
    }
    
    /*Retorna fila de mensagens a serem redirecionadas*/
    public Queue<ElemQueue> getQueueManMessage() {
        return queueManMesage;
    }

    /*Adiciona mensagens  na fila do gerenciador redirecionamento*/
    void addQueueMessage(ElemQueue elemQueue){
        queueManMesage.add(elemQueue);
    }

}
