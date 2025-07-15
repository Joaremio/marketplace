package br.ufrn.imd.marketplace.model;

import java.sql.Timestamp;

public class Mensagem {
    private int id;
    private int usuarioId;
    private int chatId;
    private String texto;
    private Timestamp data_hora;

    public Mensagem() {}

    public Mensagem(int id, int usuarioId, int chatId, String texto, Timestamp data_hora) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.chatId = chatId;
        this.texto = texto;
        this.data_hora = data_hora;
    }

    public int getId() {
        return id;
    }

    public String getTexto() {
        return texto;
    }

    public Timestamp getDataHora() {
        return data_hora;
    }
    public int getUsuarioId() {
        return usuarioId;
    }
    public int getChatId() {
        return chatId;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setTexto(String texto) {
        this.texto = texto;
    }
    public void setDataHora(Timestamp data_hora) {
        this.data_hora = data_hora;
    }
    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

}
