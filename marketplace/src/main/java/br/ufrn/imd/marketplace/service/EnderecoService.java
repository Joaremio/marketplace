package br.ufrn.imd.marketplace.service;

import br.ufrn.imd.marketplace.dao.EnderecoDAO;
import br.ufrn.imd.marketplace.dto.EnderecoCepDTO;
import br.ufrn.imd.marketplace.model.Cep;
import br.ufrn.imd.marketplace.model.Endereco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoDAO enderecoDAO;

    @Autowired
    private CepService cepService;
    public Endereco inserirEndereco(int usuarioId, Endereco endereco) {
        try {
            if (endereco.isEnderecoPrincipal()) {
                enderecoDAO.desmarcarEnderecosPrincipais(usuarioId);
            }
            return enderecoDAO.inserirEndereco(usuarioId, endereco);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir novo endereço", e);
        }
    }

    public EnderecoCepDTO inserirEnderecoCompleto(int usuarioId, EnderecoCepDTO dto) {
        try {
            Cep cep = dto.getCep();
            Endereco endereco = dto.getEndereco();

            cepService.salvarOuAtualizar(cep);
            
            Endereco enderecoSalvo = inserirEndereco(usuarioId, endereco);

            dto.setEndereco(enderecoSalvo);
            return dto;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar a criação do endereço completo.", e);
        }
    }

    public void atualizarEnderecoComCep(int usuarioId, int enderecoId, EnderecoCepDTO dto) {
        try {
            Endereco enderecoAtualizado = dto.getEndereco();
            Cep cepAtualizado = dto.getCep();

            enderecoAtualizado.setId(enderecoId);
            enderecoAtualizado.setUsuarioId(usuarioId);

            cepService.salvarOuAtualizar(cepAtualizado);

            if (enderecoAtualizado.isEnderecoPrincipal()) {
                enderecoDAO.desmarcarEnderecosPrincipais(usuarioId);
            }

            enderecoDAO.atualizarEndereco(enderecoAtualizado);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar o endereço no banco de dados.", e);
        }
    }

    public void deletarEnderecoComCep(int enderecoId) {
        try {
            String cep = enderecoDAO.buscarCepDoEndereco(enderecoId);
            enderecoDAO.deletarEndereco(enderecoId);

            if (cep != null && !enderecoDAO.cepEmUso(cep)) {
                cepService.excluirCep(cep);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar endereço", e);
        }
    }



    public EnderecoCepDTO buscarEnderecoCompletoPorId(int enderecoId) {
        try {
            return enderecoDAO.buscarEnderecoCompletoPorId(enderecoId);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar endereço completo", e);
        }
    }

    public List<EnderecoCepDTO> buscarEnderecosPorUsuario(int usuarioId) {
        try {
            return enderecoDAO.buscarEnderecosPorUsuario(usuarioId);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar endereços do usuário", e);
        }
    }

    public List<EnderecoCepDTO> buscarEnderecoPorUsuarioEcep(int usuarioId, String cep) {
        try {
            return enderecoDAO.buscarEnderecoPorUsuarioEcep(usuarioId, cep);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar endereço por usuário e CEP", e);
        }
    }

    public EnderecoCepDTO buscarEnderecoPrincipal(int usuarioId) {
        try {
            return enderecoDAO.buscarEnderecoPrincipal(usuarioId);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar endereço principal", e);
        }
    }
}