package br.ufrn.imd.marketplace.service;

import br.ufrn.imd.marketplace.dao.AdministradorDAO;
import br.ufrn.imd.marketplace.dao.UsuarioDAO;
import br.ufrn.imd.marketplace.model.Administrador;
import br.ufrn.imd.marketplace.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class AdministradorService {

    @Autowired
    private AdministradorDAO administradorDAO;

    @Autowired
    private UsuarioDAO usuarioDAO;

    public void inserirAdministrador(int usuarioId) {
        try {
            Usuario usuario = usuarioDAO.buscarUsuarioById(usuarioId);
            if (usuario == null) {
                throw new RuntimeException("Usuário com id " + usuarioId + " não encontrado.");
            }
            administradorDAO.inserirADM(usuarioId);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário no banco de dados", e);
        }
    }


    public List<Administrador> listarAdministradores() {
        try{
            return administradorDAO.getADMS();
        }catch(SQLException e){
            throw new RuntimeException("Erro ao buscar administradores", e);
        }
    }

    public void deletarAdministrador(int usuarioId) {
        try{
            administradorDAO.removerADM(usuarioId);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar administrador", e);
        }

    }

    public void analisarVendedor(int usuarioId, int adminId, String status) {
        try{
            if (!status.equalsIgnoreCase("APROVADO") && !status.equalsIgnoreCase("REPROVADO")) {
                throw new IllegalArgumentException("Status inválido. Use 'APROVADO' ou 'REPROVADO'.");
            }
            administradorDAO.atualizarAnaliseVendedor(usuarioId, adminId, status.toUpperCase());
        }catch(SQLException e){
            throw new RuntimeException("Erro ao analisar vendedor", e);
        }

    }
}

