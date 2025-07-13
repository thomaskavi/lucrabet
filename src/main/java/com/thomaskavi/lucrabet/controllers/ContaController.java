package com.thomaskavi.lucrabet.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.thomaskavi.lucrabet.entities.Conta;
import com.thomaskavi.lucrabet.services.ContaService;

@RestController
@RequestMapping("/api/contas") // Define o caminho base para todos os endpoints neste controller
public class ContaController {

  @Autowired
  private ContaService contaService;

  // GET /api/contas
  // Retorna todas as contas cadastradas
  @GetMapping
  public List<Conta> getAllContas() {
    return contaService.findAll();
  }

  // GET /api/contas/{id}
  // Retorna uma conta específica pelo ID
  @GetMapping("/{id}")
  public ResponseEntity<Conta> getContaById(@PathVariable Long id) {
    Optional<Conta> conta = contaService.findById(id);
    return conta.map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  // POST /api/contas
  // Cria uma nova conta
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Conta createConta(@RequestBody Conta conta) {
    return contaService.save(conta);
  }

  // PUT /api/contas/{id}
  // Atualiza uma conta existente
  @PutMapping("/{id}")
  public ResponseEntity<Conta> updateConta(@PathVariable Long id, @RequestBody Conta contaDetails) {
    Optional<Conta> conta = contaService.findById(id);
    if (conta.isPresent()) {
      Conta existingConta = conta.get();
      existingConta.setNomeConta(contaDetails.getNomeConta());
      return ResponseEntity.ok(contaService.save(existingConta));
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  // DELETE /api/contas/{id}
  // Deleta uma conta
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<Void> deleteConta(@PathVariable Long id) {
    if (contaService.findById(id).isPresent()) {
      contaService.deleteById(id);
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}