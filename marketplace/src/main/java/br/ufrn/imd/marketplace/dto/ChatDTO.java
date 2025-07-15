package br.ufrn.imd.marketplace.dto;

import br.ufrn.imd.marketplace.model.Chat;

public class ChatDTO extends Chat {
    private String nomeComprador;
    private String nomeVendedor;

    // Getters e Setters para os novos campos
    public String getNomeComprador() {
        return nomeComprador;
    }
    public void setNomeComprador(String nomeComprador) {
        this.nomeComprador = nomeComprador;
    }
    public String getNomeVendedor() {
        return nomeVendedor;
    }
    public void setNomeVendedor(String nomeVendedor) {
        this.nomeVendedor = nomeVendedor;
    }
}