package br.ufrn.imd.marketplace.dao;

import br.ufrn.imd.marketplace.config.DB_Connection;
import br.ufrn.imd.marketplace.model.Vendedor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class VendedorDAO {

    @Autowired
    private DB_Connection dbConnection;

    public void inserirVendedor(int usuarioId) throws SQLException {
        String sql = """
            INSERT INTO vendedor (usuario_id, data_analise)
            VALUES (?, ?)
        """;

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setObject(2, LocalDate.now());
            stmt.executeUpdate();
        }
    }

    public List<Vendedor> getVendedores() throws SQLException {
        List<Vendedor> vendedores = new ArrayList<>();
        String sql = """
            SELECT u.id, u.nome, u.cpf, u.email, u.senha, u.telefone, u.data_cadastro,
                   v.status, v.data_analise
            FROM vendedor v
            JOIN usuario u ON u.id = v.usuario_id
        """;

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Vendedor vendedor = new Vendedor(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getString("telefone"),
                        rs.getObject("data_cadastro", LocalDate.class),
                        rs.getString("status"),
                        rs.getObject("data_analise", LocalDate.class)
                );
                vendedores.add(vendedor);
            }
            return vendedores;
        }
    }

    public Vendedor getVendedorById(int id) throws SQLException {
        String sql = """
        SELECT u.id, u.nome, u.cpf, u.email, u.senha, u.telefone, u.data_cadastro,
               v.status, v.data_analise
        FROM vendedor v
        JOIN usuario u ON u.id = v.usuario_id
        WHERE u.id = ?
    """;

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Vendedor(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("cpf"),
                            rs.getString("email"),
                            rs.getString("senha"),
                            rs.getString("telefone"),
                            rs.getObject("data_cadastro", LocalDate.class),
                            rs.getString("status"),
                            rs.getObject("data_analise", LocalDate.class)
                    );
                } else {
                    return null; // ou lance exceção se preferir
                }
            }
        }
    }

    public void excluirVendedor(int id) throws SQLException {
        String sql = "DELETE FROM vendedor WHERE usuario_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException("Vendedor com ID " + id + " não encontrado para exclusão.");
            }
        }
    }

    public String getVendedorStatus(int vendedorId) throws SQLException{
        String status = null;
        String sql = "SELECT status FROM vendedor WHERE usuario_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vendedorId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    status = rs.getString("status");
                }
            }
        }
        return status;
    }

    public List<Vendedor> getVendedoresPorStatus(String status) throws SQLException {
    List<Vendedor> vendedores = new ArrayList<>();
    String sql = """
        SELECT u.id, u.nome, u.cpf, u.email, u.data_cadastro, v.status, v.data_analise, u.telefone
        FROM vendedor v
        JOIN usuario u ON u.id = v.usuario_id
        WHERE v.status = ?
    """;

    try (Connection conn = dbConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, status);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Vendedor vendedor = new Vendedor(
                    rs.getInt("id"), rs.getString("nome"), rs.getString("cpf"),
                    rs.getString("email"), null, /* senha não é necessária aqui */
                    rs.getString("telefone"), rs.getObject("data_cadastro", LocalDate.class),
                    rs.getString("status"), rs.getObject("data_analise", LocalDate.class)
            );
            vendedores.add(vendedor);
        }
        return vendedores;
    }
}

}

