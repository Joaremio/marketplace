package br.ufrn.imd.marketplace.controller;


import br.ufrn.imd.marketplace.dao.AdministradorDAO;
import br.ufrn.imd.marketplace.dao.UsuarioDAO;
import br.ufrn.imd.marketplace.model.Administrador;
import br.ufrn.imd.marketplace.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/administradores")
public class AdministradorController {

    @Autowired
    private AdministradorDAO administradorDAO;
    @Autowired
    private UsuarioDAO usuarioDAO;


    @PostMapping("/{usuarioId}")
    public ResponseEntity<?> inserirAdministrador(@PathVariable int usuarioId) {
        Usuario usuario = usuarioDAO.buscarUsuarioById(usuarioId);
        if(usuario == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        administradorDAO.inserirADM(usuarioId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> listarAdministradores() {
        try {
            List<Administrador> adms = administradorDAO.getADMS();
            return ResponseEntity.ok(adms);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar administradores: " + e.getMessage());
        }
    }

    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<?> deletarAdministrador(@PathVariable int usuarioId) {
        try {
            administradorDAO.removerADM(usuarioId);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //http://localhost:8080/administradores/analisarVendedor/4/11/Aprovado
    @PutMapping("/analisarVendedor/{usuarioId}/{adminId}/{status}")
    public ResponseEntity<?> analisarVendedor(@PathVariable int usuarioId, @PathVariable int adminId, @PathVariable String status) {
        try {
            administradorDAO.atualizarAnaliseVendedor(usuarioId, adminId, status);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
