package br.ufrn.imd.marketplace.dto;

public class RedefinirSenhaRequest {
    private String email;
    private String cpf;
    private String novaSenha;

    public RedefinirSenhaRequest() {
    }
    public RedefinirSenhaRequest(String email, String cpf, String novaSenha) {
        this.email = email;
        this.cpf = cpf;
        this.novaSenha = novaSenha;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public String getNovaSenha() {
        return novaSenha;
    }
    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }   
}