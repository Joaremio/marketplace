package br.ufrn.imd.marketplace.service;

import br.ufrn.imd.marketplace.config.DB_Connection;
import br.ufrn.imd.marketplace.dao.CarrinhoDAO;
import br.ufrn.imd.marketplace.dao.CompradorDAO;
import br.ufrn.imd.marketplace.dao.UsuarioDAO;
import br.ufrn.imd.marketplace.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // 1. IMPORTAR
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private CarrinhoDAO carrinhoDAO;

    @Autowired
    private CompradorDAO compradorDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DB_Connection dbConnection;

    public Usuario cadastrarUsuario(Usuario usuario) {
        Connection conn = null;
        try {
            conn = dbConnection.getConnection();
            conn.setAutoCommit(false);

            if (usuarioDAO.existeCpf(conn, usuario.getCpf())) {
                throw new RuntimeException("CPF já está cadastrado no sistema");
            }
            if (usuarioDAO.existeEmail(conn, usuario.getEmail())) {
                throw new RuntimeException("Email já está cadastrado no sistema");
            }
            if (usuarioDAO.existeTelefone(conn, usuario.getTelefone())) {
                throw new RuntimeException("Telefone já está cadastrado no sistema");
            }

            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
            usuario.setDataCadastro(LocalDate.now());

            Usuario usuarioSalvo = usuarioDAO.inserirUsuario(conn, usuario);
            compradorDAO.inserirComprador(conn, usuarioSalvo.getId());
            carrinhoDAO.criarCarrinho(conn, usuarioSalvo.getId());

            conn.commit();
            return usuarioSalvo;

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            throw new RuntimeException("Erro ao cadastrar usuário: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


    public boolean existePorCpf(String cpf) {
        try {
            Connection conn = dbConnection.getConnection();
            return usuarioDAO.existeCpf(conn, cpf);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar CPF: " + e.getMessage(), e);
        }
    }

    public boolean existePorEmail(String email) {
        try {
            Connection conn = dbConnection.getConnection();
            return usuarioDAO.existeEmail(conn,email);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar email: " + e.getMessage(), e);
        }
    }

    public boolean existePorTelefone(String telefone) {
        try {
            Connection conn = dbConnection.getConnection();
            return usuarioDAO.existeTelefone(conn,telefone);
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
    Connection conn = null;
    try {
        conn = dbConnection.getConnection();
        conn.setAutoCommit(false);

        Usuario usuarioExistente = usuarioDAO.buscarUsuarioById(id);
        if (usuarioExistente == null) {
            throw new RuntimeException("Usuário com ID " + id + " não encontrado.");
        }

        if (!usuarioExistente.getCpf().equals(usuarioAtualizado.getCpf())) {
            if (usuarioDAO.existeCpf(conn, usuarioAtualizado.getCpf())) {
                throw new RuntimeException("CPF já está cadastrado no sistema");
            }
        }

        if (!usuarioExistente.getEmail().equals(usuarioAtualizado.getEmail())) {
            if (usuarioDAO.existeEmail(conn, usuarioAtualizado.getEmail())) {
                throw new RuntimeException("Email já está cadastrado no sistema");
            }
        }

        if (!usuarioExistente.getTelefone().equals(usuarioAtualizado.getTelefone())) {
            if (usuarioDAO.existeTelefone(conn, usuarioAtualizado.getTelefone())) {
                throw new RuntimeException("Telefone já está cadastrado no sistema");
            }
        }
        
        if (usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().isEmpty()) {
            usuarioAtualizado.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));
        } else {
            usuarioAtualizado.setSenha(usuarioExistente.getSenha());
        }

        usuarioAtualizado.setDataCadastro(usuarioExistente.getDataCadastro());
        boolean atualizado = usuarioDAO.atualizarUsuario(conn, id, usuarioAtualizado);
        if (!atualizado) {
            throw new RuntimeException("Erro ao atualizar usuário.");
        }

        conn.commit();
        
        usuarioAtualizado.setId(id);
        return usuarioAtualizado;

    } catch (Exception e) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }
        throw new RuntimeException("Erro ao atualizar usuário: " + e.getMessage(), e);
    } finally {
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}

    public void redefinirSenha(String email, String cpf, String novaSenha) {
    try {
        Usuario usuario = usuarioDAO.buscarUsuarioPorEmail(email);

        if (usuario == null || !usuario.getCpf().equals(cpf.replaceAll("\\D", ""))) {
            throw new RuntimeException("E-mail ou CPF inválidos.");
        }

        String senhaCriptografada = passwordEncoder.encode(novaSenha);
        usuarioDAO.atualizarSenha(usuario.getId(), senhaCriptografada);

    } catch (SQLException e) {
        throw new RuntimeException("Erro de banco de dados ao redefinir a senha.", e);
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