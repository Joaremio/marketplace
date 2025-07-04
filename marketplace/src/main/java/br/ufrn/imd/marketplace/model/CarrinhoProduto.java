package br.ufrn.imd.marketplace.model;


public class CarrinhoProduto {
    private int carrinhoId;
    private int produtoId;
    private int quantidade;

    public CarrinhoProduto(int carrinhoId, int produtoId, int quantidade) {
        this.carrinhoId = carrinhoId;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
    }

    public CarrinhoProduto() {}

    public int getCarrinhoId() {
        return carrinhoId;
    }

    public void setCarrinhoId(int carrinhoId) {
        this.carrinhoId = carrinhoId;
    }

    public int getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
