package br.ufrn.imd.marketplace.dto;

import br.ufrn.imd.marketplace.model.Cep;
import br.ufrn.imd.marketplace.model.Endereco;

public class EnderecoCepDTO {
    private Cep cep;
    private Endereco endereco;

    public EnderecoCepDTO(Cep cep, Endereco endereco) {
        this.cep = cep;
        this.endereco = endereco;
    }
    public EnderecoCepDTO() {}

    public Cep getCep() {
        return cep;
    }

    public void setCep(Cep cep) {
        this.cep = cep;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
}
