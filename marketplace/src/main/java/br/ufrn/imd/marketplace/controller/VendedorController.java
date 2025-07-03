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
            vendedorService.solicitarVendedor(usuarioId);
            return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> listarVendedores() {
            List<Vendedor> vendedores = vendedorService.listarVendedores();
            return ResponseEntity.ok(vendedores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarVendedorPorId(@PathVariable int id){
            Vendedor vendedor = vendedorService.buscarVendedorPorId(id);
            return ResponseEntity.ok(vendedor);
    }

    @GetMapping("/status/{vendedorId}")
    public ResponseEntity<?> getStatusVendedor(@PathVariable int vendedorId){
        String status = vendedorService.getVendedorStatus(vendedorId);
        return ResponseEntity.ok(status);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirVendedor(@PathVariable int id){
            vendedorService.excluirVendedor(id);
            return ResponseEntity.noContent().build();
    }


}
