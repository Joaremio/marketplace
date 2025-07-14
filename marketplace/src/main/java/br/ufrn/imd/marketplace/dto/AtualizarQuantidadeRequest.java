package br.ufrn.imd.marketplace.dto;

public class AtualizarQuantidadeRequest {
    private int carrinhoId;
    private int produtoId;
    private int quantidade;

    public int getCarrinhoId() { return carrinhoId; }
    public void setCarrinhoId(int carrinhoId) { this.carrinhoId = carrinhoId; }
    public int getProdutoId() { return produtoId; }
    public void setProdutoId(int produtoId) { this.produtoId = produtoId; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
}