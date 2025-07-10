// Em: br.ufrn.imd.marketplace.dto.PacoteVendedorDTO.java
package br.ufrn.imd.marketplace.dto;

import java.time.LocalDate;
import java.util.List;

public class PacoteVendedorDTO {

    private String vendedorNome;
    private LocalDate previsaoEntrega;
    private String statusPacote;
    private List<ItemPedidoDTO> itens; // Cada pacote tem sua própria lista de itens

    // Getters e Setters
    public String getVendedorNome() {
        return vendedorNome;
    }

    public void setVendedorNome(String vendedorNome) {
        this.vendedorNome = vendedorNome;
    }

    public LocalDate getPrevisaoEntrega() {
        return previsaoEntrega;
    }

    public void setPrevisaoEntrega(LocalDate previsaoEntrega) {
        this.previsaoEntrega = previsaoEntrega;
    }

    public String getStatusPacote() {
        return statusPacote;
    }

    public void setStatusPacote(String statusPacote) {
        this.statusPacote = statusPacote;
    }

    public List<ItemPedidoDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoDTO> itens) {
        this.itens = itens;
    }
}