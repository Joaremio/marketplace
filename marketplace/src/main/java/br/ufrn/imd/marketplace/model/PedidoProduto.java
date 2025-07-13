package br.ufrn.imd.marketplace.model;


public class PedidoProduto {
    private Integer pedidoId;
    private Integer produtoId;
    private String nome;           // nome do produto
    private String vendedorNome;   // nome do vendedor
    private int quantidade;
    private double precoUnidade;
    private String imageUrl;
    private String avaliacao;

    public PedidoProduto(Integer pedidoId, Integer produtoId, String nome, String vendedorNome, int quantidade, double precoUnidade, String imageUrl) {
        this.pedidoId = pedidoId;
        this.produtoId = produtoId;
        this.nome = nome;
        this.vendedorNome = vendedorNome;
        this.quantidade = quantidade;
        this.precoUnidade = precoUnidade;
        this.imageUrl = imageUrl;
    }

    public PedidoProduto(){}

    // getters e setters padrão

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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getVendedorNome() {
        return vendedorNome;
    }

    public void setVendedorNome(String vendedorNome) {
        this.vendedorNome = vendedorNome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPrecoUnidade() {
        return precoUnidade;
    }

    public void setPrecoUnidade(double precoUnidade) {
        this.precoUnidade = precoUnidade;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(String avaliacao) {
        this.avaliacao = avaliacao;
    }
}
