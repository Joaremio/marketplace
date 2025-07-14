package br.ufrn.imd.marketplace.service;

import br.ufrn.imd.marketplace.dao.ChatDAO;
import br.ufrn.imd.marketplace.model.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.time.LocalDate;

@Service
public class ChatService {

    @Autowired
    private ChatDAO chatDAO;

    public Chat obterOuCriarChat(int compradorId, int vendedorId) {
        try {
            Chat chatExistente = chatDAO.buscarChatPorParticipantes(compradorId, vendedorId);
            if (chatExistente != null) {
                return chatExistente;
            }
            Chat novoChat = new Chat();
            novoChat.setCompradorId(compradorId);
            novoChat.setVendedorId(vendedorId);
            novoChat.setDataCriacao(LocalDate.now());
            return chatDAO.criarChat(novoChat);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao obter ou criar chat.", e);
        }
    }
}