package br.ufrn.imd.marketplace.controller;

import br.ufrn.imd.marketplace.model.Comprador;
import br.ufrn.imd.marketplace.service.CompradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/compradores")
public class CompradorController {

    @Autowired
    private CompradorService compradorService;

    @PostMapping("/{usuarioId}")
    public ResponseEntity<?> inserirComprador(@PathVariable int usuarioId) {
        try {
            compradorService.inserirComprador(usuarioId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("não encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listarCompradores() {
        try {
            List<Comprador> compradores = compradorService.listarCompradores();
            return ResponseEntity.ok(compradores);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar compradores: " + e.getMessage());
        }
    }

    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<?> deletarComprador(@PathVariable int usuarioId) {
        try {
            compradorService.deletarComprador(usuarioId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao deletar comprador: " + e.getMessage());
        }
    }
}


