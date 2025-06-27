package br.ufrn.imd.marketplace.controller;

import br.ufrn.imd.marketplace.model.Administrador;
import br.ufrn.imd.marketplace.service.AdministradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/administradores")
public class AdministradorController {

    @Autowired
    private AdministradorService administradorService;

    @PostMapping("/{usuarioId}")
    public ResponseEntity<?> inserirAdministrador(@PathVariable int usuarioId) {
        try {
            administradorService.inserirAdministrador(usuarioId);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (RuntimeException e) {
            if (e.getMessage().contains("não encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listarAdministradores() {
        try {
            List<Administrador> adms = administradorService.listarAdministradores();
            return ResponseEntity.ok(adms);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar administradores: " + e.getMessage());
        }
    }

    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<?> deletarAdministrador(@PathVariable int usuarioId) {
        try {
            administradorService.deletarAdministrador(usuarioId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao deletar administrador: " + e.getMessage());
        }
    }

    @PutMapping("/analisarVendedor/{usuarioId}/{adminId}/{status}")
    public ResponseEntity<?> analisarVendedor(@PathVariable int usuarioId,
                                              @PathVariable int adminId,
                                              @PathVariable String status) {
        try {
            administradorService.analisarVendedor(usuarioId, adminId, status);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao analisar vendedor: " + e.getMessage());
        }
    }
}
