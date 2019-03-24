/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import network.ConnectionManager;

/**
 *
 * @author Rodrigo Maia
 */
public class ResourceMonitor extends Thread{
    
    private Controller ctrServer;

    public ResourceMonitor(Controller ctrServer) {
        this.ctrServer = ctrServer;
    }
    
    @Override
    public void run(){
        do{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ResourceMonitor.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            JSONObject json = new JSONObject();
            json.put("thread-created",ctrServer.getConnectionManager().getnThreadCreated());
            json.put("sleeping-thread",ctrServer.getqSleepingListeners().size());
            json.put("Thread-listening",ctrServer.getListClients().size());
            json.put("requisition-queue",ctrServer.getQueueManMessage().size());
            writeJsonStatus(json);
        }while(true); 
    }
        
    public void writeJsonStatus(JSONObject json){
        try {
            FileWriter file = new FileWriter("/var/www/html/json.json");
            PrintWriter printer = new PrintWriter(file);
            printer.print(json.toJSONString());
            printer.flush();
            file.close();
        } catch (IOException ex) {
            Logger.getLogger(ResourceMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
