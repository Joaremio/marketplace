package br.ufrn.imd.marketplace.controller;


import br.ufrn.imd.marketplace.dao.UsuarioDAO;
import br.ufrn.imd.marketplace.dao.VendedorDAO;
import br.ufrn.imd.marketplace.model.Usuario;
import br.ufrn.imd.marketplace.model.Vendedor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/vendedores")
public class VendedorController {

    @Autowired
    private VendedorDAO vendedorDAO;

    @Autowired
    private UsuarioDAO usuarioDAO;


    @PostMapping("/{usuarioId}")
    public ResponseEntity<?> solicitarVendedor(@PathVariable int usuarioId){
        Usuario usuario =  usuarioDAO.buscarUsuarioById(usuarioId);
        if(usuario == null){
            return ResponseEntity.notFound().build();
        }
        vendedorDAO.inserirVendedor(usuarioId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> listarVendedores() {
        try{
            List<Vendedor> vendedores = vendedorDAO.getVendedores();
            return ResponseEntity.ok(vendedores);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
