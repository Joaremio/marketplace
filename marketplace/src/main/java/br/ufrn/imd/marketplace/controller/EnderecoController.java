package br.ufrn.imd.marketplace.controller;


import br.ufrn.imd.marketplace.dto.EnderecoCepDTO;
import br.ufrn.imd.marketplace.model.Cep;
import br.ufrn.imd.marketplace.model.Endereco;
import br.ufrn.imd.marketplace.service.CepService;
import br.ufrn.imd.marketplace.service.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/endereco")
public class EnderecoController {
    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private CepService cepService;

    //cria endereco completo
    @PostMapping("/{usuarioId}")
    public ResponseEntity<?> inserirEndereco(@PathVariable int usuarioId, @RequestBody EnderecoCepDTO dto) {
        Cep cep = dto.getCep();
        Endereco endereco = dto.getEndereco();

        cepService.inserirCep(cep);
        enderecoService.inserirEndereco(usuarioId, endereco);

        dto.setEndereco(endereco);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }


    @GetMapping("/{enderecoId}")
    public ResponseEntity<?> buscarEnderecoCompleto(@PathVariable int enderecoId) {
        EnderecoCepDTO dto = enderecoService.buscarEnderecoCompletoPorId(enderecoId);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endereço não encontrado.");
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> buscarEnderecosDoUsuario(@PathVariable int usuarioId) {
        List<EnderecoCepDTO> enderecos = enderecoService.buscarEnderecosPorUsuario(usuarioId);
        if (enderecos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum endereço encontrado para o usuário.");
        }
        return ResponseEntity.ok(enderecos);
    }

    @GetMapping("/usuario/{usuarioId}/cep/{cep}")
    public ResponseEntity<?> buscarEnderecoPorUsuarioEcep(@PathVariable int usuarioId, @PathVariable String cep) {
        List<EnderecoCepDTO> enderecos = enderecoService.buscarEnderecoPorUsuarioEcep(usuarioId, cep);
        if (enderecos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum endereço encontrado para o usuário.");
        }
        return ResponseEntity.ok(enderecos);
    }

    @GetMapping("/usuario/{usuarioId}/principal")
    public ResponseEntity<?> buscarEnderecoPrincipal(@PathVariable int usuarioId) {
        EnderecoCepDTO dto = enderecoService.buscarEnderecoPrincipal(usuarioId);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endereço principal não encontrado.");
        }
    }

    @DeleteMapping("/{enderecoId}")
    public ResponseEntity<?> deletarEndereco(@PathVariable int enderecoId) {
        enderecoService.deletarEnderecoComCep(enderecoId);
        return ResponseEntity.ok("Endereço deletado com sucesso.");
    }

    @PutMapping("/{usuarioId}/endereco/{enderecoId}")
    public ResponseEntity<?> atualizarEndereco(
            @PathVariable int usuarioId,
            @PathVariable int enderecoId,
            @RequestBody EnderecoCepDTO dto) {

        enderecoService.atualizarEnderecoComCep(usuarioId, enderecoId, dto);
        return ResponseEntity.ok("Endereço atualizado com sucesso.");
    }


}
