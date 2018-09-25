/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Rodrigo Maia
 */
public class ManagerReceiver extends Thread{
    private Controller ctrApp;

    public ManagerReceiver(Controller ctrApp) {
        this.ctrApp = ctrApp;
    }
    
    @Override
    public void run(){
        if(ctrApp.getSocketServer().isConnected()){
            DataInputStream dIStr = null;  
            JSONObject jsonResp= null;
            try {
                dIStr = new DataInputStream(ctrApp.getSocketServer().getInputStream());                
                JSONParser parser = new JSONParser();//Instaciando o conversor JSON
                jsonResp = (JSONObject) parser.parse(dIStr.readUTF());//Lendo Json de resposta do servidor 
            } catch (IOException ex) {
                Logger.getLogger(ManagerReceiver.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(ManagerReceiver.class.getName()).log(Level.SEVERE, null, ex);
            }   
                       
            ctrApp.feedbackLogin(jsonResp);
        }
    }
    
    
}
