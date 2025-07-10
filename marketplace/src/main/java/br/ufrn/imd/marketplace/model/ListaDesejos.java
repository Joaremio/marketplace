package br.ufrn.imd.marketplace.model;


import java.util.ArrayList;
import java.util.List;

public class ListaDesejos {
    private int id;
    private String nome;
    private int compradorId;

    public ListaDesejos(int id, String nome, int compradorId) {
        this.id = id;
        this.nome = nome;
        this.compradorId = compradorId;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
