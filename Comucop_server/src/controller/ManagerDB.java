/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import hibernate.HibernateUtil;
import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ElemQueue;
import model.User;
import model.dao.UserDAO;
import org.hibernate.Session;
import org.json.simple.JSONObject;

/**
 *
 * @author Rodrigo Maia
 */
public class ManagerDB extends Thread{
    
    private Controller ctrServ;
    private Session sessDb;
    
   

    public ManagerDB(Controller ctrServ) {
        super();
        this.ctrServ = ctrServ;        
        sessDb = HibernateUtil.getSessionFactory().openSession();
    }
    
    @Override
    public void run(){
        while(true){       
            Queue<ElemQueue> queueDb = ctrServ.getQueueManDB();
            if(!queueDb.isEmpty()){ //Verificando se há elementos na fila            
                ElemQueue eq = queueDb.poll();//recuperando elemento 
                JSONObject jsonReq = eq.getJsonReq();//recuperando requisição
                
                if(jsonReq.remove("type","login")){//Analisando operação que será efetuada no banco
                    login(eq,jsonReq);
                }
            }
        }
    }
    
    public void login(ElemQueue eq,JSONObject jsonReq){//Operação de login
        JSONObject resp = new JSONObject();
        //Faz a pesquisa do usuário
        List l = (List)UserDAO.auth(sessDb,User.class,(String)jsonReq.get("login"),(String)jsonReq.get("password"));
        if(!l.isEmpty()){//Se encontrar algum usuario valido retorna as informações do usuario com a resposta de confirmação
            User us = (User) l.get(0);            
            resp.put("status","1");
            resp.put("nome",us.getFuncionario().getFuncNome());
            resp.put("sobrenome",us.getFuncionario().getFuncSobrenome());
            resp.put("perfil",us.getFuncionario().getFuncPerfil());
            ctrServ.getmSend().sendJSON(eq.getClient(), resp);
        }else{// se não retorna o erro
            resp.put("status", "0");  
            ctrServ.getmSend().sendJSON(eq.getClient(), resp);            
        }   

    }
}
