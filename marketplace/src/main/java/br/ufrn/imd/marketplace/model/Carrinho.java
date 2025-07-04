package br.ufrn.imd.marketplace.model;

import java.util.List;

public class Carrinho {
    int id;
    int compradorId;

    public Carrinho(int id, int compradorId) {
        this.id = id;
        this.compradorId = compradorId;
    }

    public Carrinho() {}


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
