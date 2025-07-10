package br.ufrn.imd.marketplace.controller;

import br.ufrn.imd.marketplace.model.Carrinho;
import br.ufrn.imd.marketplace.model.CarrinhoProduto;
import br.ufrn.imd.marketplace.service.CarrinhoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/carrinho")
public class CarrinhoController {

    @Autowired
    private CarrinhoService carrinhoService;

    @PostMapping("/{usuarioId}")
    public ResponseEntity<?> criarCarrinho(@PathVariable int usuarioId) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(carrinhoService.criarCarrinho(usuarioId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/produto")
    public ResponseEntity<?> inserirProduto(@RequestBody CarrinhoProduto produto) {
        try {
            carrinhoService.inserirProduto(produto);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/produto/{produtoId}/{usuarioId}")
    public ResponseEntity<?> removerProduto(@PathVariable int produtoId, @PathVariable int usuarioId) {
        try {
            carrinhoService.removerProduto(usuarioId, produtoId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{carrinhoId}")
    public ResponseEntity<?> getProdutosCarrinho(@PathVariable int carrinhoId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(carrinhoService.getProdutos(carrinhoId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/todos")
    public ResponseEntity<?> getAllCarrinhos() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(carrinhoService.listarCarrinhos());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> getCarrinhoPorUsuario(@PathVariable int usuarioId) {
        try {
            // Chama o serviço que retorna o objeto completo
            Carrinho carrinhoCompleto = carrinhoService.buscarCarrinhoCompletoPorUsuarioId(usuarioId);

            if (carrinhoCompleto != null) {
                return ResponseEntity.ok(carrinhoCompleto); // Retorna 200 OK com o carrinho e seus produtos
            } else {
                return ResponseEntity.notFound().build(); // Retorna 404 se o usuário não tiver um carrinho
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/produto/quantidade")
    public ResponseEntity<?> atualizarQuantidade(@RequestBody CarrinhoProduto produto) {
        try {
            carrinhoService.atualizarQuantidade(produto);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}