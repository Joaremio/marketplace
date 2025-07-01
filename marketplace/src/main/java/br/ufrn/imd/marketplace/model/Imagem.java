package br.ufrn.imd.marketplace.model;

public class Imagem {
    String ImagemCaminho;
    int produtoId;

    public Imagem(String imagemCaminho, int produtoId) {
        ImagemCaminho = imagemCaminho;
        this.produtoId = produtoId;
    }

    public String getImagemCaminho() {
        return ImagemCaminho;
    }

    public void setImagemCaminho(String imagemCaminho) {
        ImagemCaminho = imagemCaminho;
    }

    public int getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }
}
