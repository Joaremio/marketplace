package br.ufrn.imd.marketplace.dao;

import br.ufrn.imd.marketplace.config.DB_Connection;
import br.ufrn.imd.marketplace.model.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.time.LocalDate;

@Repository
public class ChatDAO {

    @Autowired
    private DB_Connection dbConnection;

    public Chat buscarChatPorParticipantes(int id1, int id2) throws SQLException {
        String sql = "SELECT * FROM chat WHERE (comprador_id = ? AND vendedor_id = ?) OR (comprador_id = ? AND vendedor_id = ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id1);
            stmt.setInt(2, id2);
            stmt.setInt(3, id2);
            stmt.setInt(4, id1);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Chat chat = new Chat();
                    chat.setId(rs.getInt("id"));
                    chat.setCompradorId(rs.getInt("comprador_id"));
                    chat.setVendedorId(rs.getInt("vendedor_id"));
                    chat.setDataCriacao(rs.getObject("data_criacao", LocalDate.class));
                    return chat;
                }
            }
        }
        return null;
    }

    public Chat criarChat(Chat chat) throws SQLException {
        String sql = "INSERT INTO chat (comprador_id, vendedor_id, data_criacao) VALUES (?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, chat.getCompradorId());
            stmt.setInt(2, chat.getVendedorId());
            stmt.setObject(3, chat.getDataCriacao());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) chat.setId(rs.getInt(1));
            }
        }
        return chat;
    }
}