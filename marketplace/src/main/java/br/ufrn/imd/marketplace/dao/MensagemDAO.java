package br.ufrn.imd.marketplace.dao;

import br.ufrn.imd.marketplace.config.DB_Connection;
import br.ufrn.imd.marketplace.model.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MensagemDAO {

    @Autowired
    private DB_Connection dbConnection;

    public Mensagem salvarMensagem(Mensagem mensagem) throws SQLException {
        String sql = "INSERT INTO mensagem (chat_id, usuario_id, texto) VALUES (?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, mensagem.getChatId());
            stmt.setInt(2, mensagem.getUsuarioId());
            stmt.setString(3, mensagem.getTexto());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) mensagem.setId(rs.getInt(1));
            }
        }
        return mensagem;
    }

    public List<Mensagem> buscarMensagensPorChatId(int chatId) throws SQLException {
        List<Mensagem> mensagens = new ArrayList<>();
        String sql = "SELECT * FROM mensagem WHERE chat_id = ? ORDER BY data_hora ASC";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, chatId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Mensagem msg = new Mensagem();
                msg.setId(rs.getInt("id"));
                msg.setChatId(rs.getInt("chat_id"));
                msg.setUsuarioId(rs.getInt("usuario_id"));
                msg.setTexto(rs.getString("texto"));
                msg.setDataHora(rs.getTimestamp("data_hora"));
                mensagens.add(msg);
            }
        }
        return mensagens;
    }
}