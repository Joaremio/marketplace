package br.ufrn.imd.marketplace.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    int id;
    int compradorId;
    LocalDate dataPedido;
    String statusPedido;
    LocalDate previsaoEntrega;
    String efetivacao;
    Double valorTotal;
    String pagamentoForma;
    private List<PedidoProduto> itens;

    public List<PedidoProduto> getItens() {
        return itens;
    }

    public void setItens(List<PedidoProduto> itens) {
        this.itens = itens;
    }



    public Pedido(){
        this.itens = new ArrayList<PedidoProduto>();
        this.dataPedido = LocalDate.now();
    }

    public Pedido(int id, int compradorId, LocalDate dataPedido, String statusPedido, LocalDate previsaoEntrega, String efetivacao, Double valorTotal, String pagamentoForma, List<PedidoProduto> itens) {
        this.id = id;
        this.compradorId = compradorId;
        this.dataPedido = dataPedido;
        this.statusPedido = statusPedido;
        this.previsaoEntrega = previsaoEntrega;
        this.efetivacao = efetivacao;
        this.valorTotal = valorTotal;
        this.pagamentoForma = pagamentoForma;
        this.itens = itens;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompradorId() {
        return compradorId;
    }

    public void setCompradorId(int compradorId) {
        this.compradorId = compradorId;
    }

    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDate dataPedido) {
        this.dataPedido = dataPedido;
    }

    public String getStatusPedido() {
        return statusPedido;
    }

    public void setStatusPedido(String statusPedido) {
        this.statusPedido = statusPedido;
    }

    public LocalDate getPrevisaoEntrega() {
        return previsaoEntrega;
    }

    public void setPrevisaoEntrega(LocalDate previsaoEntrega) {
        this.previsaoEntrega = previsaoEntrega;
    }

    public String getEfetivacao() {
        return efetivacao;
    }

    public void setEfetivacao(String efetivacao) {
        this.efetivacao = efetivacao;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getPagamentoForma() {
        return pagamentoForma;
    }

    public void setPagamentoForma(String pagamentoForma) {
        this.pagamentoForma = pagamentoForma;
    }

}
