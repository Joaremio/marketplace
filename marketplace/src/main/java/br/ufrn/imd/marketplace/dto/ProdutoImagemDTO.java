package br.ufrn.imd.marketplace.dto;

import br.ufrn.imd.marketplace.model.Produto;

public class ProdutoImagemDTO extends Produto {

    String imagemUrl;

    public ProdutoImagemDTO(int id, int vendedorId, String nome, Double preco, String descricao, int estoque, String categoria, String imagemUrl) {
        super(id, vendedorId, nome, preco, descricao, estoque, categoria);
        this.imagemUrl = imagemUrl;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }
}
