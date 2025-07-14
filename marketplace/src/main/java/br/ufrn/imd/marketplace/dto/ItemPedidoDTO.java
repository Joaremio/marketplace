package br.ufrn.imd.marketplace.dto;

public class ItemPedidoDTO {
    private Integer pedidoId;
    private Integer produtoId;
    private String nome;
    private String imageUrl;
    private int quantidade;
    private double precoUnitario;
    private String vendedorNome;

    public ItemPedidoDTO() {}

    public ItemPedidoDTO(Integer produtoId, String nome, String imageUrl, int quantidade, double precoUnitario, String vendedorNome) {
        this.produtoId = produtoId;
        this.nome = nome;
        this.imageUrl = imageUrl;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.vendedorNome = vendedorNome;
    }

    // Getters e Setters
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public String getVendedorNome() {
        return vendedorNome;
    }

    public void setVendedorNome(String vendedorNome) {
        this.vendedorNome = vendedorNome;
    }
}
