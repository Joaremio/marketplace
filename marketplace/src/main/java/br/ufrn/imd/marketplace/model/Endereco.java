package br.ufrn.imd.marketplace.model;

public class Endereco {
    private int id;
    private int usuarioId;
    private String numeroEndereco;
    private String complemento;
    private String tipoEndereco;
    private boolean isEnderecoPrincipal;
    private String cep;



    public Endereco() {}

    public Endereco(int usuarioId, String numeroEndereco, String complemento, String tipoEndereco, boolean enderecoPrincipal, String cep) {
        this.usuarioId = usuarioId;
        this.numeroEndereco = numeroEndereco;
        this.complemento = complemento;
        this.tipoEndereco = tipoEndereco;
        this.isEnderecoPrincipal = enderecoPrincipal;
        this.cep = cep;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNumeroEndereco() {
        return numeroEndereco;
    }

    public void setNumeroEndereco(String numeroEndereco) {
        this.numeroEndereco = numeroEndereco;
    }

    public String getTipoEndereco() {
        return tipoEndereco;
    }

    public void setTipoEndereco(String tipoEndereco) {
        this.tipoEndereco = tipoEndereco;
    }

    public boolean isEnderecoPrincipal() {
        return isEnderecoPrincipal;
    }

    public void setEnderecoPrincipal(boolean enderecoPrincipal) {
        this.isEnderecoPrincipal = enderecoPrincipal;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }
}
