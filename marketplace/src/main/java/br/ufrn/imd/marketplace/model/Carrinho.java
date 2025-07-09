package br.ufrn.imd.marketplace.model;

import br.ufrn.imd.marketplace.dto.ProdutoCarrinhoDetalhado; // 1. IMPORTAR O DTO
import java.util.List;

public class Carrinho {
    int id;
    int compradorId;
    private List<ProdutoCarrinhoDetalhado> produtos; // 2. ADICIONAR A LISTA

    public Carrinho(int id, int compradorId) {
        this.id = id;
        this.compradorId = compradorId;
    }

    public Carrinho() {}

    // 3. ADICIONAR GETTERS E SETTERS PARA A LISTA
    public List<ProdutoCarrinhoDetalhado> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ProdutoCarrinhoDetalhado> produtos) {
        this.produtos = produtos;
    }

    // Getters e Setters existentes
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
}