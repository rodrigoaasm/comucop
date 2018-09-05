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
    private double certifCliente;
    private MyRSAKey aKeysServer;

    public ClientConRecord(Socket sockClient, double CertifCliente, MyRSAKey keysServer) {
        this.sockClient = sockClient;
        this.certifCliente = CertifCliente;
        aKeysServer = keysServer;
    }
    
    public static  void generateCertifCliente(){
        
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

    public double getCertifCliente() {
        return certifCliente;
    }

    public void setCertifCliente(double CertifCliente) {
        this.certifCliente = CertifCliente;
    }

    public MyRSAKey getKeysServer() {
        return aKeysServer;
    }

    public void setKeysServer(MyRSAKey keysServer) {
        this.aKeysServer = keysServer;
    }
    
    
}
