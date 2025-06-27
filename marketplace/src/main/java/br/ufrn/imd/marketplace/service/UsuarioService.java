package br.ufrn.imd.marketplace.service;

import br.ufrn.imd.marketplace.dao.UsuarioDAO;
import br.ufrn.imd.marketplace.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private CompradorService compradorService;

    public Usuario cadastrarUsuario(Usuario usuario) {
        try {
            usuario.setDataCadastro(LocalDate.now());
            Usuario usuarioSalvo = usuarioDAO.inserirUsuario(usuario);
            compradorService.inserirComprador(usuarioSalvo.getId());
            return usuarioSalvo;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar usuário", e);
        }
    }

    public Usuario buscarPorId(int id) {
        try {
            Usuario usuario = usuarioDAO.buscarUsuarioById(id);
            if (usuario == null) {
                throw new RuntimeException("Usuário com ID " + id + " não encontrado.");
            }
            return usuario;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário", e);
        }
    }

    public List<Usuario> listarTodos() {
        try {
            return usuarioDAO.buscarUsuarios();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários", e);
        }
    }

    public Usuario atualizarUsuario(int id, Usuario usuarioAtualizado) {
        try {
            boolean atualizado = usuarioDAO.atualizarUsuario(id, usuarioAtualizado);
            if (!atualizado) {
                throw new RuntimeException("Usuário com ID " + id + " não encontrado.");
            }
            usuarioAtualizado.setId(id);
            return usuarioAtualizado;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuário", e);
        }
    }

    public void deletarUsuario(int id) {
        try {
            boolean deletado = usuarioDAO.deletarUsuario(id);
            if (!deletado) {
                throw new RuntimeException("Usuário com ID " + id + " não encontrado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao deletar usuário", e);
        }
    }
}
