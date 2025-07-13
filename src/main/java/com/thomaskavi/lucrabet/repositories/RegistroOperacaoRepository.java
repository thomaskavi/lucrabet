package com.thomaskavi.lucrabet.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thomaskavi.lucrabet.entities.RegistroOperacao;

@Repository
public interface RegistroOperacaoRepository extends JpaRepository<RegistroOperacao, Long> {
  // Métodos de consulta baseados nas colunas da planilha e associações

  // Consultas por data da operação
  List<RegistroOperacao> findByDataOperacao(LocalDate dataOperacao);

  List<RegistroOperacao> findByDataOperacaoBetween(LocalDate startDate, LocalDate endDate);

  // Consulta por link da plataforma (usando a propriedade da entidade associada)
  List<RegistroOperacao> findByPlataforma_LinkPlataforma(String linkPlataforma);

  // Consulta por nome da conta (usando a propriedade da entidade associada)
  List<RegistroOperacao> findByConta_NomeConta(String nomeConta);

  // Consulta por situação
  List<RegistroOperacao> findBySituacao(String situacao);

  // Consulta por se o saque foi completo
  List<RegistroOperacao> findBySaqueCompletoFeito(Boolean saqueCompletoFeito);
}