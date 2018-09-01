package model;

public class Funcionario {

    private String nome;
    private String cpf;
    private String departamento;
    private String perfil;

    public Funcionario(String pNome, String pCpf, String pDepartamento, String pPerfil) {
        nome = pNome;
        cpf = pCpf;
        departamento = pDepartamento;
        perfil = pPerfil;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

}
