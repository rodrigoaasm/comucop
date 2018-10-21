
package controller;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;
import model.Chat;
import model.Contato;
import model.Mensagem;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import view.MainWindow;


public class Controller {
   
    private MainWindow mWin;
    private ControllerDep ctrDep;
    private ControllerFuncionario ctrFunc;
    private ManagerSend mSend;
    private ManagerReceiver mRec;
    private Contato cliente;
    private ArrayList<Contato> listaConts;
    private ArrayList<Chat> listChats;
    private Chat selectionChat;
    private File soundFile;
    private String serverIP;

    
    //Sockets de conexão TCP
    private Socket server;

    //Inicia aplicação atraves do contrutor do controle
    public Controller() {
        //Exibi janela principal da aplicação
        this.mWin = new MainWindow(this);
        mWin.setVisible(true);
        ctrDep = new ControllerDep(this);
        ctrFunc = new ControllerFuncionario(this);
        this.serverIP = "127.0.0.1";       
        try {//Inicia o gerenciador de envios
            mSend = new ManagerSend(this, InetAddress.getByName(serverIP));
        } catch (UnknownHostException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }        
        //Instancia lista de chat e contatos
        listaConts = new ArrayList<>();
        listChats = new ArrayList<>();
        
        try {//Instancia som de notificação
            soundFile = new File("notifSound.wav");    
        } catch (Exception e) {}
    }

    /*Responsável por inicia e reinicia o gerenciador de recebimentos*/
    void startReceiver() {
        //Garanti a exclusão e a parada do gerenciador de recebimento caso ele esteja aberto
        if (mRec != null) { 
            mRec.interrupt();
            mRec = null;
        }
        //Instancia Thread e inicia a mesma
        mRec = new ManagerReceiver(this);
        mRec.start();
    }
    
