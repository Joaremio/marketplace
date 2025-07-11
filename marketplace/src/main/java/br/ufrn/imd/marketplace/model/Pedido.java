package br.ufrn.imd.marketplace.model;

import br.ufrn.imd.marketplace.StatusPedido;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    int id;
    int compradorId;
    LocalDate dataPedido;
    private StatusPedido statusPedido;
    LocalDate previsaoEntrega;
    String efetivacao;
    Double valorTotal;
    String pagamentoForma;
    private List<PedidoProduto> itens;

    public Pedido(String pagamentoForma, Double valorTotal, String efetivacao, LocalDate previsaoEntrega, StatusPedido statusPedido, LocalDate dataPedido, int compradorId, int id) {
        this.pagamentoForma = pagamentoForma;
        this.valorTotal = valorTotal;
        this.efetivacao = efetivacao;
        this.previsaoEntrega = previsaoEntrega;
        this.statusPedido = statusPedido;
        this.dataPedido = dataPedido;
        this.compradorId = compradorId;
        this.id = id;
    }

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

    public StatusPedido getStatusPedido() {
        return statusPedido;
    }

    public void setStatusPedido(StatusPedido statusPedido) {
        this.statusPedido = statusPedido;
    }
}
