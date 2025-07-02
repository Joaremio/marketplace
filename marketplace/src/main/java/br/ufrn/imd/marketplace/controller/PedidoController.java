package br.ufrn.imd.marketplace.controller;


import br.ufrn.imd.marketplace.model.PedidoProduto;
import br.ufrn.imd.marketplace.service.Pedido;
import br.ufrn.imd.marketplace.service.PedidoProdutoService;
import br.ufrn.imd.marketplace.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedido")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PedidoProdutoService pedidoProdutoService;


    @PostMapping()
    public ResponseEntity<?> criarPedido(@RequestBody Pedido pedido) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.criarPedido(pedido));
    }

    @GetMapping("/comprador/{compradorId}")
    public ResponseEntity<?> listarPedidosPorComprador(@PathVariable int compradorId) {
        return ResponseEntity.status(HttpStatus.OK).body(pedidoService.buscarPedidosPorComprador(compradorId));
    }

    @GetMapping("/{produtoId}")
    public ResponseEntity<?> buscarProdutoPorId(@PathVariable int produtoId) {
        pedidoService.buscarPedidoPorId(produtoId);
        return ResponseEntity.status(HttpStatus.OK).body(pedidoService.buscarPedidoPorId(produtoId));
    }

    @DeleteMapping("/{pedidoId}")
    public ResponseEntity<?> excluirPedido(@PathVariable int pedidoId) {
        pedidoService.excluirPedido(pedidoId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{produtoId}/{status}")
    public ResponseEntity<?> atualizarStatusPedido(@PathVariable int produtoId, @PathVariable String status) {
        pedidoService.atualizarStatusPedido(produtoId, status);
        return ResponseEntity.noContent().build();
    }


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

}
