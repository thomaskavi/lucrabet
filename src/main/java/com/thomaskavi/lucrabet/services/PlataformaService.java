package com.thomaskavi.lucrabet.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thomaskavi.lucrabet.entities.Plataforma;
import com.thomaskavi.lucrabet.repositories.PlataformaRepository;

@Service
public class PlataformaService {

  @Autowired
  private PlataformaRepository plataformaRepository;

  // Método para buscar todas as plataformas
  public List<Plataforma> findAll() {
    return plataformaRepository.findAll();
  }

  // Método para buscar uma plataforma por ID
  public Optional<Plataforma> findById(Long id) {
    return plataformaRepository.findById(id);
  }

  // Método para salvar (criar ou atualizar) uma plataforma
  public Plataforma save(Plataforma plataforma) {
    return plataformaRepository.save(plataforma);
  }

  // Método para deletar uma plataforma por ID
  public void deleteById(Long id) {
    plataformaRepository.deleteById(id);
  }

  // Método para buscar uma plataforma pelo nome do link
  public Optional<Plataforma> findByLinkPlataforma(String linkPlataforma) {
    return plataformaRepository.findByLinkPlataforma(linkPlataforma);
  }

  /**
   * Método para obter uma Plataforma existente pelo nome do link ou criar uma
   * nova se não existir.
   * Isso é útil para garantir que não haja duplicação de plataformas ao criar
   * RegistroOperacao.
   * 
   * @param linkPlataforma O nome do link da plataforma.
   * @return A Plataforma encontrada ou uma nova criada e salva.
   */
  public Plataforma getOrCreatePlataforma(String linkPlataforma) {
    return plataformaRepository.findByLinkPlataforma(linkPlataforma)
        .orElseGet(() -> plataformaRepository.save(new Plataforma(null, linkPlataforma)));
    // O ID é null aqui porque será gerado automaticamente pelo JPA
  }
}