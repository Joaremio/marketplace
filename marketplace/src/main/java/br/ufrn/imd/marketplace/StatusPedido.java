package br.ufrn.imd.marketplace;

public enum StatusPedido {
    PENDENTE("Pendente"),
    EM_ANDAMENTO("Em Andamento"),
    ENVIADO("Enviado"),
    ENTREGUE("Entregue"),
    CANCELADO("Cancelado");

    private final String descricao;

    StatusPedido(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

