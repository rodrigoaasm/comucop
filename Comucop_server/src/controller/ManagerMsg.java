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

public class ManagerMsg extends Thread {

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

    /*Método principal da thread, que faz o redirecionamento das mensagens*/
    public void run() {
        int i;
        while (true) {
            Queue<ElemQueue> queueManMesage = objCtrPrincipal.getQueueManMessage();
            if (queueManMesage.isEmpty() == false) {
                ElemQueue eq = queueManMesage.poll();//recuperando elemento
                JSONObject rec = eq.getJsonReq(); //Recuperando json
                ArrayList<ClientConRecord> vetCliente = objCtrPrincipal.getClients();//recuperando lista de onlines
                
                ClientConRecord remetente = eq.getClient();//Recebendo remetente
                ClientConRecord destinatario = null;
                
                Object dest = eq.getJsonReq().get("dest");
                int codDest = Integer.parseInt(dest.toString());//recebendo codigo do destinatário

                for (i = 0; i < vetCliente.size(); i++) {//Verificando se o destinatário está online
                    if (codDest == vetCliente.get(i).getClientCod()) {                        
                        destinatario = vetCliente.get(i);
                    }
                }

                if (destinatario != null) {  //Se for diferente de null está online, pois foi encontrado na lista              
                    objCtrPrincipal.getmSend().sendJSON(destinatario, rec);                   
                }else{//se não está off, pois não foi encontrado
                    //Mongo aqui dentro
                }
                
            } else {
                try {//Dormi po 33ms se a fila estiver vazia
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
