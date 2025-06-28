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
            compradorService.inserirComprador(usuarioId);
            return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> listarCompradores() {
            List<Comprador> compradores = compradorService.listarCompradores();
            return ResponseEntity.ok(compradores);
    }

    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<?> deletarComprador(@PathVariable int usuarioId) {
            compradorService.deletarComprador(usuarioId);
            return ResponseEntity.noContent().build();
    }
}


