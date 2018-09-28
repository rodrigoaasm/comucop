package model;
// Generated 25/09/2018 14:00:12 by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Departamento generated by hbm2java
 */
public class Departamento  implements java.io.Serializable {


     private Integer depCod;
     private String depNome;
     private String depSigla;
     private String depDesc;
     private Set funcionarios = new HashSet(0);

    public Departamento() {
    }

	
    public Departamento(String depNome, String depSigla) {
        this.depNome = depNome;
        this.depSigla = depSigla;
    }
    public Departamento(String depNome, String depSigla, String depDesc, Set funcionarios) {
       this.depNome = depNome;
       this.depSigla = depSigla;
       this.depDesc = depDesc;
       this.funcionarios = funcionarios;
    }
   
    public Integer getDepCod() {
        return this.depCod;
    }
    
    public void setDepCod(Integer depCod) {
        this.depCod = depCod;
    }
    public String getDepNome() {
        return this.depNome;
    }
    
    public void setDepNome(String depNome) {
        this.depNome = depNome;
    }
    public String getDepSigla() {
        return this.depSigla;
    }
    
    public void setDepSigla(String depSigla) {
        this.depSigla = depSigla;
    }
    public String getDepDesc() {
        return this.depDesc;
    }
    
    public void setDepDesc(String depDesc) {
        this.depDesc = depDesc;
    }
    public Set getFuncionarios() {
        return this.funcionarios;
    }
    
    public void setFuncionarios(Set funcionarios) {
        this.funcionarios = funcionarios;
    }




}

