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

    public void inserirComprador(Connection conn, int usuarioId) throws SQLException {
        String sql = "INSERT INTO comprador (usuario_id) VALUES (?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            stmt.executeUpdate();
        }
    }

    public List<Comprador> getCompradores() throws SQLException {
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
        }
    }

    public void removerComprador(int usuarioId) throws SQLException {
        String sql = "DELETE FROM comprador WHERE usuario_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            stmt.executeUpdate();
        }
    }
}


