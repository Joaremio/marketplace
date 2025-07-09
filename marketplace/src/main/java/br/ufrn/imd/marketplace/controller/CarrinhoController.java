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
        return ResponseEntity.status(HttpStatus.CREATED).body(carrinhoService.criarCarrinho(usuarioId));
    }

    @PostMapping("/produto")
    public ResponseEntity<?> inserirProduto(@RequestBody CarrinhoProduto produto) {
        carrinhoService.inserirProduto(produto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/produto/{produtoId}/{usuarioId}")
    public ResponseEntity<?> removerProduto(@PathVariable int produtoId, @PathVariable int usuarioId) {
        // CORREÇÃO: A ordem dos argumentos foi ajustada para bater com o que o serviço espera.
        carrinhoService.removerProduto(usuarioId, produtoId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{carrinhoId}")
    public ResponseEntity<?> getProdutosCarrinho(@PathVariable int carrinhoId) {
        return ResponseEntity.status(HttpStatus.OK).body(carrinhoService.getProdutos(carrinhoId));
    }

    @GetMapping("/todos")
    public ResponseEntity<?> getAllCarrinhos() {
        return ResponseEntity.status(HttpStatus.OK).body(carrinhoService.listarCarrinhos());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> getCarrinhoPorUsuario(@PathVariable int usuarioId) {
        // Chama o serviço que retorna o objeto completo
        Carrinho carrinhoCompleto = carrinhoService.buscarCarrinhoCompletoPorUsuarioId(usuarioId);

        if (carrinhoCompleto != null) {
            return ResponseEntity.ok(carrinhoCompleto); // Retorna 200 OK com o carrinho e seus produtos
        } else {
            return ResponseEntity.notFound().build(); // Retorna 404 se o usuário não tiver um carrinho
        }
    }

    // Em CarrinhoController.java

    @PutMapping("/produto/quantidade")
    public ResponseEntity<?> atualizarQuantidade(@RequestBody CarrinhoProduto produto) {
        // Vamos precisar de um método no serviço para isso
        carrinhoService.atualizarQuantidade(produto);
        return ResponseEntity.ok().build();
    }
}