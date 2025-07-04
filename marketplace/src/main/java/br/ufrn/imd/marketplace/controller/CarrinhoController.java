package br.ufrn.imd.marketplace.controller;


import br.ufrn.imd.marketplace.model.Carrinho;
import br.ufrn.imd.marketplace.model.CarrinhoProduto;
import br.ufrn.imd.marketplace.service.CarrinhoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> removerProduto(int produtoId, int usuarioId) {
        carrinhoService.removerProduto(produtoId, usuarioId);
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
}
