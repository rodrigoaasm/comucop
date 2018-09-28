/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;

/**
 *
 * @author Kevin
 */
public class Mensagem {
    
    private Contato destinatario;
    private Contato remetente;
    private String conteudo;
    private Date horario;

    public Mensagem(Contato destinatario, Contato remetente, String conteudo, Date horario) {
        this.destinatario = destinatario;
        this.remetente = remetente;
        this.conteudo = conteudo;
        this.horario = horario;
    }

    public Contato getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Contato destinatario) {
        this.destinatario = destinatario;
    }

    public Contato getRemetente() {
        return remetente;
    }

    public void setRemetente(Contato remetente) {
        this.remetente = remetente;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public Date getHorario() {
        return horario;
    }

    public void setHorario(Date horario) {
        this.horario = horario;
    }
    
    
    
}
