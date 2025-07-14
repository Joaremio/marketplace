package br.ufrn.imd.marketplace.service;

import br.ufrn.imd.marketplace.dao.MensagemDAO;
import br.ufrn.imd.marketplace.model.Mensagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;

@Service
public class MensagemService {

    @Autowired
    private MensagemDAO mensagemDAO;

    public Mensagem salvarMensagem(Mensagem mensagem) {
        try {
            return mensagemDAO.salvarMensagem(mensagem);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar mensagem.", e);
        }
    }

    public List<Mensagem> buscarMensagensPorChatId(int chatId) {
        try {
            return mensagemDAO.buscarMensagensPorChatId(chatId);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar histórico de mensagens.", e);
        }
    }
}