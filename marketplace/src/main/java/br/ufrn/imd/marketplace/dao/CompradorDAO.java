package br.ufrn.imd.marketplace.dao;

import br.ufrn.imd.marketplace.config.DB_Connection;
import br.ufrn.imd.marketplace.model.Comprador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CompradorDAO {

    @Autowired
    private DB_Connection dbConnection;

    public void inserirComprador(int usuarioId) {
        String sql = "INSERT INTO comprador (usuario_id) VALUES (?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir comprador no banco de dados", e);
        }
    }

    public List<Comprador> getCompradores() {
        List<Comprador> compradores = new ArrayList<>();
        String sql = """
            SELECT u.id, u.nome, u.cpf, u.email, u.senha, u.telefone, u.data_cadastro
            FROM comprador c
            JOIN usuario u ON u.id = c.usuario_id
        """;

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                compradores.add(new Comprador(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getString("telefone"),
                        rs.getObject("data_cadastro", LocalDate.class)
                ));
            }

            return compradores;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar compradores no banco de dados.", e);
        }
    }

    public void removerComprador(int usuarioId) {
        String sql = "DELETE FROM comprador WHERE usuario_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover comprador no banco de dados", e);
        }
    }
}


