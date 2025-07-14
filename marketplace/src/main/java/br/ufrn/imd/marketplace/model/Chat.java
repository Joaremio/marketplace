package br.ufrn.imd.marketplace.model;

import java.time.LocalDate;

public class Chat {
    private int id;
    private int compradorId;
    private int vendedorId;
    private LocalDate dataCriacao;

    public Chat() {}
    public Chat(int id, int compradorId, int vendedorId, LocalDate dataCriacao) {
        this.id = id;
        this.compradorId = compradorId;
        this.vendedorId = vendedorId;
        this.dataCriacao = dataCriacao;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCompradorId() { return compradorId; }
    public void setCompradorId(int compradorId) { this.compradorId = compradorId; }
    public int getVendedorId() { return vendedorId; }
    public void setVendedorId(int vendedorId) { this.vendedorId = vendedorId; }
    public LocalDate getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDate dataCriacao) { this.dataCriacao = dataCriacao; }
}
