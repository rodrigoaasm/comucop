package controller;


import controller.*;
import model.*;
import org.json.simple.JSONObject;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManagerMsg extends Thread {

  /*  private Mongo mongo;
    private DB db;
    private DBCollection table;*/
    private Controller objCtrPrincipal;

    public ManagerMsg(Controller pController) {
        objCtrPrincipal = pController;
    }

   /* Método principal da thread, que faz o redirecionamento das mensagens*/
    public void run() {
        int i;
        while (true) {
            Queue<JSONObject> queueManMesage = objCtrPrincipal.getQueueManMessage();

            if (queueManMesage.isEmpty() == false) {                
                JSONObject rec = queueManMesage.poll();//recuperando elemento                
                String type = (String) rec.get("type");
                
                if (type.compareTo("mensagem")==0) {                    
                    Object dest = rec.get("dest");
                    ClientRegistration cr = objCtrPrincipal.getListClients().get(Integer.parseInt(dest.toString()));//recebendo codigo do destinatário
                    
                    if(cr != null){
                        cr.addQPackToBeSent(rec);
                        System.err.println("User Online");
                    }                    
                }else{                    
                    //getMesagesOff(eq.getClient());                    
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
/*
    public void closeConection() {
        mongo.close();
    }

    public void insertMongoDB(JSONObject rec) {

        //INICIALIZA CONEXÃO COM O MONGODB
        mongo = new Mongo("localhost", 27017);//ABRE CONEXÃO COM MONGO
        db = mongo.getDB("comucop");//RECEBE A BASE DE DADOS RELACIONADA AO PROJETO
        table = db.getCollection("mesage");//RECEBE A COLECTION QUE GUARDA OS DOCUMENTOS

        BasicDBObject document = new BasicDBObject();//CRIA UM BASIC OBJECT PARA PERSISTIR NO BANCO

        document.put("rem", rec.get("rem"));//============================
        document.put("time", rec.get("time"));//===== OBTEM OS DADOS ===========
        document.put("dest", rec.get("dest"));//===== QUE SERÃO PERSISTIDOS ====
        document.put("cont", rec.get("cont"));//===== E POPULAM O OBJETO =======
        document.put("type", "backup");//================================

        table.insert(document);//PERSISTE O OBJETO NO BANCO

        closeConection();//FECHA A CONEXÃO

    }

    //MÉTODO PARA COLETAR AS MENSAGENS QUE O USUÁRIO RECEBER ENQUANTO ESTAVA OFFLINE
    public void getMesagesOff(ClientConRecord client) {

        //INICIALIZA CONEXÃO COM O MONGODB
        mongo = new Mongo("localhost", 27017);//ABRE CONEXÃO COM MONGO
        db = mongo.getDB("comucop");//RECEBE A BASE DE DADOS RELACIONADA AO PROJETO
        table = db.getCollection("mesage");//RECEBE A COLECTION QUE GUARDA OS DOCUMENTOS

        DBCursor cursor = table.find();
        int codUser = client.getClientCod();
        JSONParser parser = new JSONParser();//Instaciando o conversor JSON

        while (cursor.hasNext()) {

            BasicDBObject msg = (BasicDBObject) cursor.next();
            System.out.println("cursor: " + msg);

            if (Integer.parseInt(msg.getString("dest")) == codUser) {
                JSONObject mensage = new JSONObject();
                try {
                    mensage = (JSONObject) parser.parse(msg.toJson());
                                 //   objCtrPrincipal.getmSend().sendJSON(client,mensage);
                                    System.out.println(mensage.toJSONString());
                } catch (ParseException ex) {
                    Logger.getLogger(ManagerMsg.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        
        BasicDBObject query = new BasicDBObject();
        query.append("dest", codUser);
        table.remove(query);

        closeConection();//FECHA A CONEXÃO

    }
*/
}