    /*Método responsável pelo alerta sonoro*/
    public void notifyMsg(){        
        try {  
            //Lê arquivo do som e prepara a reprodução
            AudioInputStream sound = AudioSystem.getAudioInputStream(soundFile);
            DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(sound);
            clip.start();//Reproduz alerta sonoro     
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }

    /*Método responsável por retorna a situação da tentativa de login do usuario*/
    public void feedbackLogin(JSONObject jsonResp) {
        //retorna valor de status respondido pelo servidor. Status = 0, erro;Status = 1, sucesso.
        int getSt = Integer.parseInt((String) jsonResp.get("status"));
        
        if (getSt == 1) {//Se ocorreu com sucesso o login 
            this.reqDepart();//Requisita departementos cadastrados
            mWin.loginOk();//Faz as devidas modificações na aplicação cquando logada
            //Guarda informações do usuário logado que foram respondidas pelo servidor*/
            cliente = new Contato(Integer.parseInt((String) jsonResp.get("codigo")),
                    (String) jsonResp.get("perfil"), (String) jsonResp.get("nome"), (String) jsonResp.get("sobrenome"));
            
            mWin.callMessage("Seja bem-vindo " + jsonResp.get("nome") + " " + jsonResp.get("sobrenome") + "!",
                    "Login efetuado com sucesso", JOptionPane.INFORMATION_MESSAGE);//Mensagem de sucesso
        } else {//Se ocorreu erro na autenticação retorna uma mensagem pro usuário
            mWin.callMessage("Login e senha não correspondem a um usuário da aplicação! ",
                    "Falha Login", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*Método responsável por montar a requisição de departamentos*/
    private void reqDepart() {
        JSONObject jsonreq = new JSONObject();
        jsonreq.put("type", "req-depart");//Monta json
        mSend.sendJSON(jsonreq.toJSONString());//envia json
    }

    /*Método responsável por montar a requisição de contatos(funcionários) por departamento*/
    public void expToContacts(String codDep) {
        JSONObject jsonreq = new JSONObject();
        jsonreq.put("type", "exp-to-contacts");//Monta json
        jsonreq.put("id-depart", codDep);//Informa codigo do departamento
        mSend.sendJSON(jsonreq.toJSONString());//Envia json
    }

    /*Método responsável por disparar a tentativa de uma conexão*/
    public void tryEstablishCon(String user, String password) throws IOException {
        mSend.establishCon(user, password);
    }

    /*Método responsável por disparar a tentativa de desconexão*/ 
    public void finishCon() {
        if(server != null){
            if(server.isConnected()){//Avalia se ainda está conectado
                try {
                    mSend.finishCon();//Tenta finalizar
                } catch (IOException ex) {//Informa erro se ocorrer
                    mWin.callMessage("Erro ao fechar a conexão com servidor!",
                            "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /*Método responsavel por montar o json correpondente a uma mensagem do usuário a outro contato*/
    public void sendMsg(String textMsg) {
        JSONObject jsonMsg = new JSONObject();

        Date d = new Date();//Pegando hora da mensagem
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss-dd/MM/yyyy");

        JSONObject rem = new JSONObject();//Contruido json com as informações do remetente        
        rem.put("codigo", cliente.getCodigo());
        rem.put("nome", cliente.getNome());
        rem.put("sobrenome", cliente.getSobrenome());
        rem.put("perfil", cliente.getPerfil());
        
        JSONArray listRem = new JSONArray();
        listRem.add(rem);        
                
        jsonMsg.put("rem", listRem);//Contruindo json da mensagem
        jsonMsg.put("dest", selectionChat.getDestinatario().getCodigo());
        jsonMsg.put("time", format.format(d));
        jsonMsg.put("cont", textMsg);
        jsonMsg.put("type", "mensagem");

        selectionChat.addMsg(new Mensagem(selectionChat.getDestinatario(),
                cliente,textMsg, d,true));//Guardando a mensagem 
        //retira o chat da lista e coloca ele em primeiro
        listChats.remove(selectionChat);
        listChats.add(0,selectionChat);
        //Envia mensagem
        mSend.sendJSON(jsonMsg.toJSONString());//Enviando
        //Atualiza o textarea com a msg enviada
        String msgs = "";        
        //Recebe mensagens 
        for(Mensagem m: selectionChat.getMensagens()){
             msgs += m.getMsg()+"\n";
        }
        mWin.preencheChat(msgs);
        mWin.updateChat();
      
    }

    /*Método responsavel por analisar para qual departamento os contatos recebidos do servidor pertencem*/
    void toExpCellDepartsReq(JSONObject jsonResp) {
        leituraJson(jsonResp);        
        int depart = Integer.parseInt((String) jsonResp.get("id-depart"));
        mWin.expCellDeparts(depart, listaConts);
    }

    /*Método que faz a leitura dos contatos retornados pelo servidor*/
    public void leituraJson(JSONObject jsonObj) {
        //Recebe a lista de contatos*/
        JSONArray funcs = (JSONArray) jsonObj.get("Contatos");
        Iterator<JSONObject> ite = funcs.iterator();
        /*Faz a leitura das informações idividuais de cada contato*/
        while (ite.hasNext()) {
            //leitura
            JSONObject objDep = (JSONObject) ite.next();
            Long codigo = (Long) objDep.get("Codigo");
            String nome = (String) objDep.get("Nome");
            String sobrenome = (String) objDep.get("Sobrenome");
            String perfil = (String) objDep.get("Perfil");
            Integer cod = 1;
            try {//codigo é recebido como um long, portanto é necessário sua conversão
                cod = Integer.valueOf(codigo.toString());
            } catch (Exception e) {
                System.out.println("Capacidade do Integer estourou.");
            }
            
            //Instancia contato e adiciona a lista de ocntatos
            Contato f = new Contato(cod, perfil, nome, sobrenome);
            listaConts.add(f);
        }
    }
    
    //Método responsável por ler a mensagem recebida do servidor*/
    public void leituraJsonMsg(JSONObject jsonObj) {
        //Declaração das variaveis para leitura da MSG
        Integer ver = 0, codDest = 1;   
        String data = (String) jsonObj.get("time");
        String msg = (String) jsonObj.get("cont");
        Long dest = (Long) jsonObj.get("dest");
        try{//Converte o valor do codigo do destinatário em inteiro
            codDest = Integer.valueOf(dest.toString());
        }catch(Exception e){
            e.setStackTrace(e.getStackTrace());
        }
        
        //Recebe informações do remetente
        JSONArray rem = (JSONArray) jsonObj.get("rem");
        Iterator i = rem.iterator();
        
        //Faz a leitura das informação do remetente
        while(i.hasNext()){  
            //Variaveis
            JSONObject jsonrem = (JSONObject) i.next();
            Integer cod = null;
            
            //Iniciado Valores
            Long longcod = (Long)jsonrem.get("codigo");
            try {cod = Integer.valueOf(longcod.toString());} catch (Exception e){}            
            String perfil = (String) jsonrem.get("perfil");
            String nome = (String) jsonrem.get("nome");
            String sobrenome = (String) jsonrem.get("sobrenome");
            
            //Controi o objeto contato coma s informações do remtente
            Contato f = new Contato(cod, perfil, nome, sobrenome);
            //Verifica se já existe uma conversa com esse contato 
            for (Chat c : listChats) {
                if ((c.getRemetente().getCodigo() == codDest
                        && c.getDestinatario().getCodigo() == f.getCodigo())
                        ||(c.getRemetente().getCodigo() == f.getCodigo())
                        && c.getDestinatario().getCodigo() ==codDest) {//Se sim
                   
                    //Coloca ele no inicio da lista
                    listChats.remove(c);
                    listChats.add(0,c);
                    
                    //Adiciona mensagem ao chat
                    c.addMsg(new Mensagem(cliente, f, msg, new Date(data)));
                    ver++;
                    String msgs = c.toString();
                    //Se o chat que recebeu a mensagem for o que estiver em foco na tela
                    if(selectionChat.equals(c)){
                         mWin.preencheChat(msgs);//Escrevi mensagem na tela
                    }
                    mWin.updateChat(); //Atualiza lista de chats na tela            
                    break;
                }
            }
            //Se não foi encotrado um chat já iniciado que correponda com remtente da mensagem
            if (ver == 0) {//Instancia o chat com novo remetente
                Chat c = new Chat(cliente, f);
                c.addMsg(new Mensagem(cliente, f, msg, new Date(data)));
                listChats.add(0,c);         
                mWin.updateChat();           
            }      
             notifyMsg();//Chama notificação
        }
    }

    /*Retorna lista de contatos*/
    public ArrayList<Contato> getListaConts() {
        return listaConts;
    }

    /*Faz a censura a tela caso a conexão caia*/
    void conBroke() {
        mWin.backLoginWin();
        server = null;
    }

    /*Método responsavel por redirencionar na tela, a conversa que o usuário deseja colocar em foco*/
    public void selectContact(int funcCod) {
        
        for (Chat c : listChats) {//Verifica se o chat com esse destinatário já existe
            if (c.getDestinatario().getCodigo() == funcCod) {
                selectionChat = c;//Se existir marca como chat selecionado
                listChats.remove(c);//Retira o chat da lista e coloca de volta em primeiro
                listChats.add(0,c);
                mWin.openTextUI(c.getDestinatario().getNome() + " " + c.getDestinatario().getSobrenome()
                        ,c.getDestinatario().getPerfil());
                String msgs = c.toString();
                mWin.preencheChat(msgs);
                mWin.changeTabRec();
                return;
            }
        }
        for (Contato cont : listaConts) {//Se não existir, cria o chat para conversa
            if (funcCod == cont.getCodigo()) {
                selectionChat = new Chat(cliente, cont);
                listChats.add(0,selectionChat);
                mWin.openTextUI(selectionChat.getDestinatario().getNome() + " " + selectionChat.getDestinatario().getSobrenome()
                        ,selectionChat.getDestinatario().getPerfil());
                mWin.updateChat();
                mWin.changeTabRec();//Muda aba
            }
        }

    } 
    
    /*Metodo responsável por colocar na tela informações sobre o contato*/
    public void updateUIChat(int posicChat) {
        selectionChat = listChats.get(posicChat);
        mWin.preencheChat(selectionChat.toString(),
                selectionChat.getDestinatario().getNome() + " " + 
                selectionChat.getDestinatario().getSobrenome(),
                selectionChat.getDestinatario().getPerfil());
    }
    
    /*Metodo responsável por notificar erros com servidor*/
    void throwExp(String message) {
        mWin.callMessage(message, "Erro ao conectar com o servidor!", 
                JOptionPane.ERROR_MESSAGE);
    }    

    /*Retorna a lista de chats*/
    public ArrayList<Chat> getListChats() {
        return listChats;
    }

     /*Retorna controle de departamento*/
    public ControllerDep getCtrDep() {
        return ctrDep;
    }

    /*Retorna controle de funcionario*/
    public ControllerFuncionario getCtrFunc() {
        return ctrFunc;
    }

    /*Retorna socket da conexão persistente com o servidor*/
    public Socket getSocketServer() {
        return server;
    }

    /*Define o socket da conexão persistente com o servidor*/ 
    public void setSocketServer(Socket s) {
        if (server != null) {
            try {
                server.close();//Garanti que o socket antigo esteja realmente fechado
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        server = s;
    }

    /*Retorna janela principal*/
    public MainWindow getmWin() {
        return mWin;
    }
    
    /*Método Principal da aplicação*/
    public static void main(String args[]) {
        Controller c = new Controller();
    }

    public String getIPServer() {
        return serverIP;
    }

    public void setIPServer(String ip) throws UnknownHostException {
        serverIP = ip;
        mSend.setIpServer(serverIP);
    }

}
