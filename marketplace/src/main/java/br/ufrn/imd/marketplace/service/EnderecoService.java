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
    private CepService cepService; // Usaremos o CepService para a lógica de salvar/atualizar

    /**
     * Insere um novo endereço para um usuário, garantindo a lógica de endereço principal.
     */
    public Endereco inserirEndereco(int usuarioId, Endereco endereco) {
        try {
            // Se este novo endereço for principal, desmarca qualquer outro que já seja.
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

            // 1. Garante que o CEP exista (cria ou atualiza)
            cepService.salvarOuAtualizar(cep);
            
            // 2. Insere o endereço
            // A sua lógica de inserirEndereco já trata a questão do endereço principal
            Endereco enderecoSalvo = inserirEndereco(usuarioId, endereco);

            // 3. Monta e retorna o DTO completo com os IDs gerados
            dto.setEndereco(enderecoSalvo);
            return dto;

        } catch (Exception e) {
            // Re-lança como uma exceção de runtime para o controller capturar
            throw new RuntimeException("Erro ao processar a criação do endereço completo.", e);
        }
    }

    /**
     * Atualiza um endereço existente, incluindo suas informações de CEP.
     */
    public void atualizarEnderecoComCep(int usuarioId, int enderecoId, EnderecoCepDTO dto) {
        try {
            Endereco enderecoAtualizado = dto.getEndereco();
            Cep cepAtualizado = dto.getCep();

            // Garante que os IDs estão corretos no objeto de endereço
            enderecoAtualizado.setId(enderecoId);
            enderecoAtualizado.setUsuarioId(usuarioId);

            // 1. Usa o CepService para salvar ou atualizar as informações do CEP na tabela 'cep'.
            // Esta é a correção principal que garante a atualização de logradouro, bairro, etc.
            cepService.salvarOuAtualizar(cepAtualizado);

            // 2. Se este endereço está sendo marcado como principal, desmarca os outros.
            if (enderecoAtualizado.isEnderecoPrincipal()) {
                enderecoDAO.desmarcarEnderecosPrincipais(usuarioId);
            }

            // 3. Atualiza as informações na tabela 'endereco' (número, complemento, CEP_cep, etc.).
            enderecoDAO.atualizarEndereco(enderecoAtualizado);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar o endereço no banco de dados.", e);
        }
    }

    /**
     * Deleta um endereço e, se o CEP associado não estiver mais em uso, deleta o CEP também.
     */
    public void deletarEnderecoComCep(int enderecoId) {
        try {
            String cep = enderecoDAO.buscarCepDoEndereco(enderecoId);
            enderecoDAO.deletarEndereco(enderecoId);

            if (cep != null && !enderecoDAO.cepEmUso(cep)) {
                // Aqui usamos o CepService, não o CepDAO diretamente.
                cepService.excluirCep(cep);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar endereço", e);
        }
    }


    // --- MÉTODOS DE BUSCA (sem alterações, apenas repassando a chamada para o DAO) ---

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