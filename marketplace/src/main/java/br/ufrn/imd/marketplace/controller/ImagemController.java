package br.ufrn.imd.marketplace.controller;


import br.ufrn.imd.marketplace.model.Imagem;
import br.ufrn.imd.marketplace.service.ImagemService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/imagem")
public class ImagemController {

    @Autowired
    private ImagemService imagemService;

    @PostMapping
    public ResponseEntity<?> salvarImagem(@RequestBody Imagem imagem) {
        imagemService.salvarImagem(imagem);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{produtoId}")
    public ResponseEntity<List<Imagem>> listarImagens(@PathVariable int produtoId) {
        return ResponseEntity.status(HttpStatus.OK).body(imagemService.buscarImagensDoProduto(produtoId));
    }

    @DeleteMapping
    public ResponseEntity<?> excluirImagem(@RequestBody Imagem imagem) {
        imagemService.excluirImagem(imagem);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
