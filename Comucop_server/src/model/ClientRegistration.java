/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import network.ConnectionListener;
import java.net.Socket;
import java.util.LinkedList;
import org.json.simple.JSONObject;

/**
 *
 * @author Rodrigo Maia
 */
public class ClientRegistration {
    private Socket clientSock;
    private ConnectionListener clientListener;
    private Integer codClient;    

    public ClientRegistration(Socket clientSock, ConnectionListener clientListener) {
        this.clientSock = clientSock;
        this.clientListener = clientListener;
    }    

    public Socket getClientSock() {
        return clientSock;
    }

    public Runnable getClientListener() {
        return clientListener;
    }
    
    public void addQPackToBeSent(JSONObject jsonPkg){
        clientListener.addQPackToBeSent(jsonPkg);
    } 
        
    public Integer getCodClient() {
        return codClient;
    }

    public void setCodClient(Integer codClient) {
        this.codClient = codClient;
    }

    public void forseClose() {
        clientListener.forcingListenerSleep();
    }
}
