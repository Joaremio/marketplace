package br.ufrn.imd.marketplace.controller;

import br.ufrn.imd.marketplace.dto.AvaliacaoRequest;
import br.ufrn.imd.marketplace.model.Pedido;
import br.ufrn.imd.marketplace.model.PedidoProduto;
import br.ufrn.imd.marketplace.service.PedidoProdutoService;
import br.ufrn.imd.marketplace.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedido")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // INJEÇÃO MANTIDA CASO USE PARA OUTRAS COISAS, MAS NÃO É USADO NA CRIAÇÃO DO PEDIDO
    @Autowired
    private PedidoProdutoService pedidoProdutoService;


    @PostMapping()
    public ResponseEntity<?> criarPedido(@RequestBody Pedido pedido) {
        // CORREÇÃO: A lógica agora está toda encapsulada no serviço, garantindo atomicidade.
        // O frontend deve enviar o objeto Pedido contendo a lista de itens.
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.criarPedido(pedido));
    }

    @GetMapping("/comprador/{compradorId}")
    public ResponseEntity<?> listarPedidosPorComprador(@PathVariable int compradorId) {
        return ResponseEntity.status(HttpStatus.OK).body(pedidoService.buscarPedidosDTOPorComprador(compradorId));
    }


    @GetMapping("/vendedorPedidos/{vendedorId}")
    public ResponseEntity<?> listarPedidosPorVendedor(@PathVariable int vendedorId) {
        return ResponseEntity.status(HttpStatus.OK).body(pedidoService.buscarPedidosPorVendedor(vendedorId));
    }


    // CORREÇÃO: O nome da variável de path foi corrigido para 'pedidoId'.
    // CORREÇÃO: A chamada duplicada ao serviço foi removida.
    @GetMapping("/{pedidoId}")
    public ResponseEntity<?> buscarPedidoPorId(@PathVariable int pedidoId) {
        Pedido pedido = pedidoService.buscarPedidoPorId(pedidoId);
        return ResponseEntity.status(HttpStatus.OK).body(pedido);
    }

    @DeleteMapping("/{pedidoId}")
    public ResponseEntity<?> excluirPedido(@PathVariable int pedidoId) {
        pedidoService.excluirPedido(pedidoId);
        return ResponseEntity.noContent().build();
    }

    // CORREÇÃO: O nome da variável de path foi corrigido para 'pedidoId'.
    @PutMapping("/{pedidoId}/{status}")
    public ResponseEntity<?> atualizarStatusPedido(@PathVariable int pedidoId, @PathVariable String status) {
        pedidoService.atualizarStatusPedido(pedidoId, status);
        return ResponseEntity.noContent().build();
    }

    // Estes endpoints podem ser mantidos para futuras funcionalidades de edição de pedido,
    // mas não são mais usados no fluxo principal de criação.
    @PostMapping("/item")
    public ResponseEntity<?> adicionarItemAoPedido(@RequestBody PedidoProduto item){
        pedidoProdutoService.AdicionarItemAoPedido(item);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{pedidoId}/{itemId}")
    public ResponseEntity<?> removerItemAoPedido(@PathVariable int pedidoId, @PathVariable int itemId) {
        pedidoProdutoService.ExcluirItemAoPedido(pedidoId, itemId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/avaliar-item")
    public ResponseEntity<?> avaliarItemPedido(@RequestBody AvaliacaoRequest request) {
        pedidoProdutoService.avaliarProduto(request.getPedidoId(), request.getProdutoId(), request.getAvaliacao());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/avaliacoes/produto/{produtoId}")
    public ResponseEntity<List<AvaliacaoRequest>> buscarAvaliacoesPorProduto(@PathVariable int produtoId) {
        List<AvaliacaoRequest> avaliacoes = pedidoProdutoService.buscarAvaliacoesPorProduto(produtoId);
        return ResponseEntity.ok(avaliacoes);
    }
}