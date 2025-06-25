package br.ufrn.imd.marketplace.model;


import java.time.LocalDate;
import java.util.Date;

public class Usuario {
    private int id;
    private String nome;
    private String cpf;
    private String email;
    private String senha;
    private LocalDate data_cadastro;


    public Usuario() {
        this.data_cadastro = LocalDate.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public LocalDate getDataCadastro() {
        return data_cadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.data_cadastro = dataCadastro;
    }
}
