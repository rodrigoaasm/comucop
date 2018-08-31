package controller;

import java.util.ArrayList;
import javax.swing.JFrame;
import model.Departamento;
import tools.MyTableModel;
import view.CadastroDepartamento;
import view.ConsultarDepartamento;

public class ControllerDep {
    //Declaração de variáveis auxiliares
    private Controller ctrMain;
    private ArrayList<Departamento> listaDeps;
    private CadastroDepartamento depCad;
    private ConsultarDepartamento consDep;
    private MyTableModel tabelaCons;
    
    //Metodo Construtor
    public ControllerDep(Controller pCtr) {
        ctrMain = pCtr;
        listaDeps = new ArrayList<>();
    }
    
    //Metodo que abrirá a janela desejada
    public void AbreJanela(int pCodJan) {
        if (pCodJan == 1) {
            depCad = new CadastroDepartamento(this);
            depCad.setVisible(true);
            depCad.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        } else if (pCodJan == 2) {

        } else if(pCodJan == 3){
            consDep = new ConsultarDepartamento(this);
            AdicionaDadosTabela();
            consDep.setVisible(true);
            consDep.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }
    }
    
    //Metodo de Cadastro no Array de Departamentos
    public void CadastraDepartamento(String pNome, String pSigla, String pDescricao) {
        listaDeps.add(new Departamento(pNome, pSigla, pDescricao, "cadastro"));
    }
    
    
    public void RetornaDepartamentos() {

    }
    
    //Metodo responsável por alterar dados
    public void AlterarDepartamento() {

    }
    
    //Metodo responsável por remover dados
    public void RemoverDepartamento() {

    }
    
    //Metodo que adicionara dados na tabela, para sua visualização
    public void AdicionaDadosTabela(){
        tabelaCons = new MyTableModel();
        consDep.carregaTabela(tabelaCons);
        tabelaCons.setRowCount(0);
        for(Departamento p: listaDeps){
            tabelaCons.addRow(new Object[]{
                p.getSigla(),
                p.getNome()
            });
        }
        
    }
    
}
