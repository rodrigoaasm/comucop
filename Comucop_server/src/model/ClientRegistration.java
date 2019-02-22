/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.net.Socket;

/**
 *
 * @author Rodrigo Maia
 */
public class ClientRegistration {
    private Socket clientSock;
    private Runnable clientListener;

    public ClientRegistration(Socket clientSock, Runnable clientListener) {
        this.clientSock = clientSock;
        this.clientListener = clientListener;
    }    

    public Socket getClientSock() {
        return clientSock;
    }

    public Runnable getClientListener() {
        return clientListener;
    }
    
}
