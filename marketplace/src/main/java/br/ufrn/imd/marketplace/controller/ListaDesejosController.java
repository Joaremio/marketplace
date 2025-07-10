package br.ufrn.imd.marketplace.controller;


import br.ufrn.imd.marketplace.dto.ProdutoImagemDTO;
import br.ufrn.imd.marketplace.model.ListaDesejos;
import br.ufrn.imd.marketplace.model.ListaProduto;
import br.ufrn.imd.marketplace.service.ListaDesejosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/listaDesejos")
public class ListaDesejosController {


    @Autowired
    private ListaDesejosService listaDesejosService;

    @PostMapping
    public ResponseEntity<?> criarListaDesejos(@RequestBody ListaDesejos listaDesejos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(listaDesejosService.criarListaDesejos(listaDesejos));
    }

    @GetMapping("/{listaId}")
    public ResponseEntity<?> getListaDesejosById(@PathVariable int listaId){
        return ResponseEntity.status(HttpStatus.OK).body(listaDesejosService.getListaDesejosById(listaId));
    }

    @PostMapping("/adicionar")
    public ResponseEntity<?> adicionarProdutoAListaDesejos(@RequestBody ListaProduto produto){
       listaDesejosService.adicionarProdutoAListaDesejos(produto);
       return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/produtos/{listaId}")
    public ResponseEntity<?> getProdutoIdsDaLista(@PathVariable int listaId) {
        List<Integer> produtoIds = listaDesejosService.getListaDesejosComProduto(listaId);

        if (produtoIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Nenhum produto encontrado para essa lista.");
        }

        return ResponseEntity.ok(produtoIds);  // Exemplo de retorno: [4, 7, 10]
    }

    @GetMapping("/{listaId}/detalhes")
    public ResponseEntity<List<ProdutoImagemDTO>> getProdutosDetalhadosDaLista(@PathVariable int listaId) {
        List<ProdutoImagemDTO> produtos = listaDesejosService.buscarProdutosCompletosDaLista(listaId);
        // Não precisa mais verificar se está vazio, o frontend pode fazer isso.
        // Retornar uma lista vazia com status 200 OK é o padrão.
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/por-comprador/{compradorId}")
    public ResponseEntity<List<ListaDesejos>> buscarListasPorComprador(@PathVariable int compradorId) {
        List<ListaDesejos> listas = listaDesejosService.buscarListasPorComprador(compradorId);
        return ResponseEntity.ok(listas);
    }

    // Adicione estes dois novos endpoints ao seu Controller

    // Endpoint para atualizar o nome
    @PutMapping("/{listaId}")
    public ResponseEntity<?> atualizarNomeLista(@PathVariable int listaId, @RequestBody ListaDesejos lista) {
        // O DTO/Model recebido deve conter o novo nome
        listaDesejosService.atualizarNomeLista(listaId, lista.getNome());
        return ResponseEntity.ok().build();
    }

    // Endpoint para excluir a lista
    @DeleteMapping("/{listaId}")
    public ResponseEntity<?> excluirLista(@PathVariable int listaId) {
        listaDesejosService.excluirLista(listaId);
        return ResponseEntity.noContent().build();
    }
}
