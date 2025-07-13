package com.thomaskavi.lucrabet.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thomaskavi.lucrabet.entities.Conta;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
  Optional<Conta> findByNomeConta(String nomeConta);
}
