package br.ufrn.imd.marketplace.dao;

import br.ufrn.imd.marketplace.config.DB_Connection;
import br.ufrn.imd.marketplace.model.Administrador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AdministradorDAO {

    @Autowired
    private DB_Connection dbConnection;

    public void inserirADM(int usuarioId) throws SQLException {
        String sql =  "INSERT INTO administrador(usuario_id) VALUES(?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

             stmt.setInt(1, usuarioId);
             stmt.executeUpdate();
        }
    }

    public List<Administrador> getADMS() throws SQLException {
        List<Administrador> adms = new ArrayList<>();
        String sql = """
        SELECT u.id, u.nome, u.cpf, u.email, u.senha, u.telefone, u.data_cadastro
        FROM administrador a
        JOIN usuario u ON u.id = a.usuario_id
        """;

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                adms.add(new Administrador(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getString("telefone"),
                        rs.getObject("data_cadastro", LocalDate.class)
                ));
            }
            return adms;
        }
    }

    public void removerADM(int usuarioId) throws SQLException {
        String sql = "DELETE FROM administrador WHERE usuario_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             stmt.setInt(1, usuarioId);
             stmt.executeUpdate();
        }
    }

    public void atualizarAnaliseVendedor(int usuarioId, int adminId, String novoStatus) throws SQLException {
        String sql = """
        UPDATE vendedor
        SET status = ?, administrador_id = ?, data_analise = ?
        WHERE usuario_id = ?
    """;

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, novoStatus);
            stmt.setInt(2, adminId);
            stmt.setObject(3, LocalDate.now());
            stmt.setInt(4, usuarioId);
            stmt.executeUpdate();
        }
    }

    public boolean ehAdministrador(int usuarioId) throws SQLException {
        String sql = "SELECT 1 FROM administrador WHERE usuario_id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // já retorna diretamente
        }
    }

}
