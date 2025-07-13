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

import com.thomaskavi.lucrabet.entities.Plataforma;
import com.thomaskavi.lucrabet.services.PlataformaService;

@RestController
@RequestMapping("/api/plataformas") // Define o caminho base para todos os endpoints neste controller
public class PlataformaController {

  @Autowired
  private PlataformaService plataformaService;

  // GET /api/plataformas
  // Retorna todas as plataformas cadastradas
  @GetMapping
  public List<Plataforma> getAllPlataformas() {
    return plataformaService.findAll();
  }

  // GET /api/plataformas/{id}
  // Retorna uma plataforma específica pelo ID
  @GetMapping("/{id}")
  public ResponseEntity<Plataforma> getPlataformaById(@PathVariable Long id) {
    Optional<Plataforma> plataforma = plataformaService.findById(id);
    return plataforma.map(ResponseEntity::ok) // Se encontrar, retorna 200 OK com o objeto
        .orElseGet(() -> ResponseEntity.notFound().build()); // Se não encontrar, retorna 404 Not Found
  }

  // POST /api/plataformas
  // Cria uma nova plataforma
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED) // Retorna 201 Created em caso de sucesso
  public Plataforma createPlataforma(@RequestBody Plataforma plataforma) {
    return plataformaService.save(plataforma);
  }

  // PUT /api/plataformas/{id}
  // Atualiza uma plataforma existente
  @PutMapping("/{id}")
  public ResponseEntity<Plataforma> updatePlataforma(@PathVariable Long id, @RequestBody Plataforma plataformaDetails) {
    Optional<Plataforma> plataforma = plataformaService.findById(id);
    if (plataforma.isPresent()) {
      Plataforma existingPlataforma = plataforma.get();
      existingPlataforma.setLinkPlataforma(plataformaDetails.getLinkPlataforma());
      return ResponseEntity.ok(plataformaService.save(existingPlataforma)); // Retorna 200 OK com o objeto atualizado
    } else {
      return ResponseEntity.notFound().build(); // Se não encontrar, retorna 404 Not Found
    }
  }

  // DELETE /api/plataformas/{id}
  // Deleta uma plataforma
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT) // Retorna 204 No Content em caso de sucesso (sem corpo na resposta)
  public ResponseEntity<Void> deletePlataforma(@PathVariable Long id) {
    if (plataformaService.findById(id).isPresent()) {
      plataformaService.deleteById(id);
      return ResponseEntity.noContent().build(); // Retorna 204 No Content
    } else {
      return ResponseEntity.notFound().build(); // Se não encontrar, retorna 404 Not Found
    }
  }
}