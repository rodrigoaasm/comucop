/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.Contato;
import model.Funcionario;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import view.MainWindow;

/**
 *
 * @author root
 */
public class Controller {

    //Controllers 
    private MainWindow mWin;
    private ControllerDep ctrDep;
    private ControllerFuncionario ctrFunc;
    private ManagerSend mSend;
    private ManagerReceiver mRec;
    private Integer dpReq;
    private Contato cliente;
    private ArrayList<Contato> listaConts;

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
    }

    void startReceiver() {
        if (mRec != null) { //Garanti a exclusão e a parada do gerenciador de recebimento
            mRec.interrupt();
            mRec = null;
        }
        mRec = new ManagerReceiver(this);
        mRec.start();
    }

    public void feedbackLogin(JSONObject jsonResp) {
        int getSt = Integer.parseInt((String) jsonResp.get("status"));
        System.out.println(jsonResp.get("status"));
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
        try {
            mSend.sendJSON(jsonreq.toJSONString());
        } catch (IOException ex) {
            mWin.callMessage("Erro ao montar requisição de departamentos!",
                    "Erro de montagem", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void expToContacts(String codDep) {
        JSONObject jsonreq = new JSONObject();
        jsonreq.put("type", "exp-to-contacts");
        jsonreq.put("id-depart", codDep);
        try {
            mSend.sendJSON(jsonreq.toJSONString());
        } catch (IOException ex) {
            mWin.callMessage("Erro ao montar requisição de contantos!",
                    "Erro de montagem", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void tryEstablishCon(String user, String password) throws IOException {
        mSend.establishCon(user, password);
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

    public Integer getDpReq() {
        return dpReq;
    }

    public void setDpReq(Integer dpReq) {
        this.dpReq = dpReq;
    }

    public void LeituraJson(JSONObject jsonObj, Integer dp) {

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
            Contato f = new Contato(cod, perfil,nome, sobrenome);
            listaConts.add(f);
        }
        
    }

    public ArrayList<Contato> getListaConts() {
        return listaConts;
    }
    
    
}
