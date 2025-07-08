package br.ufrn.imd.marketplace.dto;

import java.time.LocalDate;

public class PedidoProdutoVendedorDTO {
    private int pedidoId;
    private LocalDate dataPedido;
    private String statusPedido;
    private int produtoId;
    private String nomeProduto;
    private int quantidade;
    private double precoUnitario;

    public PedidoProdutoVendedorDTO(int pedidoId, LocalDate dataPedido, String statusPedido,
                                    int produtoId, String nomeProduto, int quantidade, double precoUnitario) {
        this.pedidoId = pedidoId;
        this.dataPedido = dataPedido;
        this.statusPedido = statusPedido;
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public int getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(int pedidoId) {
        this.pedidoId = pedidoId;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getStatusPedido() {
        return statusPedido;
    }

    public void setStatusPedido(String statusPedido) {
        this.statusPedido = statusPedido;
    }

    public int getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }

    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDate dataPedido) {
        this.dataPedido = dataPedido;
    }
}

