package controller;

import com.mongodb.*;
import controller.*;
import model.*;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.simple.JSONObject;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ElemQueue;
import static org.hibernate.criterion.Restrictions.eq;

public class ManagerMsg extends Thread {

    private Mongo mongo;
    private DB db;
    private DBCollection table;
    private Controller objCtrPrincipal;

    public ManagerMsg(Controller pController) {

        objCtrPrincipal = pController;

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
                    insertMongoDB(rec);
                    getMesagesOff(2);
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

    public void insertMongoDB(JSONObject rec) {

        //INICIALIZA CONEXÃO COM O MONGODB
        mongo = new Mongo("localhost", 27017);//ABRE CONEXÃO COM MONGO
        db = mongo.getDB("comucop");//RECEBE A BASE DE DADOS RELACIONADA AO PROJETO
        table = db.getCollection("mesage");//RECEBE A COLECTION QUE GUARDA OS DOCUMENTOS

        BasicDBObject document = new BasicDBObject();//CRIA UM BASIC OBJECT PARA PERSISTIR NO BANCO

        document.put("remetente", rec.get("rem"));//============================
        document.put("time", rec.get("time"));//===== OBTEM OS DADOS ===========
        document.put("dest", rec.get("dest"));//===== QUE SERÃO PERSISTIDOS ====
        document.put("cont", rec.get("cont"));//===== E POPULAM O OBJETO =======
        document.put("type", rec.get("type"));//================================

        table.insert(document);//PERSISTE O OBJETO NO BANCO

        closeConection();//FECHA A CONEXÃO

    }

    //MÉTODO PARA COLETAR AS MENSAGENS QUE O USUÁRIO RECEBER ENQUANTO ESTAVA OFFLINE
    public void getMesagesOff(int codUser){
        
        //INICIALIZA CONEXÃO COM O MONGODB
        mongo = new Mongo("localhost", 27017);//ABRE CONEXÃO COM MONGO
        db = mongo.getDB("comucop");//RECEBE A BASE DE DADOS RELACIONADA AO PROJETO
        table = db.getCollection("mesage");//RECEBE A COLECTION QUE GUARDA OS DOCUMENTOS

        Iterator<DBObject> cursor = table.find((DBObject) eq("dest","NumberLong("+codUser+")")).iterator();
        int i = 1;
        
        while(cursor.hasNext()){
            
            System.out.println("JSON " + i + "\n" + cursor.toString());
            
        }

        closeConection();//FECHA A CONEXÃO
        
    }

}
