package br.ufrn.imd.marketplace.controller;


import br.ufrn.imd.marketplace.dao.UsuarioDAO;
import br.ufrn.imd.marketplace.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioDAO usuarioDAO;


    @PostMapping
    public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario usuario){
        try {
            usuarioDAO.InserirUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }
}
