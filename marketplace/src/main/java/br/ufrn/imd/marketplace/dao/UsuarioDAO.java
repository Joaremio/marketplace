package br.ufrn.imd.marketplace.dao;

import br.ufrn.imd.marketplace.config.DB_Connection;
import br.ufrn.imd.marketplace.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UsuarioDAO {
    @Autowired
    private DB_Connection dbConnection;

    public Usuario inserirUsuario(Connection conn, Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuario (nome, cpf, email, senha, data_cadastro, telefone) VALUES (?,?,?,?,?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCpf());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getSenha());
            stmt.setObject(5, usuario.getDataCadastro());
            stmt.setString(6, usuario.getTelefone());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Falha ao inserir usuário, nenhuma linha afetada.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    usuario.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Falha ao obter o ID do usuário inserido.");
                }
            }

            return usuario;
        }
    }

    // Método para verificar se CPF já existe
    public boolean existeCpf(Connection conn, String cpf) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE cpf = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cpf);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        }
    }

    // Método para verificar se email já existe
    public boolean existeEmail(Connection conn, String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE email = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        }
    }

    // Método para verificar se telefone já existe
    public boolean existeTelefone(Connection conn ,String telefone) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE telefone = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, telefone);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        }
    }

    // Método para buscar usuário por CPF
    public Usuario buscarUsuarioPorCpf(String cpf) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE cpf = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("cpf"),
                            rs.getString("email"),
                            rs.getString("senha"),
                            rs.getString("telefone"),
                            rs.getObject("data_cadastro", LocalDate.class)
                    );
                } else {
                    return null;
                }
            }
        }
    }

    // Método para buscar usuário por email
    public Usuario buscarUsuarioPorEmail(String email) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE email = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("cpf"),
                            rs.getString("email"),
                            rs.getString("senha"),
                            rs.getString("telefone"),
                            rs.getObject("data_cadastro", LocalDate.class)
                    );
                } else {
                    return null;
                }
            }
        }
    }

    public Usuario buscarUsuarioById(int id) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("cpf"),
                            rs.getString("email"),
                            rs.getString("senha"),
                            rs.getString("telefone"),
                            rs.getObject("data_cadastro", LocalDate.class)
                    );
                } else {
                    return null;
                }
            }
        }
    }

    public List<Usuario> buscarUsuarios() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuario";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usuarios.add(new Usuario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getString("telefone"),
                        rs.getObject("data_cadastro", LocalDate.class)
                ));
            }

            return usuarios;
        }
    }

     public void atualizarSenha(int usuarioId, String novaSenhaCriptografada) throws SQLException {
        String sql = "UPDATE usuario SET senha = ? WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, novaSenhaCriptografada);
            stmt.setInt(2, usuarioId);
            stmt.executeUpdate();
        }
    }

public boolean atualizarUsuario(Connection conn, int id, Usuario usuarioAtualizado) throws SQLException {
    String sql = "UPDATE usuario SET nome = ?, cpf = ?, email = ?, senha = ?, telefone = ? WHERE id = ?";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, usuarioAtualizado.getNome());
        stmt.setString(2, usuarioAtualizado.getCpf());
        stmt.setString(3, usuarioAtualizado.getEmail());
        stmt.setString(4, usuarioAtualizado.getSenha());
        stmt.setString(5, usuarioAtualizado.getTelefone());
        stmt.setInt(6, id);

        return stmt.executeUpdate() > 0;
    }
}

public boolean atualizarUsuario(int id, Usuario usuarioAtualizado) throws SQLException {
    try (Connection conn = dbConnection.getConnection()) {
        return atualizarUsuario(conn, id, usuarioAtualizado);
    }
}

    public boolean deletarUsuario(int id) throws SQLException {
        String sql = "DELETE FROM usuario WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}