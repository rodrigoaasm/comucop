package controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JFrame;
import model.Departamento;
import tools.MyTableModel;
import view.CadastroDepartamento;
import view.ConsultarDepartamento;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ControllerDep {

    //Declaração de variáveis auxiliares
    private Controller ctrMain;
    private ArrayList<Departamento> listaDeps;
    private CadastroDepartamento depCad;
    private ConsultarDepartamento consDep;
    private MyTableModel tabelaCons;
    private Integer index;

    //Metodo Construtor
    public ControllerDep(Controller pCtr) {
        ctrMain = pCtr;
        listaDeps = new ArrayList<>();
       
    }

    //Metodo que abrirá a janela desejada
    public void AbreJanela(int pCodJan) {
        if (pCodJan == 1) {
            depCad = new CadastroDepartamento(this, -1);
            depCad.setLocationRelativeTo(null);
            depCad.setVisible(true);
            depCad.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        } else if (pCodJan == 2) {
            depCad = new CadastroDepartamento(this, index);
            depCad.AlterarCadastro(listaDeps.get(index));
            depCad.setLocationRelativeTo(null);
            depCad.setVisible(true);
            depCad.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        } else if (pCodJan == 3) {
            consDep = new ConsultarDepartamento(this);
            AdicionaDadosTabela();
            consDep.setLocationRelativeTo(null);
            consDep.setVisible(true);
            consDep.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }
    }

    //Metodo de Cadastro no Array de Departamentos
    public void CadastraDepartamento(String pNome, String pSigla, String pDescricao) {
        listaDeps.add(new Departamento(pNome, pSigla, pDescricao, "cadastro"));
        this.AdicionaJson();
    }

    //Metodo responsável por alterar dados
    public void AlterarDepartamento(String pNome, String pSigla, String pDescricao) {
        listaDeps.set(index, new Departamento(pNome, pSigla, pDescricao, "cadastro"));
        this.AdicionaJson();
    }

    //Metodo responsável por remover dados
    public void RemoverDepartamento() {
        listaDeps.remove(listaDeps.get(index));
        AdicionaDadosTabela();
        this.AdicionaJson();
    }

    private void AdicionaJson() {
        JSONObject obj = new JSONObject();

        JSONArray lista = new JSONArray();
        for (Departamento dp : listaDeps) {
            JSONObject deps = new JSONObject();
            deps.put("Nome", dp.getNome());
            deps.put("Sigla", dp.getSigla());
            deps.put("Descrição", dp.getDescricao());
            deps.put("Tipo", dp.getTipo());
            lista.add(deps);
        }
        obj.put("Departamentos", lista);

        try {

            FileWriter file = new FileWriter("departamento.json");
            file.write(obj.toJSONString());
            file.flush();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void LeituraJson(JSONObject jsonResp) {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("departamento.json"));
            JSONObject jsonObj = (JSONObject) obj;

            JSONArray deps = (JSONArray) jsonObj.get("Departamentos");
            Iterator<JSONObject> ite = deps.iterator();
            while (ite.hasNext()) {
                JSONObject objDep = (JSONObject) ite.next();
                String nome = (String) objDep.get("Nome");
                String sigla = (String) objDep.get("Sigla");
                String decricao = (String) objDep.get("Descrição");
                String tipo = (String) objDep.get("Tipo");

                Departamento dp = new Departamento(
                        nome, sigla, decricao, tipo
                );
                listaDeps.add(dp);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //Metodo que adicionara dados na tabela, para sua visualização
    public void AdicionaDadosTabela() {
        tabelaCons = new MyTableModel();
        consDep.carregaTabela(tabelaCons);
        tabelaCons.setRowCount(0);
        for (Departamento p : listaDeps) {
            tabelaCons.addRow(new Object[]{
                p.getSigla(),
                p.getNome()
            });
        }

    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public ArrayList<Departamento> getListaDeps() {
        return listaDeps;
    }
    
    

}
