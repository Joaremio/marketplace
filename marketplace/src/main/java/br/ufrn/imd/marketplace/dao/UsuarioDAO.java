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

    public Usuario inserirUsuario(Usuario usuario) {

        String sql = "INSERT INTO usuario (nome, cpf, email, senha, data_cadastro, telefone) VALUES (?,?,?,?,?,?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

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

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir usuário no banco de dados.", e);
        }
    }
    public Usuario buscarUsuarioById(int id) {

        String sql = "SELECT * FROM usuario WHERE id = ? ";

        try(Connection conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){

            stmt.setInt(1,id);

            if(rs.next()){
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setCpf(rs.getString("cpf"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setDataCadastro(rs.getObject("data_cadastro", LocalDate.class));
                usuario.setTelefone(rs.getString("telefone"));
                return usuario;
            }else{
                throw new RuntimeException("Usuário com ID " + id + " não encontrado.");
            }
        }catch(SQLException e){
            throw new RuntimeException("Erro ao buscar usuário no banco de dados.", e);
        }
  }
    public List<Usuario> buscarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuario";

        try(Connection conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setCpf(rs.getString("cpf"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setDataCadastro(rs.getObject("data_cadastro", LocalDate.class));
                usuario.setTelefone(rs.getString("telefone"));
                usuarios.add(usuario);
            }

        return usuarios;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuários no banco de dados.", e);
        }
  }
    public Usuario atualizarUsuario(int id , Usuario usuarioAtualizado){
        try(Connection conn = dbConnection.getConnection()){
            String sql = "UPDATE usuario SET nome = ?, cpf = ?, email = ?, senha = ?, telefone = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1,usuarioAtualizado.getNome());
            stmt.setString(2,usuarioAtualizado.getCpf());
            stmt.setString(3,usuarioAtualizado.getEmail());
            stmt.setString(4,usuarioAtualizado.getSenha());
            stmt.setString(5,usuarioAtualizado.getTelefone());
            stmt.setInt(6, id);

            int linhas = stmt.executeUpdate();

            if(linhas == 0){
                throw new RuntimeException("Usuário com ID " + id + " não encontrado.");
            }

            usuarioAtualizado.setId(id);
            return usuarioAtualizado;

        }catch(SQLException e){
            throw new RuntimeException("Erro ao atualizar usuário.", e);
        }
    }

    public void deletarUsuario(int id){
        try(Connection conn = dbConnection.getConnection()){
            String sql = "DELETE FROM usuario WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1,id);

            int linhas = stmt.executeUpdate();

            if (linhas == 0) {
                throw new RuntimeException("Usuário com ID " + id + " não encontrado.");
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
}
