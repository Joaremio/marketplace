package br.ufrn.imd.marketplace.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Vendedor extends Usuario{

    private String status;
    private LocalDate dataAnalise;

    public Vendedor(){super();}

    public Vendedor(int id,
                    String nome,
                    String cpf,
                    String email,
                    String senha,
                    String telefone,
                    LocalDate dataCadastro,
                    String status,
                    LocalDate dataAnalise) {
        super(id, nome, cpf, email, senha, telefone, dataCadastro);
        this.status = status;
        this.dataAnalise = dataAnalise;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDataAnalise() {
        return dataAnalise;
    }

    public void setDataAnalise(LocalDate dataAnalise) {
        this.dataAnalise = dataAnalise;
    }


}

