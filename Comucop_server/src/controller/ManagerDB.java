/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import hibernate.HibernateUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import model.ClientConRecord;
import model.Departamento;
import model.ElemQueue;
import model.User;
import model.dao.GeneralDAO;
import model.dao.UserDAO;
import org.hibernate.Session;
import org.json.simple.JSONArray;
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
                    this.login(eq,jsonReq);
                }else if(jsonReq.remove("type","req-depart")){
                    this.reqDepart(eq.getClient());
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
            //Controi o JSON de resposta
            resp.put("type","login");
            resp.put("status","1");
            resp.put("nome",us.getFuncionario().getFuncNome());
            resp.put("sobrenome",us.getFuncionario().getFuncSobrenome());
            resp.put("perfil",us.getFuncionario().getFuncPerfil());
            
            ctrServ.getmSend().sendJSON(eq.getClient(), resp);//Envia JSON
            eq.getClient().setClient(us.getUsLogin());//Carregando informações do usuário no registro de conexão
            eq.getClient().setClientCod(us.getUsCod());
            ctrServ.getClients().add(eq.getClient());//adiciona conexão com o cliente
        }else{// se não retorna o erro
            resp.put("type","login");
            resp.put("status", "0");  
            ctrServ.getmSend().sendJSON(eq.getClient(), resp);    
         }   

    }  

    private void reqDepart(ClientConRecord client) {
        JSONObject resp = new JSONObject();
        //Recebe do banco todos os departamentos
        Iterator iDeps = GeneralDAO.all(sessDb, Departamento.class).iterator();        
        
        JSONArray lista = new JSONArray();//Instaciando sub-array json        
        while (iDeps.hasNext()) {//Construindo json de departamentos
            Departamento dep = (Departamento)iDeps.next();
            JSONObject jsonDep = new JSONObject();
            jsonDep.put("Codigo", dep.getDepCod());
            jsonDep.put("Nome", dep.getDepNome());
            jsonDep.put("Sigla", dep.getDepSigla());
            jsonDep.put("Descrição", dep.getDepDesc());
            lista.add(jsonDep);
        }
        resp.put("Departamentos", lista);//finaliza construção do json
        resp.put("type","req-depart");
        
        System.out.println("Enviado departamentos");
        ctrServ.getmSend().sendJSON(client, resp);//Envia json
        
    }
}
