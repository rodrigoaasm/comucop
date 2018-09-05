package controller;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import javax.swing.JFrame;
import model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import tools.*;
import view.*;

public class ControllerFuncionario {

    //Declaração de variáveis auxiliares
    private Controller ctrMain;
    private ArrayList<Funcionario> listFuncs;
    private CadastroFuncionario cadFunc;
    private ConsultarFuncionario consFunc;
    private MyTableModel tabelaCons;

    //Metodo construtor
    public ControllerFuncionario(Controller pCtrPrincipal) {

        ctrMain = pCtrPrincipal;
        listFuncs = new ArrayList<>();

    }

    //Metodo que abrirá a janela desejada
    public void AbreJanela(int pCodJan) {
        if (pCodJan == 1) {
            cadFunc = new CadastroFuncionario(this, 1);
            cadFunc.setVisible(true);
            cadFunc.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        } else if (pCodJan == 2) {
            consFunc = new ConsultarFuncionario(this);
            consFunc.setVisible(true);
            consFunc.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        } else if (pCodJan == 3) {

        }
    }

    public void cadastraFuncionario(String nome, String sobrenome, String cpf, String departamento,
            String perfil, String login, String senha) {
        listFuncs.add(new Funcionario(nome, sobrenome, cpf, departamento, perfil, login, senha));
        this.AdicionaJson();
    }
    
    private void AdicionaJson() {
        JSONObject obj = new JSONObject();

        JSONArray lista = new JSONArray();
        for (Funcionario f : listFuncs) {
            JSONObject funcs = new JSONObject();
            funcs.put("Nome", f.getNome());
            funcs.put("Sobrenome", f.getSobrenome());
            funcs.put("CPF", f.getCpf());
            funcs.put("Departamento", f.getDepartamento());
            funcs.put("Perfil", f.getPerfil());
            funcs.put("Login", f.getLogin());
            funcs.put("Senha", f.getSenha());
            lista.add(funcs);
        }
        obj.put("Funcionarios", lista);

        try {

            FileWriter file = new FileWriter("funcionarios.json");
            file.write(obj.toJSONString());
            file.flush();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Metodo que adicionara dados na tabela, para sua visualização
    public void AdicionaDadosTabela() {
        tabelaCons = new MyTableModel();
        consFunc.carregaTabela(tabelaCons);
        tabelaCons.setRowCount(0);
        for (Funcionario f : listFuncs) {
            tabelaCons.addRow(new Object[]{
                f.getNome(),
                f.getSobrenome(),
                f.getDepartamento(),
                f.getPerfil()
            });
        }

    }

}
