package br.ufrn.imd.marketplace.dto;

public class AvaliacaoRequest {
    private Integer pedidoId;
    private Integer produtoId;
    private String avaliacao;
    private String nomeComprador;

    public AvaliacaoRequest(Integer pedidoId, Integer produtoId, String avaliacao) {
        this.pedidoId = pedidoId;
        this.produtoId = produtoId;
        this.avaliacao = avaliacao;
    }

    public AvaliacaoRequest () {}

    public AvaliacaoRequest(String avaliacao, String nomeComprador) {
        this.avaliacao = avaliacao;
        this.nomeComprador = nomeComprador;
    }
    public AvaliacaoRequest(Integer pedidoId, Integer produtoId, String avaliacao, String nomeComprador) {
        this.pedidoId = pedidoId;
        this.produtoId = produtoId;
        this.avaliacao = avaliacao;
        this.nomeComprador = nomeComprador;
    }

    public Integer getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Integer pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Integer getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Integer produtoId) {
        this.produtoId = produtoId;
    }


    public String getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(String avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getNomeComprador() {
        return nomeComprador;
    }

    public void setNomeComprador(String nomeComprador) {
        this.nomeComprador = nomeComprador;
    }
}