package br.ufrn.imd.marketplace.controller;

import br.ufrn.imd.marketplace.dto.LoginRequest;
import br.ufrn.imd.marketplace.dto.RedefinirSenhaRequest;
import br.ufrn.imd.marketplace.model.Usuario;
import br.ufrn.imd.marketplace.service.AuthService;
import br.ufrn.imd.marketplace.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private AuthService authService;
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario novoUsuario = usuarioService.cadastrarUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            if (e.getMessage().contains("CPF")) {
                errorResponse.put("field", "cpf");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
            } else if (e.getMessage().toLowerCase().contains("email")) {
                errorResponse.put("field", "email");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
            } else if (e.getMessage().toLowerCase().contains("telefone")) {
                errorResponse.put("field", "telefone");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
        }
    }

    // Endpoint de Login
    @PostMapping("/login")
    public ResponseEntity<?> autenticarUsuario(@RequestBody LoginRequest loginRequest) {
        try {
            String token = authService.autenticar(loginRequest.getEmail(), loginRequest.getSenha());
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    // Endpoint para verificar se CPF já existe
    @GetMapping("/verificar-cpf/{cpf}")
    public ResponseEntity<?> verificarCpf(@PathVariable String cpf) {
        try {
            boolean existe = usuarioService.existePorCpf(cpf.replaceAll("\\D", ""));
            Map<String, Object> response = new HashMap<>();
            response.put("exists", existe);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Erro ao verificar CPF");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Endpoint para verificar se email já existe
    @GetMapping("/verificar-email")
    public ResponseEntity<?> verificarEmail(@RequestParam String email) {
        try {
            boolean existe = usuarioService.existePorEmail(email);
            Map<String, Object> response = new HashMap<>();
            response.put("exists", existe);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Erro ao verificar email");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Endpoint para verificar se telefone já existe
    @GetMapping("/verificar-telefone")
    public ResponseEntity<?> verificarTelefone(@RequestParam String telefone) {
        try {
            String telefoneFormatado = telefone.replaceAll("\\D", "");
            boolean existe = usuarioService.existePorTelefone(telefoneFormatado);
            Map<String, Object> response = new HashMap<>();
            response.put("exists", existe);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Erro ao verificar telefone");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUsuarioById(@PathVariable int id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping
    public ResponseEntity<?> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarUsuario(@PathVariable int id, @RequestBody Usuario usuarioAtualizado) {
        Usuario atualizado = usuarioService.atualizarUsuario(id, usuarioAtualizado);
        return ResponseEntity.ok(atualizado);
    }

    @PostMapping("/redefinir-senha")
public ResponseEntity<?> redefinirSenha(@RequestBody RedefinirSenhaRequest request) {
    try {
        usuarioService.redefinirSenha(request.getEmail(), request.getCpf(), request.getNovaSenha());
        return ResponseEntity.ok().body("Senha redefinida com sucesso!");
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarUsuario(@PathVariable int id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
