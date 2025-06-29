package br.ufrn.imd.marketplace.service;


import br.ufrn.imd.marketplace.dao.CepDAO;
import br.ufrn.imd.marketplace.dao.EnderecoDAO;
import br.ufrn.imd.marketplace.dto.EnderecoCepDTO;
import br.ufrn.imd.marketplace.model.Cep;
import br.ufrn.imd.marketplace.model.Endereco;
import br.ufrn.imd.marketplace.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class EnderecoService {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EnderecoDAO enderecoDAO;

    @Autowired
    private CepDAO cepDAO;

    public Endereco inserirEndereco(int usuarioId, Endereco endereco) {
        try{
            if (endereco.isEnderecoPrincipal()) {
                enderecoDAO.desmarcarEnderecosPrincipais(usuarioId);
            }
            Usuario usuarioEncontrado =  usuarioService.buscarPorId(usuarioId);
            Endereco enderecoSalvo = enderecoDAO.inserirEndereco(usuarioEncontrado.getId(),endereco);
            return enderecoSalvo;
        } catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException("Erro ao inserir endereco", e);
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

    public void deletarEnderecoComCep(int enderecoId) {
        try {
            String cep = enderecoDAO.buscarCepDoEndereco(enderecoId);

            enderecoDAO.deletarEndereco(enderecoId);

            if (cep != null && !enderecoDAO.cepEmUso(cep)) {
                cepDAO.excluirCep(cep);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar endereço", e);
        }
    }

    public void atualizarEnderecoComCep(int usuarioId, int enderecoId, EnderecoCepDTO dto) {
        try {
            // 1. Buscar CEP antigo
            String cepAntigo = enderecoDAO.buscarCepDoEndereco(enderecoId);

            // 2. Se novo CEP não existir, insere
            Cep novoCep = dto.getCep();
            if (!cepDAO.cepExiste(novoCep.getCep())) {
                cepDAO.inserirCep(novoCep);
            }

            Endereco endereco = dto.getEndereco();
            endereco.setId(enderecoId);
            endereco.setUsuarioId(usuarioId);

            // 3. Garantir que só um endereço seja principal
            if (endereco.isEnderecoPrincipal()) {
                enderecoDAO.desmarcarEnderecosPrincipais(usuarioId);
            }

            // 4. Atualiza o endereço
            enderecoDAO.atualizarEndereco(endereco);

            // 5. Remove o CEP antigo se não for mais usado
            if (cepAntigo != null && !cepAntigo.equals(novoCep.getCep())) {
                if (!enderecoDAO.cepEmUso(cepAntigo)) {
                    cepDAO.excluirCep(cepAntigo);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar endereço", e);
        }
    }

}
