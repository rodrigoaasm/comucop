package controller;

import com.mongodb.*;
import controller.*;
import model.*;
import java.util.ArrayList;
import org.json.simple.JSONObject;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ElemQueue;

public class ManagerMsg extends Thread{

    private Mongo mongo;
    private DB db;
    private DBCollection table;
    private Controller objCtrPrincipal;

    public ManagerMsg(Controller pController) {

        objCtrPrincipal = pController;
       // mongo = new Mongo("localhost", 27017);
       // db = mongo.getDB("comucop");
       // table = db.getCollection("mesages");

    }
    
    public void run(){
        while (true) {            
            Queue<ElemQueue> queueManMesage = objCtrPrincipal.getQueueManMesage();
            if(queueManMesage.isEmpty() == false){
                ElemQueue eq = queueManMesage.poll();//recuperando elemento
                JSONObject rec = eq.getJsonReq();
                ArrayList<ClientConRecord> vetCliente = objCtrPrincipal.getClients();
                ClientConRecord client = eq.getClient();
                if(vetCliente.contains(client)){
                    ManagerSend manSend = new ManagerSend(objCtrPrincipal);
                    manSend.sendJSON(client, rec);
                }
                System.out.println("controller.ManagerMsg.run() --> " + rec.toJSONString());
            }else{
                try {
                    Thread.sleep(23);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ManagerMsg.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void closeConection() {
        mongo.close();
    }

    public void insertMongoDB() {

        BasicDBObject document = new BasicDBObject();
        document.put("remetente", "");
        document.put("destinatario", "");
        document.put("conteudo", "");
        document.put("data_envio", "");

    }

}
