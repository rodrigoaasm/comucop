/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import cripth.MyRSAKey;
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

    private volatile ArrayList<ClientConRecord> clientsConRec;
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

        mSend = new ManagerSend(this);//Instaciando Gerenciador de recebimentos
        try {
            mReceiver = new ManagerReceiver(this);//Instaciando gereciador de recebimento e iniciando thread
            mReceiver.start();
            mDb = new ManagerDB(this);
            mDb.start();
            mMsg = new ManagerMsg(this);
            mMsg.start();
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String args[]) {
        Controller c = new Controller();
    }

    public ArrayList<ClientConRecord> getClients() {
        return clientsConRec;
    }

    public ManagerSend getmSend() {
        return mSend;
    }

    public ManagerReceiver getmReceiver() {
        return mReceiver;
    }

    public Queue<ElemQueue> getQueueManDB() {
        return queueManDB;
    }

    void addQueueDB(ElemQueue elemQueue) {
        queueManDB.add(elemQueue);
    }
    
    public Queue<ElemQueue> getQueueManMesage() {
        return queueManMesage;
    }

    void addQueueMesage(ElemQueue elemQueue){
        queueManMesage.add(elemQueue);
    }

}
