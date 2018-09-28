/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import cripth.MyRSAKey;
import java.net.Socket;
import java.security.PublicKey;

/**
 *
 * @author Rodrigo Maia
 */
public class ClientConRecord {
    
    private Socket sockClient;
    private PublicKey publicKeyClient;
    private MyRSAKey aKeysServer;
    private String client;
    private Integer clientCod;

    public ClientConRecord(Socket sockClient, MyRSAKey keysServer) {
        this.sockClient = sockClient;

        aKeysServer = keysServer;
    }

    public MyRSAKey getaKeysServer() {
        return aKeysServer;
    }

    public void setaKeysServer(MyRSAKey aKeysServer) {
        this.aKeysServer = aKeysServer;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Integer getClientCod() {
        return clientCod;
    }

    public void setClientCod(Integer client_cod) {
        this.clientCod = client_cod;
    }

    public Socket getSockClient() {
        return sockClient;
    }

    public void setSockClient(Socket sockClient) {
        this.sockClient = sockClient;
    }
    
    public boolean isSocketConnected(){
        if(sockClient != null){
            return sockClient.isConnected();
        }else return false;
    }
            
    public PublicKey getPublicKeyClient() {
        return publicKeyClient;
    }

    public void setPublicKeyClient(PublicKey publicKeyClient) {
        this.publicKeyClient = publicKeyClient;
    }

    public MyRSAKey getKeysServer() {
        return aKeysServer;
    }

    public void setKeysServer(MyRSAKey keysServer) {
        this.aKeysServer = keysServer;
    }
    
    
}
