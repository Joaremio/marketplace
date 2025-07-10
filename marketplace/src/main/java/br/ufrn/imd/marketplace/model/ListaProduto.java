package br.ufrn.imd.marketplace.model;

public class ListaProduto {
    private int produtoId;
    private int listaId;

    public ListaProduto(int produtoId, int listaId) {
        this.produtoId = produtoId;
        this.listaId = listaId;
    }

    public int getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }

    public int getListaId() {
        return listaId;
    }

    public void setListaId(int listaId) {
        this.listaId = listaId;
    }
}
