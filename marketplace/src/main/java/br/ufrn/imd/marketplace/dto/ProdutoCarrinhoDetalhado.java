package br.ufrn.imd.marketplace.dto;

public class ProdutoCarrinhoDetalhado {
    private int produtoId;
    private String nome;
    private double preco;
    private String imagemUrl;
    private int quantidade;

    public ProdutoCarrinhoDetalhado(int produtoId, String nome, double preco, String imagemUrl, int quantidade) {
        this.produtoId = produtoId;
        this.nome = nome;
        this.preco = preco;
        this.imagemUrl = imagemUrl;
        this.quantidade = quantidade;
    }

    public ProdutoCarrinhoDetalhado() {}

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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }
}
