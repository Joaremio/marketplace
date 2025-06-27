package br.ufrn.imd.marketplace.controller;

import br.ufrn.imd.marketplace.model.Vendedor;
import br.ufrn.imd.marketplace.service.VendedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendedores")
public class VendedorController {

    @Autowired
    private VendedorService vendedorService;

    @PostMapping("/{usuarioId}")
    public ResponseEntity<?> solicitarVendedor(@PathVariable int usuarioId){
        try {
            vendedorService.solicitarVendedor(usuarioId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("não encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listarVendedores() {
        try {
            List<Vendedor> vendedores = vendedorService.listarVendedores();
            return ResponseEntity.ok(vendedores);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar vendedores: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarVendedorPorId(@PathVariable int id){
        try{
            Vendedor vendedor = vendedorService.buscarVendedorPorId(id);
            return ResponseEntity.ok(vendedor);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirVendedor(@PathVariable int id){
        try{
            vendedorService.excluirVendedor(id);
            return ResponseEntity.noContent().build();
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
