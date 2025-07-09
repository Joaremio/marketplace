package br.ufrn.imd.marketplace.controller;

import br.ufrn.imd.marketplace.dto.ProdutoImagemDTO;
import br.ufrn.imd.marketplace.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    /**
     * Endpoint PÚBLICO: Busca a lista de produtos para a vitrine.
     * Permite filtrar por nome e/ou categoria.
     * Ex: GET /produto?nome=camiseta
     */
    @GetMapping
    public ResponseEntity<List<ProdutoImagemDTO>> buscarProdutosParaVitrine(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String categoria) {
        
        List<ProdutoImagemDTO> produtos = produtoService.buscarProdutosParaVitrine(nome, categoria);
        return ResponseEntity.ok(produtos);
    }

    /**
     * Endpoint PÚBLICO: Busca os detalhes de um único produto pelo seu ID.
     * Usado pela página de detalhes do produto (PaginaProduto.jsx).
     * Ex: GET /produto/3
     */
    @GetMapping("/{produtoId}")
    public ResponseEntity<?> buscarProdutoPublicoPorId(@PathVariable int produtoId) {
        try {
            ProdutoImagemDTO produto = produtoService.buscarProdutoPublicoPorId(produtoId);
            return ResponseEntity.ok(produto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Endpoint PRIVADO: Busca todos os produtos de um vendedor específico.
     * Rota corrigida para /vendedor/{vendedorId} para evitar conflito.
     * Ex: GET /produto/vendedor/123
     */
    @GetMapping("/vendedor/{vendedorId}")
public ResponseEntity<?> buscarProdutosPorVendedor(@PathVariable int vendedorId) {
    try {
        List<ProdutoImagemDTO> produtos = produtoService.buscarProdutosPorVendedor(vendedorId);
        // Isto retorna um ResponseEntity<List<ProdutoImagemDTO>>
        return ResponseEntity.ok(produtos);
    } catch (RuntimeException e) {
        // Isto retorna um ResponseEntity<String>, o que agora é permitido pelo <?>
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}

    /**
     * Endpoint PRIVADO: Cadastra um novo produto para um vendedor.
     */
    @PostMapping("/{vendedorId}")
    public ResponseEntity<ProdutoImagemDTO> cadastrarProduto(@PathVariable int vendedorId, @RequestBody ProdutoImagemDTO produto) {
        ProdutoImagemDTO produtoCadastrado = produtoService.cadastrarProduto(vendedorId, produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoCadastrado);
    }

    /**
     * Endpoint PRIVADO: Atualiza um produto existente de um vendedor.
     */
    @PutMapping("/atualizar/{produtoId}/{vendedorId}")
    public ResponseEntity<ProdutoImagemDTO> atualizarProduto(@PathVariable int produtoId, @PathVariable int vendedorId, @RequestBody ProdutoImagemDTO produto) {
        ProdutoImagemDTO produtoAtualizado = produtoService.atualizarProduto(produtoId, vendedorId, produto);
        return ResponseEntity.ok(produtoAtualizado);
    }

    /**
     * Endpoint PRIVADO: Desativa um produto (remoção lógica).
     */
    @PutMapping("/desativar/{produtoId}/{vendedorId}")
    public ResponseEntity<Void> desativarProduto(@PathVariable int produtoId, @PathVariable int vendedorId) {
        produtoService.desativarProduto(vendedorId, produtoId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint PRIVADO: Remove um produto permanentemente.
     */
    @DeleteMapping("/{produtoId}")
    public ResponseEntity<Void> removerProduto(@PathVariable int produtoId) {
        produtoService.deletarProduto(produtoId);
        return ResponseEntity.noContent().build();
    }
}