package br.ufrn.imd.marketplace.controller;


import br.ufrn.imd.marketplace.dao.ProdutoDAO;
import br.ufrn.imd.marketplace.model.Produto;
import br.ufrn.imd.marketplace.service.ProdutoService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @PostMapping("/{vendedorId}")
    public ResponseEntity<?> cadastrarProduto(@PathVariable int vendedorId, @RequestBody Produto produto) {
        Produto produtoCadastrado = produtoService.cadastrarProduto(vendedorId, produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoCadastrado);
    }

    @GetMapping("/{vendedorId}")
    public ResponseEntity<?> buscarProdutosPorVendedor(@PathVariable int vendedorId) {
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.buscarProdutosPorVendedor(vendedorId));
    }

    @GetMapping("/{produtoId}/{vendedorId}")
    public ResponseEntity<?> buscarProdutoPorId(@PathVariable int produtoId, @PathVariable int vendedorId){
            return ResponseEntity.status(HttpStatus.OK).body(produtoService.buscarProdutoPorId(vendedorId, produtoId));
    }

    @PutMapping("/{produtoId}/{vendedorId}")
    public ResponseEntity<?> desativarProduto(@PathVariable int produtoId, @PathVariable int vendedorId) {
        produtoService.desativarProduto(vendedorId, produtoId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/atualizar/{produtoId}/{vendedorId}")
    public ResponseEntity<?> atualizarProduto(@PathVariable int produtoId, @PathVariable int vendedorId, @RequestBody Produto produto) {
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.atualizarProduto(produtoId, vendedorId, produto));
    }

    @DeleteMapping("/{produtoId}")
    public ResponseEntity<?> removerProduto(@PathVariable int produtoId) {
        produtoService.deletarProduto(produtoId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
