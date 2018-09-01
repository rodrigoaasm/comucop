package controller;

import java.util.*;
import javax.swing.JFrame;
import model.*;
import tools.*;
import view.*;

public class ControllerFuncionario {
   
    //Declaração de variáveis auxiliares
    private Controller ctrMain;
    private ArrayList<Funcionario> listaFunc;
    private CadastroFuncionario cadFunc;
    private MyTableModel tabelaCons;
    
    //Metodo construtor
    public ControllerFuncionario(Controller pCtrPrincipal){
        
        ctrMain = pCtrPrincipal;
        listaFunc = new ArrayList<>();
        
    }
    
   //Metodo que abrirá a janela desejada
    public void AbreJanela(int pCodJan) {
        if (pCodJan == 1) {
            cadFunc = new CadastroFuncionario(this);
            cadFunc.setVisible(true);
            cadFunc.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        } else if (pCodJan == 2) {

        } else if(pCodJan == 3){
            
        }
    }
    
}
