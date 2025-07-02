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
        administradorService.inserirAdministrador(usuarioId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> listarAdministradores() {
        List<Administrador> adms = administradorService.listarAdministradores();
        return ResponseEntity.ok(adms);
    }

    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<?> deletarAdministrador(@PathVariable int usuarioId) {
        administradorService.deletarAdministrador(usuarioId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/analisarVendedor/{usuarioId}/{adminId}/{status}")
    public ResponseEntity<?> analisarVendedor(@PathVariable int usuarioId,
                                              @PathVariable int adminId,
                                              @PathVariable String status) {
        administradorService.analisarVendedor(usuarioId, adminId, status);
        return ResponseEntity.noContent().build();
    }


}
