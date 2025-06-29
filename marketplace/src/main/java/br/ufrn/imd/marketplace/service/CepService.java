package br.ufrn.imd.marketplace.service;


import br.ufrn.imd.marketplace.dao.CepDAO;
import br.ufrn.imd.marketplace.dao.UsuarioDAO;
import br.ufrn.imd.marketplace.model.Cep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class CepService {

    @Autowired
    private CepDAO cepDAO;
    @Autowired
    private UsuarioDAO usuarioDAO;

    public void inserirCep(Cep cep) {
        try {
            if (cepDAO.cepExiste(cep.getCep())) {
                System.out.println("CEP já existe, reutilizando.");
                return;
            }
            cepDAO.inserirCep(cep);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir CEP", e);
        }
    }

    public List<Cep> listarCeps() {
        try{
           return cepDAO.listarCeps();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Cep buscarCep(String cep) {
        try{
            return cepDAO.buscarCep(cep);
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean excluirCep(String cep) {
        try {
            return cepDAO.excluirCep(cep);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir CEP: " + cep, e);
        }
    }


    public Cep alterarCep(String cepAntigo, Cep cepAtualizado) {
        try {
            return cepDAO.atualizarCep(cepAntigo, cepAtualizado);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar CEP: " + cepAntigo, e);
        }
    }

}
