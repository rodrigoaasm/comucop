/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import hibernate.HibernateUtil;
import java.util.Iterator;
import java.util.List;
import model.Departamento;
import model.Funcionario;
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
public class ManagerDB{
    
    private Controller ctrServ;
    private Session sessDb;
    
   
    /*Contrutor inicia conexão hibernate com o mysql*/
    public ManagerDB(Controller ctrServ) {
        this.ctrServ = ctrServ;        
        sessDb = HibernateUtil.getSessionFactory().openSession();
    }
    
  /*  @Override
    public void run(){
        while(true){       
         //   Queue<ElemQueue> queueDb = ctrServ.getQueueManDB();
        /*    if(!queueDb.isEmpty()){ //Verificando se há elementos na fila            
                ElemQueue eq = queueDb.poll();//recuperando elemento 
                JSONObject jsonReq = eq.getJsonReq();//recuperando requisição
                
                if(jsonReq.remove("type","login")){//Analisando operação que será efetuada no banco
                    this.login(eq,jsonReq);
                }else if(jsonReq.remove("type","req-depart")){
                    this.reqDepart(eq.getClient());
                }else if(jsonReq.remove("type","exp-to-contacts")){
                    this.expToContacts(eq,jsonReq);
                }
            }
        
    }*/
    
    /*Método responsável pela função de login*/
    public JSONObject login(JSONObject jsonReq){//Operação de login
        JSONObject resp = new JSONObject();
        //Faz a pesquisa do usuário
        System.err.println("Consultado DB");
        List l = (List)UserDAO.auth(sessDb,(String)jsonReq.get("login"),(String)jsonReq.get("password"));
        if(!l.isEmpty()){//Se encontrar algum usuario valido retorna as informações do usuario com a resposta de confirmação
            User us = (User) l.get(0); 
            //Constroi o JSON de resposta
            resp.put("type","login");
            resp.put("status","1");
            resp.put("codigo","" + us.getFuncionario().getFuncCod());
            resp.put("nome",us.getFuncionario().getFuncNome());
            resp.put("sobrenome",us.getFuncionario().getFuncSobrenome());
            resp.put("perfil",us.getFuncionario().getFuncPerfil());         
        }else{// se não retorna o erro
            resp.put("type","login");
            resp.put("status", "0");    
        }   
        return resp;
    }  

    /*Método responsável por retorna a lista de departamentos*/
    public JSONObject reqDepart() {
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
        return resp;
    }

    /*Método que retorna a lista de funcionários por departamento*/
    public JSONObject expToContacts(JSONObject jsonReq) {
        JSONObject resp = new JSONObject();
        //Recebe do banco todos os departamentos
        String s = (String)jsonReq.get("id-depart");//recuperando codigo        
        Departamento dep = (Departamento)GeneralDAO.load(sessDb,
                Departamento.class, 
                Integer.parseInt(s)); //Recuperando id e convertendo para int
        Iterator iFunc = dep.getFuncionarios().iterator();
        
        JSONArray lista = new JSONArray();//Instaciando sub-array json  
        while(iFunc.hasNext()){//Lendo os funcionários do departamento
            Funcionario f = (Funcionario) iFunc.next();
            //Preechendo json de um funcionario
            JSONObject jsonDep = new JSONObject();            
            jsonDep.put("Codigo", f.getFuncCod());
            jsonDep.put("Nome", f.getFuncNome());
            jsonDep.put("Sobrenome", f.getFuncSobrenome());
            jsonDep.put("Perfil", f.getFuncPerfil());
            lista.add(jsonDep);//Adicionando funcionário ao array
        }
        resp.put("Contatos", lista);//finaliza construção do json
        resp.put("type","exp-to-contacts");
        resp.put("id-depart",s);
        
        return resp;
    }
}
