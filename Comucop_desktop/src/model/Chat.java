/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;

/**
 *
 * @author Kevin
 */
public class Chat {
    
    private Contato remetente;
    private Contato destinatario;
    private ArrayList<Mensagem> mensagens;

    public Chat(Contato remetente, Contato destinatario) {
        this.remetente = remetente;
        this.destinatario = destinatario;
        mensagens = new ArrayList<>();
    }
    
    public void addMsg(Mensagem pMsg){
        mensagens.add(pMsg);
    }
    
    public String getLastMsg(){
        if(mensagens.isEmpty()){
            return mensagens.get(mensagens.size()-1).getContMsg();
        }else return "...";        
    }

    public int countUnreadMsg(){
        int count = 0;
        for(Mensagem m :mensagens){
            if(!m.isStatusRead()){
                count++;
            }
        }
        return count;
    }
    
    public Contato getRemetente() {
        return remetente;
    }

    public void setRemetente(Contato remetente) {
        this.remetente = remetente;
    }

    public Contato getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Contato destinatario) {
        this.destinatario = destinatario;
    }

    public ArrayList<Mensagem> getMensagens() {
        return mensagens;
    }

    public void setMensagens(ArrayList<Mensagem> mensagens) {
        this.mensagens = mensagens;
    }
    
    public String toString(){
        String msgs = "";
        for(Mensagem m: mensagens){
            msgs += m.getMsg()+"\n";
            m.setStatusRead(true);
        }
        return msgs;
    }
    
    
    
}
