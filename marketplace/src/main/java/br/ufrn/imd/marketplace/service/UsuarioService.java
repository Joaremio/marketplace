package br.ufrn.imd.marketplace.service;

import br.ufrn.imd.marketplace.dao.UsuarioDAO;
import br.ufrn.imd.marketplace.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // 1. IMPORTAR
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario cadastrarUsuario(Usuario usuario) {
        try {
            // Verificar se CPF já existe
            if (usuarioDAO.existeCpf(usuario.getCpf())) {
                throw new RuntimeException("CPF já está cadastrado no sistema");
            }
            
            // Verificar se email já existe
            if (usuarioDAO.existeEmail(usuario.getEmail())) {
                throw new RuntimeException("Email já está cadastrado no sistema");
            }
            
            // Verificar se telefone já existe
            if (usuarioDAO.existeTelefone(usuario.getTelefone())) {
                throw new RuntimeException("Telefone já está cadastrado no sistema");
            }
            
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
            
            usuario.setDataCadastro(LocalDate.now());
            Usuario usuarioSalvo = usuarioDAO.inserirUsuario(usuario);
            compradorService.inserirComprador(usuarioSalvo.getId());
            return usuarioSalvo;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao cadastrar usuário: " + e.getMessage(), e);
        }
    }

    public boolean existePorCpf(String cpf) {
        try {
            return usuarioDAO.existeCpf(cpf);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar CPF: " + e.getMessage(), e);
        }
    }

    public boolean existePorEmail(String email) {
        try {
            return usuarioDAO.existeEmail(email);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar email: " + e.getMessage(), e);
        }
    }

    public boolean existePorTelefone(String telefone) {
        try {
            return usuarioDAO.existeTelefone(telefone);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar telefone: " + e.getMessage(), e);
        }
    }

    public Usuario buscarPorCpf(String cpf) {
        try {
            Usuario usuario = usuarioDAO.buscarUsuarioPorCpf(cpf);
            if (usuario == null) {
                throw new RuntimeException("Usuário com CPF " + cpf + " não encontrado.");
            }
            return usuario;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por CPF", e);
        }
    }

    public Usuario buscarPorEmail(String email) {
        try {
            Usuario usuario = usuarioDAO.buscarUsuarioPorEmail(email);
            if (usuario == null) {
                return null; 
            }
            return usuario;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por email", e);
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
            Usuario usuarioExistente = usuarioDAO.buscarUsuarioById(id);
            if (usuarioExistente == null) {
                throw new RuntimeException("Usuário com ID " + id + " não encontrado.");
            }

            if (!usuarioExistente.getCpf().equals(usuarioAtualizado.getCpf())) {
                if (usuarioDAO.existeCpf(usuarioAtualizado.getCpf())) {
                    throw new RuntimeException("CPF já está cadastrado no sistema");
                }
            }

            if (!usuarioExistente.getEmail().equals(usuarioAtualizado.getEmail())) {
                if (usuarioDAO.existeEmail(usuarioAtualizado.getEmail())) {
                    throw new RuntimeException("Email já está cadastrado no sistema");
                }
            }

            if (!usuarioExistente.getTelefone().equals(usuarioAtualizado.getTelefone())) {
                if (usuarioDAO.existeTelefone(usuarioAtualizado.getTelefone())) {
                    throw new RuntimeException("Telefone já está cadastrado no sistema");
                }
            }
            
            // Lógica para atualizar a senha, se necessário
            if (usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().isEmpty()) {
                usuarioAtualizado.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));
            } else {
                // Mantém a senha antiga se nenhuma nova for fornecida
                usuarioAtualizado.setSenha(usuarioExistente.getSenha());
            }

            boolean atualizado = usuarioDAO.atualizarUsuario(id, usuarioAtualizado);
            if (!atualizado) {
                throw new RuntimeException("Erro ao atualizar usuário.");
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