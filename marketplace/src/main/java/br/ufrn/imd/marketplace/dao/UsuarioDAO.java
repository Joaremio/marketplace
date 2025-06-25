package br.ufrn.imd.marketplace.dao;

import br.ufrn.imd.marketplace.config.DB_Connection;
import br.ufrn.imd.marketplace.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;


@Repository
public class UsuarioDAO {
    @Autowired
    private DB_Connection dbConnection;

    public void InserirUsuario(Usuario usuario) {
        try(Connection conn = dbConnection.getConnection()){
            String sql = "INSERT INTO usuario (nome, cpf, email, senha, data_cadastro) VALUES (?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCpf());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getSenha());
            stmt.setObject(5, usuario.getDataCadastro());

            stmt.executeUpdate();

            System.out.println("✅ Usuário inserido com sucesso!");
        } catch(SQLException e){
            System.err.println("❌ Erro ao inserir usuário: " + e.getMessage());
            throw new RuntimeException("Erro ao inserir usuário no banco de dados.", e);
        }
  }

}
