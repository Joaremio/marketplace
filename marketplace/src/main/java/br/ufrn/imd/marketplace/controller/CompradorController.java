package br.ufrn.imd.marketplace.controller;


import br.ufrn.imd.marketplace.dao.CompradorDAO;
import br.ufrn.imd.marketplace.dao.UsuarioDAO;
import br.ufrn.imd.marketplace.model.Administrador;
import br.ufrn.imd.marketplace.model.Comprador;
import br.ufrn.imd.marketplace.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/compradores")
public class CompradorController {

    @Autowired
    private CompradorDAO compradorDAO;
    @Autowired
    private UsuarioDAO usuarioDAO;

    @PostMapping("/{usuarioId}")
    public ResponseEntity<?> inserirComprador(@PathVariable int usuarioId) {

        Usuario usuario = usuarioDAO.buscarUsuarioById(usuarioId);

        if(usuario == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        compradorDAO.inserirComprador(usuarioId);

        return ResponseEntity.noContent().build();
    }


    //usuarios que também são compradores
    @GetMapping
    public ResponseEntity<?> listarCompradores() {
        try {
            List<Comprador> compradores = compradorDAO.getCompradores();
            return ResponseEntity.ok(compradores);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar administradores: " + e.getMessage());
        }
    }

    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<?> deletarAdministrador(@PathVariable int usuarioId) {
        try {
            compradorDAO.removerComprador(usuarioId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
