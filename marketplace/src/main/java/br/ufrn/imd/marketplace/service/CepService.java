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

    public void salvarOuAtualizar(Cep cep) {
        try {
            cepDAO.salvarOuAtualizar(cep);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar ou atualizar CEP", e);
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




}
