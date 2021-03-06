package controller;

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

public class ControllerDep {

    //Declaração de variáveis auxiliares
    private static int count = 0;
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
        listaDeps.add(new Departamento(++count, pNome, pSigla, pDescricao, "cadastro"));
        this.AdicionaJson();
    }

    //Metodo responsável por alterar dados
    public void AlterarDepartamento(Integer codigo, String pNome, String pSigla, String pDescricao) {
        listaDeps.set(index, new Departamento(codigo, pNome, pSigla, pDescricao, "cadastro"));
        this.AdicionaJson();
    }

    //Metodo responsável por remover dados
    public void RemoverDepartamento() {
        listaDeps.remove(listaDeps.get(index));
        AdicionaDadosTabela();
        this.AdicionaJson();
    }

    //Metodo que adicionaa os dados do array de departamentos no json
    private void AdicionaJson() {
        JSONObject obj = new JSONObject();

        JSONArray lista = new JSONArray();
        for (Departamento dp : listaDeps) {
            JSONObject deps = new JSONObject();
            deps.put("Codigo", dp.getCodigo());
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

    //Metodo responsavel por passar o Json para o array
    public void LeituraJson(JSONObject jsonObj) {
        JSONArray deps = (JSONArray) jsonObj.get("Departamentos");
        Iterator<JSONObject> ite = deps.iterator();
        while (ite.hasNext()) {
            JSONObject objDep = (JSONObject) ite.next();
            Long codigo = (Long) objDep.get("Codigo");
            String nome = (String) objDep.get("Nome");
            String sigla = (String) objDep.get("Sigla");
            String decricao = (String) objDep.get("Descrição");
            String tipo = (String) objDep.get("Tipo");
            Integer cod = 1;
            try {
                cod = Integer.valueOf(codigo.toString());
            } catch (Exception e) {
                System.out.println("Capacidade do Integer estourou.");
            }
            Departamento dp = new Departamento(
                    cod, nome, sigla, decricao, tipo
            );
            listaDeps.add(dp);
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
