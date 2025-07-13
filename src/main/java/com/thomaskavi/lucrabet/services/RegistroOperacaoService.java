package com.thomaskavi.lucrabet.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thomaskavi.lucrabet.entities.Conta;
import com.thomaskavi.lucrabet.entities.Plataforma;
import com.thomaskavi.lucrabet.entities.RegistroOperacao;
import com.thomaskavi.lucrabet.repositories.RegistroOperacaoRepository;

@Service
public class RegistroOperacaoService {

  @Autowired
  private RegistroOperacaoRepository registroOperacaoRepository;

  @Autowired
  private PlataformaService plataformaService; // Para usar o getOrCreatePlataforma

  @Autowired
  private ContaService contaService; // Para usar o getOrCreateConta

  // --- Métodos CRUD Básicos ---

  // Buscar todos os registros de operações
  public List<RegistroOperacao> findAll() {
    return registroOperacaoRepository.findAll();
  }

  // Buscar um registro de operação por ID
  public Optional<RegistroOperacao> findById(Long id) {
    return registroOperacaoRepository.findById(id);
  }

  /**
   * Salva um RegistroOperacao. Antes de salvar, garante que as entidades
   * Plataforma e Conta associadas existem ou são criadas.
   * 
   * @param registroOperacao O objeto RegistroOperacao a ser salvo.
   * @return O RegistroOperacao salvo.
   */
  public RegistroOperacao save(RegistroOperacao registroOperacao) {
    // Garante que a plataforma existe ou é criada
    Plataforma plataforma = plataformaService
        .getOrCreatePlataforma(registroOperacao.getPlataforma().getLinkPlataforma());
    registroOperacao.setPlataforma(plataforma);

    // Garante que a conta existe ou é criada
    Conta conta = contaService.getOrCreateConta(registroOperacao.getConta().getNomeConta());
    registroOperacao.setConta(conta);

    return registroOperacaoRepository.save(registroOperacao);
  }

  // Deletar um registro de operação por ID
  public void deleteById(Long id) {
    registroOperacaoRepository.deleteById(id);
  }

  // --- Métodos de Consulta Específicos ---

  // Buscar registros por data da operação
  public List<RegistroOperacao> findByDataOperacao(LocalDate dataOperacao) {
    return registroOperacaoRepository.findByDataOperacao(dataOperacao);
  }

  // Buscar registros por período de datas
  public List<RegistroOperacao> findByPeriodo(LocalDate startDate, LocalDate endDate) {
    return registroOperacaoRepository.findByDataOperacaoBetween(startDate, endDate);
  }

  // Buscar registros por link da plataforma
  public List<RegistroOperacao> findByPlataformaLink(String linkPlataforma) {
    return registroOperacaoRepository.findByPlataforma_LinkPlataforma(linkPlataforma);
  }

  // Buscar registros por nome da conta
  public List<RegistroOperacao> findByContaNome(String nomeConta) {
    return registroOperacaoRepository.findByConta_NomeConta(nomeConta);
  }

  // Buscar registros por situação
  public List<RegistroOperacao> findBySituacao(String situacao) {
    return registroOperacaoRepository.findBySituacao(situacao);
  }

  // Buscar registros por status de saque completo
  public List<RegistroOperacao> findBySaqueCompletoFeito(Boolean saqueCompletoFeito) {
    return registroOperacaoRepository.findBySaqueCompletoFeito(saqueCompletoFeito);
  }

  // --- Métodos de Cálculos e Agregações ---

  /**
   * Calcula o lucro total para um determinado dia.
   * Corresponde à soma da coluna 'Lucro' para uma data específica.
   */
  public BigDecimal calcularLucroTotalDiario(LocalDate data) {
    return registroOperacaoRepository.findByDataOperacao(data).stream()
        .map(RegistroOperacao::getValorLucro)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  /**
   * Calcula o valor total de depósito para um determinado dia.
   * Corresponde à soma da coluna 'Valor Depós.' para uma data específica.
   */
  public BigDecimal calcularDepositoTotalDiario(LocalDate data) {
    return registroOperacaoRepository.findByDataOperacao(data).stream()
        .map(RegistroOperacao::getValorDeposito)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  /**
   * Calcula o lucro líquido total para um determinado dia (Lucro Total - Depósito
   * Total).
   */
  public BigDecimal calcularLucroLiquidoDiario(LocalDate data) {
    BigDecimal totalLucro = calcularLucroTotalDiario(data);
    BigDecimal totalDeposito = calcularDepositoTotalDiario(data);
    return totalLucro.subtract(totalDeposito);
  }

  /**
   * Calcula o lucro total geral de todas as operações.
   */
  public BigDecimal calcularLucroTotalGeral() {
    return registroOperacaoRepository.findAll().stream()
        .map(RegistroOperacao::getValorLucro)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  /**
   * Calcula o depósito total geral de todas as operações.
   */
  public BigDecimal calcularDepositoTotalGeral() {
    return registroOperacaoRepository.findAll().stream()
        .map(RegistroOperacao::getValorDeposito)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  /**
   * Calcula o lucro líquido total geral de todas as operações (Lucro Total Geral
   * - Depósito Total Geral).
   */
  public BigDecimal calcularLucroLiquidoGeral() {
    BigDecimal totalLucro = calcularLucroTotalGeral();
    BigDecimal totalDeposito = calcularDepositoTotalGeral();
    return totalLucro.subtract(totalDeposito);
  }

  /**
   * Calcula o lucro total para uma plataforma específica.
   */
  public BigDecimal calcularLucroTotalPorPlataforma(String linkPlataforma) {
    return registroOperacaoRepository.findByPlataforma_LinkPlataforma(linkPlataforma).stream()
        .map(RegistroOperacao::getValorLucro)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  /**
   * Calcula o lucro total para uma conta específica.
   */
  public BigDecimal calcularLucroTotalPorConta(String nomeConta) {
    return registroOperacaoRepository.findByConta_NomeConta(nomeConta).stream()
        .map(RegistroOperacao::getValorLucro)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  /**
   * Retorna o lucro total agrupado por dia.
   * Retorna um Map onde a chave é a data e o valor é o lucro total daquele dia.
   */
  public Map<LocalDate, BigDecimal> getLucroTotalPorDia() {
    return registroOperacaoRepository.findAll().stream()
        .collect(Collectors.groupingBy(RegistroOperacao::getDataOperacao,
            Collectors.reducing(BigDecimal.ZERO, RegistroOperacao::getValorLucro, BigDecimal::add)));
  }
}