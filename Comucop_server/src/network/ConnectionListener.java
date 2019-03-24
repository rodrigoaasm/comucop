/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;



import controller.Controller;
import controller.ManagerDB;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ClientRegistration;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 *
 * @author Rodrigo Maia
 */
public class ConnectionListener implements Runnable{
    
    //Constantes
    public final static int SLEEPING = 0;
    public final static int WAITING_FOR_AUTH = 1;
    public final static int LISTINING = 2;
    public final static int WILL_SLEEP = 3;
    //Variáveis de estado
    private int status;
    private long lastCommunication;
    private ClientRegistration client;
    //Dependências     
    private Controller ctrServer;
    private ManagerDB mDB;    
    private DataInputStream inputStream;
    private DataOutputStream outputStream;    
    private LinkedList qPackToBeSent;
    //Auxiliares
    private JSONParser parser;
    
    public ConnectionListener(Controller ctrServer) {
        this.ctrServer = ctrServer;
        this.mDB = ctrServer.getmDB();
        this.status = SLEEPING;
        this.qPackToBeSent = new LinkedList();
        this.parser = new JSONParser();//Instaciando o conversor JSON        
        this.lastCommunication = 0;
    }    
    
    @Override
    public void run() {
        while(true){            
            if(status == SLEEPING){
               try { Thread.sleep(20);} catch (InterruptedException ex) {}  
            }else if(status == WILL_SLEEP){
                closeCon();
            }else{
                if(this.client.getClientSock().isClosed()){//Se o cliente fechar a conexão retira ele do serviço                    
                    closeCon();
                }
                else{
                    this.listener();//começa escutar a conexão                    
                }
            }
        }
    }

    public void wakeUp(ClientRegistration client) {
        this.client = client;
        try {
            inputStream = new DataInputStream(client.getClientSock().getInputStream());//Pegando serviço de entrada de stream do socket 
            outputStream = new DataOutputStream(client.getClientSock().getOutputStream());//Pegando serviço de saida de stream do socket 
        } catch (IOException ex) {
            Logger.getLogger(ConnectionListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.status = WAITING_FOR_AUTH;       
        lastCommunication = System.currentTimeMillis();
    }
    
    public void forcingListenerSleep(){
        this.status = WILL_SLEEP;
    }
    
    public void listener(){         
        try{      
            if(inputStream.available()> 0){ //Se houver dados na entrada                                     
                JSONObject jsonReq = (JSONObject) parser.parse(inputStream.readUTF());//Lendo Json de requisição do cliente 
                routerWork(jsonReq); //Fazendo a triagem do pacote
                lastCommunication = System.currentTimeMillis();//Registra momento do envio
            }            
            if(!qPackToBeSent.isEmpty()){//Se tiver dados a serem enviados na fila 
                JSONObject jsonReq = (JSONObject) qPackToBeSent.poll();//Extrair arquivo da fila
                outputStream.writeUTF(jsonReq.toJSONString());  //Escreve dados no canal                               
                outputStream.flush();
                lastCommunication = System.currentTimeMillis();//Registra momento do envio
            }
            
            //Se o client ficar mais de 20000 millisegundos sem operar verifica se ele realmente está conectado
            if(System.currentTimeMillis() - lastCommunication > 20000){                
                JSONObject jsonReq = (JSONObject) new JSONObject();
                jsonReq.put("type","to-alive");
                outputStream.writeUTF(jsonReq.toJSONString());//Escreve dados no canal                               
                outputStream.flush();//Espera resposta 
                lastCommunication = System.currentTimeMillis();//Registra momento do envio
            }
        } catch (ParseException ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            closeCon();     
        } 
    }  
    
    public void routerWork(JSONObject jsonreq) throws IOException{              
        String type = (String)jsonreq.get("type");
        if(type.compareTo("login")==0){
            System.err.println("Login processado");
            this.login(jsonreq);
        }else if(status == WAITING_FOR_AUTH){
            System.err.println("Req. Fechamento");
            closeCon();//Fechando conexão com o client             
        }else if(type.compareTo("req-depart")==0){
            qPackToBeSent.add(mDB.reqDepart());
        }else if(type.compareTo("exp-to-contacts")==0){
            qPackToBeSent.add(mDB.expToContacts(jsonreq));
        }else if(type.compareTo("mensagem")==0){
            ctrServer.getQueueManMessage(jsonreq);
        }else if(type.compareTo("logout")==0){
            closeCon();
        }
    }
    
    public void closeCon(){
        System.err.println("Fechando comunicação");
        if(status != SLEEPING){
            try {
                this.status = SLEEPING; // mudnaod status de operação                
                this.outputStream.close();
                this.inputStream.close();
                this.client.getClientSock().close();//Fechando conexão
                ClientRegistration c = this.ctrServer.getListClients().remove(client.getCodClient());//Removendo registor de client
                c = null;
                this.client = null;
                this.qPackToBeSent.clear();
                this.ctrServer.addQSleepingListeners(this);//Se adicionando a fila de ociosas                 
            } catch (IOException ex) {
                Logger.getLogger(ConnectionListener.class.getName()).log(Level.SEVERE, null, ex);
            }     
             
        }
    }

    private void login(JSONObject jsonreq) throws IOException {
        JSONObject jsonresp = mDB.login(jsonreq);//Chama o gerenciador de banco de dados
        System.err.println("Resultado enviado");
        if(Integer.parseInt((String) jsonresp.get("status")) == 1){ //Se o login foi feito com sucesso  
            client.setCodClient(Integer.parseInt((String) jsonresp.get("codigo")));//Definindo codigo do cliente logado
            if(ctrServer.getListClients().containsKey(client.getCodClient())){//Analisa se o clienet já possui um login ativo
               ctrServer.getListClients().get(client.getCodClient()).forseClose();
            }             
            ctrServer.addListClients(Integer.parseInt((String) jsonresp.get("codigo")), client);//Adicionando a lista de clientes conectados
            outputStream.writeUTF(jsonresp.toJSONString());//Enviado resposta do login
            outputStream.flush(); 
            status = LISTINING; 
        } else {
            closeCon(); 
        }       
    }

    public void addQPackToBeSent(JSONObject jsonPkg) {
        qPackToBeSent.add(jsonPkg);
    }
}        
  