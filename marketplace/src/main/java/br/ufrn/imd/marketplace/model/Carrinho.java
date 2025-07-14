package br.ufrn.imd.marketplace.model;

import br.ufrn.imd.marketplace.dto.ProdutoCarrinhoDetalhado;
import java.util.List;

public class Carrinho {
    int id;
    int compradorId;
    private List<ProdutoCarrinhoDetalhado> produtos;

    public Carrinho(int id, int compradorId) {
        this.id = id;
        this.compradorId = compradorId;
    }

    public Carrinho() {}

    public List<ProdutoCarrinhoDetalhado> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ProdutoCarrinhoDetalhado> produtos) {
        this.produtos = produtos;
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
}