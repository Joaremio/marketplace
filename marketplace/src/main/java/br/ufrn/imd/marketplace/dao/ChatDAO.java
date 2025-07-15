package br.ufrn.imd.marketplace.dao;

import br.ufrn.imd.marketplace.config.DB_Connection;
import br.ufrn.imd.marketplace.dto.ChatDTO;
import br.ufrn.imd.marketplace.model.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    public List<ChatDTO> buscarChatsPorUsuarioId(int usuarioId) throws SQLException {
        List<ChatDTO> chats = new ArrayList<>();
        String sql = """
            SELECT c.id, c.comprador_id, c.vendedor_id, c.data_criacao,
                   u_comprador.nome AS nome_comprador,
                   u_vendedor.nome AS nome_vendedor
            FROM chat c
            JOIN usuario u_comprador ON c.comprador_id = u_comprador.id
            JOIN usuario u_vendedor ON c.vendedor_id = u_vendedor.id
            WHERE c.comprador_id = ? OR c.vendedor_id = ?
            ORDER BY c.id DESC
        """;
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setInt(2, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ChatDTO chatDto = new ChatDTO();
                    chatDto.setId(rs.getInt("id"));
                    chatDto.setCompradorId(rs.getInt("comprador_id"));
                    chatDto.setVendedorId(rs.getInt("vendedor_id"));
                    chatDto.setDataCriacao(rs.getObject("data_criacao", LocalDate.class));
                    chatDto.setNomeComprador(rs.getString("nome_comprador"));
                    chatDto.setNomeVendedor(rs.getString("nome_vendedor"));
                    chats.add(chatDto);
                }
            }
        }
        return chats;
    }
}