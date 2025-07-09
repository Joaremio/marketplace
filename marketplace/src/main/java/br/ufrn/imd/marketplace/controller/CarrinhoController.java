package br.ufrn.imd.marketplace.controller;

import br.ufrn.imd.marketplace.dto.AtualizarQuantidadeRequest;
import br.ufrn.imd.marketplace.dto.ProdutoCarrinhoDetalhado;
import br.ufrn.imd.marketplace.model.Carrinho;
import br.ufrn.imd.marketplace.model.CarrinhoProduto;
import br.ufrn.imd.marketplace.service.CarrinhoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/carrinho")
public class CarrinhoController {

    @Autowired
    private CarrinhoService carrinhoService;

    // Busca o carrinho de um usuário (cria se não existir)
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> getCarrinhoPorUsuario(@PathVariable int usuarioId) {
        Optional<Carrinho> carrinhoOpt = carrinhoService.getCarrinhoByID(usuarioId);
        // Se não encontrar, cria um novo carrinho para o usuário
        return ResponseEntity.ok(carrinhoOpt.orElseGet(() -> carrinhoService.criarCarrinho(usuarioId)));
    }
    
    // Busca todos os produtos detalhados de um carrinho pelo ID do usuário
    @GetMapping("/detalhes/usuario/{usuarioId}")
    public ResponseEntity<List<ProdutoCarrinhoDetalhado>> getProdutosDetalhados(@PathVariable int usuarioId) {
        List<ProdutoCarrinhoDetalhado> produtos = carrinhoService.getProdutosDetalhadosPorUsuario(usuarioId);
        return ResponseEntity.ok(produtos);
    }

    // Insere um novo produto no carrinho
    @PostMapping("/produto")
    public ResponseEntity<?> inserirProduto(@RequestBody CarrinhoProduto produto) {
        carrinhoService.inserirProduto(produto);
        return ResponseEntity.ok().build();
    }

    // Atualiza a quantidade de um produto
    @PutMapping("/produto/quantidade")
    public ResponseEntity<?> atualizarQuantidade(@RequestBody AtualizarQuantidadeRequest request) {
        carrinhoService.atualizarQuantidadeProduto(request.getCarrinhoId(), request.getProdutoId(), request.getQuantidade());
        return ResponseEntity.ok().build();
    }

    // Remove um produto do carrinho
    @DeleteMapping("/{carrinhoId}/produto/{produtoId}")
    public ResponseEntity<?> removerProduto(@PathVariable int carrinhoId, @PathVariable int produtoId) {
        carrinhoService.removerProduto(carrinhoId, produtoId);
        return ResponseEntity.noContent().build();
    }
}