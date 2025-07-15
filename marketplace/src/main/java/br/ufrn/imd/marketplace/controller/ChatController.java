package br.ufrn.imd.marketplace.controller;

import br.ufrn.imd.marketplace.dto.ChatDTO;
import br.ufrn.imd.marketplace.model.Chat;
import br.ufrn.imd.marketplace.model.Mensagem;
import br.ufrn.imd.marketplace.service.ChatService;
import br.ufrn.imd.marketplace.service.MensagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


class IniciarChatRequest {
    public int compradorId;
    public int vendedorId;
}

@RestController
@RequestMapping("/api")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private MensagemService mensagemService;

    @PostMapping("/chats/iniciar")
    public ResponseEntity<Chat> iniciarChat(@RequestBody IniciarChatRequest request) {
        Chat chat = chatService.obterOuCriarChat(request.compradorId, request.vendedorId);
        return ResponseEntity.ok(chat);
    }
    @GetMapping("/mensagens/chat/{chatId}")
    public ResponseEntity<List<Mensagem>> getMensagensDoChat(@PathVariable int chatId) {
        List<Mensagem> mensagens = mensagemService.buscarMensagensPorChatId(chatId);
        return ResponseEntity.ok(mensagens);
    }
    @PostMapping("/mensagens")
    public ResponseEntity<Mensagem> enviarMensagem(@RequestBody Mensagem mensagem) {
        Mensagem mensagemSalva = mensagemService.salvarMensagem(mensagem);
        return ResponseEntity.ok(mensagemSalva);
    }

    @GetMapping("/chats/usuario/{usuarioId}")
    public ResponseEntity<List<ChatDTO>> getChatsDoUsuario(@PathVariable int usuarioId) {
        List<ChatDTO> chats = chatService.buscarChatsPorUsuario(usuarioId);
        return ResponseEntity.ok(chats);
    }

}