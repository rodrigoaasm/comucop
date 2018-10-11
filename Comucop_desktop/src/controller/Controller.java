/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author root
 */
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

    
    //Sockets de conexão TCP
    private Socket server;

    public Controller() {
        this.mWin = new MainWindow(this);
        mWin.setVisible(true);
        ctrDep = new ControllerDep(this);
        ctrFunc = new ControllerFuncionario(this);

        try {
            mSend = new ManagerSend(this, InetAddress.getByName("127.0.0.1"));
        } catch (UnknownHostException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        listaConts = new ArrayList<>();
        listChats = new ArrayList<>();
        
        try {
            soundFile = new File("notifSound.wav");    
        } catch (Exception e) {
            
        }
    }

    void startReceiver() {
        if (mRec != null) { //Garanti a exclusão e a parada do gerenciador de recebimento
            mRec.interrupt();
            mRec = null;
        }
        mRec = new ManagerReceiver(this);
        mRec.start();
    }
    
    public void notifyMsg(){        
        try {  
            AudioInputStream sound = AudioSystem.getAudioInputStream(soundFile);
            DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(sound);
            clip.start();          
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }

    public void feedbackLogin(JSONObject jsonResp) {
        int getSt = Integer.parseInt((String) jsonResp.get("status"));
        
        if (getSt == 1) {
            this.reqDepart();
            mWin.loginOk();

            cliente = new Contato(Integer.parseInt((String) jsonResp.get("codigo")),
                    (String) jsonResp.get("perfil"), (String) jsonResp.get("nome"), (String) jsonResp.get("sobrenome"));
            mWin.callMessage("Seja bem-vindo " + jsonResp.get("nome") + " " + jsonResp.get("sobrenome") + "!",
                    "Login efetuado com sucesso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            mWin.callMessage("Login e senha não correspondem a um usuário da aplicação! ",
                    "Falha Login", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void reqDepart() {
        JSONObject jsonreq = new JSONObject();
        jsonreq.put("type", "req-depart");
        mSend.sendJSON(jsonreq.toJSONString());
    }

    public void expToContacts(String codDep) {
        JSONObject jsonreq = new JSONObject();
        jsonreq.put("type", "exp-to-contacts");
        jsonreq.put("id-depart", codDep);
        mSend.sendJSON(jsonreq.toJSONString());
    }

    public void tryEstablishCon(String user, String password) throws IOException {
        mSend.establishCon(user, password);
    }

    public void finishCon() {
        if(server != null){
            if(server.isConnected()){
                try {
                    mSend.finishCon();
                } catch (IOException ex) {
                    mWin.callMessage("Erro ao fechar a conexão com servidor!",
                            "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public void sendMsg(String textMsg) {
        JSONObject jsonMsg = new JSONObject();

        Date d = new Date();//Pegando hora da mensagem
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss-dd/MM/yyyy");

        JSONObject rem = new JSONObject();//Contruido json com as informações do remetente        
        rem.put("codigo", cliente.getCodigo());
        rem.put("nome", cliente.getNome());
        rem.put("sobrenome", cliente.getSobrenome());
        rem.put("perfil", cliente.getPerfil());

        jsonMsg.put("rem", rem.toJSONString());//Contruindo json da mensagem
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

    public ControllerDep getCtrDep() {
        return ctrDep;
    }

    public ControllerFuncionario getCtrFunc() {
        return ctrFunc;
    }

    public static void main(String args[]) {
        Controller c = new Controller();
    }

    public Socket getSocketServer() {
        return server;
    }

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

    public MainWindow getmWin() {
        return mWin;
    }

    void toExpCellDepartsReq(JSONObject jsonResp) {
        leituraJson(jsonResp);        
        int depart = Integer.parseInt((String) jsonResp.get("id-depart"));
        mWin.expCellDeparts(depart, listaConts);
    }

    public void leituraJson(JSONObject jsonObj) {

        JSONArray funcs = (JSONArray) jsonObj.get("Contatos");
        Iterator<JSONObject> ite = funcs.iterator();
        while (ite.hasNext()) {
            JSONObject objDep = (JSONObject) ite.next();
            Long codigo = (Long) objDep.get("Codigo");
            String nome = (String) objDep.get("Nome");
            String sobrenome = (String) objDep.get("Sobrenome");
            String perfil = (String) objDep.get("Perfil");
            Integer cod = 1;
            try {
                cod = Integer.valueOf(codigo.toString());
            } catch (Exception e) {
                System.out.println("Capacidade do Integer estourou.");
            }
            Contato f = new Contato(cod, perfil, nome, sobrenome);
            listaConts.add(f);
        }
    }
    
    //Recebe a MSG
    public void leituraJsonMsg(JSONObject jsonObj) {
        //Declaração das variaveis para leitura da MSG
        Integer ver = 0, codDest = 1;
        String funcs = (String) jsonObj.get("rem");
        funcs = funcs.replaceAll("[{]", "");
        funcs = funcs.replaceAll("[}]", "");
        funcs = funcs.replaceAll("\"", "");
        String[] text = funcs.split(",");
        String aux[] = text[0].split(":");
        Integer cod = Integer.parseInt(aux[1]);
        aux = text[1].split(":");
        String nome = aux[1];
        aux  = text[2].split(":");
        String sobrenome = aux[1];
        aux = text[3].split(":");
        String perfil = aux[1];
        String data = (String) jsonObj.get("time");
        String msg = (String) jsonObj.get("cont");
        Long dest = (Long) jsonObj.get("dest");
        try{
            codDest = Integer.valueOf(dest.toString());
        }catch(Exception e){
            e.setStackTrace(e.getStackTrace());
        }
        
        Contato f = new Contato(cod, perfil, nome, sobrenome);
        //Verifica se existe uma conversa com essa pessoa
        for (Chat c : listChats) {
            if ((c.getRemetente().getCodigo() == codDest
                    && c.getDestinatario().getCodigo() == f.getCodigo())
                    ||(c.getRemetente().getCodigo() == f.getCodigo())
                    && c.getDestinatario().getCodigo() ==codDest) {
                listChats.remove(c);
                listChats.add(0,c);
                c.addMsg(new Mensagem(cliente, f, msg, new Date(data)));
                ver++;
                String msgs = c.toString();
                if(selectionChat.equals(c)){
                     mWin.preencheChat(msgs);
                }
                mWin.updateChat();                
                break;
            }
        }
        if (ver == 0) {
            Chat c = new Chat(cliente, f);
            c.addMsg(new Mensagem(cliente, f, msg, new Date(data)));
            listChats.add(0,c);         
            mWin.updateChat();           
        }      
         notifyMsg();
    }

    public ArrayList<Contato> getListaConts() {
        return listaConts;
    }

    void conBroke() {
        mWin.backLoginWin();
        server = null;
    }

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
        for (Contato cont : listaConts) {//Se não existir cria o chat para conversa
            if (funcCod == cont.getCodigo()) {
                selectionChat = new Chat(cliente, cont);
                listChats.add(0,selectionChat);
                mWin.openTextUI(selectionChat.getDestinatario().getNome() + " " + selectionChat.getDestinatario().getSobrenome()
                        ,selectionChat.getDestinatario().getPerfil());
                mWin.updateChat();
                mWin.changeTabRec();
            }
        }

    } 
    
    public void updateUIChat(int posicChat) {
        selectionChat = listChats.get(posicChat);
        mWin.preencheChat(selectionChat.toString(),
                selectionChat.getDestinatario().getNome() + " " + 
                selectionChat.getDestinatario().getSobrenome(),
                selectionChat.getDestinatario().getPerfil());
    }
    
    
    void throwExp(String message) {
        mWin.callMessage(message, "Erro ao conectar com o servidor!", 
                JOptionPane.ERROR_MESSAGE);
    }    

    public ArrayList<Chat> getListChats() {
        return listChats;
    }

    

}
