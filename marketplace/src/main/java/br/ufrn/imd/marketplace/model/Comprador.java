package br.ufrn.imd.marketplace.model;

import java.time.LocalDate;

    public class Comprador extends Usuario {
    public Comprador() {
        super();
    }
    public Comprador(int id,
                         String nome,
                         String cpf,
                         String email,
                         String senha,
                         String telefone,
                         LocalDate dataCadastro) {
        super(id, nome, cpf, email, senha, telefone, dataCadastro);
    }
}
