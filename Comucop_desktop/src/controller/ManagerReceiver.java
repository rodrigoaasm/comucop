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
        DataInputStream dIStr = null;          
        
        if(ctrApp.getSocketServer().isConnected()){//Verifica se a conexão ainda existe           
            JSONObject jsonResp= null;
            try {
                //Preparando stream de entrada e conversor json de resposta
                dIStr = new DataInputStream(ctrApp.getSocketServer().getInputStream());          
                JSONParser parser = new JSONParser();//Instaciando o conversor JSON
                while(ctrApp.getSocketServer().isConnected()){//Enquanto a conexão estiver viva, a escuta-a
                    //Lendo Json de resposta do servidor 
                    jsonResp = (JSONObject) parser.parse(dIStr.readUTF());
                    //Analisa de qual operação é a resposta
                    String type = (String)jsonResp.get("type");
                    if(type.compareTo("login")==0){//Operação de login
                        ctrApp.feedbackLogin(jsonResp);
                    }else if(type.compareTo("req-depart")==0){//Operação requisição de departamentos
                        System.out.println(jsonResp.toJSONString());
                        ctrApp.getCtrDep().LeituraJson(jsonResp);
                        ctrApp.getmWin().addDeps();
                    }else if(type.compareTo("exp-to-contacts")==0){//Operação requisição de departamentos   
                        System.out.println(jsonResp.toJSONString());
                        ctrApp.getListaConts().clear();                        
                        ctrApp.toExpCellDepartsReq(jsonResp);
                    }else if(type.compareTo("mensagem")==0){//Operação requisição de departamentos   
                        System.out.println("MENSAGEM:"+jsonResp.toJSONString());    
                        ctrApp.leituraJsonMsg(jsonResp);
                    }
                }
                ctrApp.conBroke();//Notificando quebra na conexão
            } catch (IOException ex) {
                Logger.getLogger(ManagerReceiver.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(ManagerReceiver.class.getName()).log(Level.SEVERE, null, ex);
            }       
        }else{
            ctrApp.conBroke();//notificando quebra na conexão
        }     
    }

    
    
}
