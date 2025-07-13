package com.thomaskavi.lucrabet.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thomaskavi.lucrabet.entities.Conta;
import com.thomaskavi.lucrabet.repositories.ContaRepository;

@Service
public class ContaService {

  @Autowired
  private ContaRepository contaRepository;

  // Método para buscar todas as contas
  public List<Conta> findAll() {
    return contaRepository.findAll();
  }

  // Método para buscar uma conta por ID
  public Optional<Conta> findById(Long id) {
    return contaRepository.findById(id);
  }

  // Método para salvar (criar ou atualizar) uma conta
  public Conta save(Conta conta) {
    return contaRepository.save(conta);
  }

  // Método para deletar uma conta por ID
  public void deleteById(Long id) {
    contaRepository.deleteById(id);
  }

  // Método para buscar uma conta pelo nome da conta
  public Optional<Conta> findByNomeConta(String nomeConta) {
    return contaRepository.findByNomeConta(nomeConta);
  }

  /**
   * Método para obter uma Conta existente pelo nome da conta ou criar uma nova se
   * não existir.
   * Isso é útil para garantir que não haja duplicação de contas ao criar
   * RegistroOperacao.
   * 
   * @param nomeConta O nome da conta.
   * @return A Conta encontrada ou uma nova criada e salva.
   */
  public Conta getOrCreateConta(String nomeConta) {
    return contaRepository.findByNomeConta(nomeConta)
        .orElseGet(() -> contaRepository.save(new Conta(null, nomeConta)));
    // O ID é null aqui porque será gerado automaticamente pelo JPA
  }
}