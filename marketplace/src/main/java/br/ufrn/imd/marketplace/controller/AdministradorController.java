package br.ufrn.imd.marketplace.controller;


import br.ufrn.imd.marketplace.dao.AdministradorDAO;
import br.ufrn.imd.marketplace.dao.UsuarioDAO;
import br.ufrn.imd.marketplace.model.Administrador;
import br.ufrn.imd.marketplace.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
