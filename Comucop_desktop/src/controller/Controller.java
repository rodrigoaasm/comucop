/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.Socket;
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
    
    //Sockets de conexão TCP
    private Socket server;
    private Socket broker;

    public Controller() {        
        this.mWin = new MainWindow(this);
        mWin.setVisible(true);
        ctrDep = new ControllerDep(this);
        ctrFunc = new ControllerFuncionario(this);
    }  
    
    public ControllerDep getCtrDep() {
        return ctrDep;
    }
    
    public ControllerFuncionario getCtrFunc(){
        return ctrFunc;
    }
    
    public  static void main (String args[]){
       Controller c = new Controller();
    }

    public Socket getSocketServer() {
        return server;
    }

    public Socket getSocketBroker() {
        return broker;
    }     
    
}
