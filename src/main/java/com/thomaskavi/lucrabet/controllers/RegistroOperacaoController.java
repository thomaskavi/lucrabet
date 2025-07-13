package com.thomaskavi.lucrabet.controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.thomaskavi.lucrabet.entities.RegistroOperacao;
import com.thomaskavi.lucrabet.services.RegistroOperacaoService;

@RestController
@RequestMapping("/api/registros-operacoes") // Define o caminho base
public class RegistroOperacaoController {

  @Autowired
  private RegistroOperacaoService registroOperacaoService;

  // GET /api/registros-operacoes
  // Retorna todos os registros de operações
  @GetMapping
  public List<RegistroOperacao> getAllRegistros() {
    return registroOperacaoService.findAll();
  }

  // GET /api/registros-operacoes/{id}
  // Retorna um registro de operação específico pelo ID
  @GetMapping("/{id}")
  public ResponseEntity<RegistroOperacao> getRegistroById(@PathVariable Long id) {
    Optional<RegistroOperacao> registro = registroOperacaoService.findById(id);
    return registro.map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  // POST /api/registros-operacoes
  // Cria um novo registro de operação
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public RegistroOperacao createRegistro(@RequestBody RegistroOperacao registroOperacao) {
    return registroOperacaoService.save(registroOperacao);
  }

  // PUT /api/registros-operacoes/{id}
  // Atualiza um registro de operação existente
  @PutMapping("/{id}")
  public ResponseEntity<RegistroOperacao> updateRegistro(@PathVariable Long id,
      @RequestBody RegistroOperacao registroDetails) {
    Optional<RegistroOperacao> existingRegistroOptional = registroOperacaoService.findById(id);
    if (existingRegistroOptional.isPresent()) {
      RegistroOperacao existingRegistro = existingRegistroOptional.get();

      // Atualiza os campos relevantes
      existingRegistro.setDataOperacao(registroDetails.getDataOperacao());
      existingRegistro.setValorDeposito(registroDetails.getValorDeposito());
      existingRegistro.setNomeSlotGiros(registroDetails.getNomeSlotGiros());
      existingRegistro.setSituacao(registroDetails.getSituacao());
      existingRegistro.setValorLucro(registroDetails.getValorLucro());
      existingRegistro.setSaqueCompletoFeito(registroDetails.getSaqueCompletoFeito());

      // Para as associações, o ideal é que o 'registroDetails' já venha com as
      // entidades Plataforma e Conta
      // preenchidas (pelo menos com o ID ou nome para que o Service possa
      // buscar/criar).
      // O Service já tem a lógica de 'getOrCreate', então apenas passar o objeto é
      // suficiente.
      existingRegistro.setPlataforma(registroDetails.getPlataforma());
      existingRegistro.setConta(registroDetails.getConta());

      RegistroOperacao updatedRegistro = registroOperacaoService.save(existingRegistro);
      return ResponseEntity.ok(updatedRegistro);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  // DELETE /api/registros-operacoes/{id}
  // Deleta um registro de operação
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<Void> deleteRegistro(@PathVariable Long id) {
    if (registroOperacaoService.findById(id).isPresent()) {
      registroOperacaoService.deleteById(id);
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  // --- Endpoints de Consulta e Cálculo ---

  // GET /api/registros-operacoes/data/{data}
  // Busca registros por uma data específica
  @GetMapping("/data/{data}")
  public List<RegistroOperacao> getRegistrosByData(
      @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
    return registroOperacaoService.findByDataOperacao(data);
  }

  // GET /api/registros-operacoes/periodo?startDate=...&endDate=...
  // Busca registros por um período de datas
  @GetMapping("/periodo")
  public List<RegistroOperacao> getRegistrosByPeriodo(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
    return registroOperacaoService.findByPeriodo(startDate, endDate);
  }

  // GET /api/registros-operacoes/plataforma/{linkPlataforma}
  // Busca registros por link da plataforma
  @GetMapping("/plataforma/{linkPlataforma}")
  public List<RegistroOperacao> getRegistrosByPlataforma(@PathVariable String linkPlataforma) {
    return registroOperacaoService.findByPlataformaLink(linkPlataforma);
  }

  // GET /api/registros-operacoes/conta/{nomeConta}
  // Busca registros por nome da conta
  @GetMapping("/conta/{nomeConta}")
  public List<RegistroOperacao> getRegistrosByConta(@PathVariable String nomeConta) {
    return registroOperacaoService.findByContaNome(nomeConta);
  }

  // GET /api/registros-operacoes/situacao/{situacao}
  // Busca registros por situação
  @GetMapping("/situacao/{situacao}")
  public List<RegistroOperacao> getRegistrosBySituacao(@PathVariable String situacao) {
    return registroOperacaoService.findBySituacao(situacao);
  }

  // GET /api/registros-operacoes/saque-completo/{saqueFeito}
  // Busca registros por status de saque completo
  @GetMapping("/saque-completo/{saqueFeito}")
  public List<RegistroOperacao> getRegistrosBySaqueCompleto(@PathVariable Boolean saqueFeito) {
    return registroOperacaoService.findBySaqueCompletoFeito(saqueFeito);
  }

  // GET /api/registros-operacoes/lucro-diario/{data}
  // Calcula o lucro total para um dia específico
  @GetMapping("/lucro-diario/{data}")
  public ResponseEntity<BigDecimal> getLucroTotalDiario(
      @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
    BigDecimal lucro = registroOperacaoService.calcularLucroTotalDiario(data);
    return ResponseEntity.ok(lucro);
  }

  // GET /api/registros-operacoes/deposito-diario/{data}
  // Calcula o depósito total para um dia específico
  @GetMapping("/deposito-diario/{data}")
  public ResponseEntity<BigDecimal> getDepositoTotalDiario(
      @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
    BigDecimal deposito = registroOperacaoService.calcularDepositoTotalDiario(data);
    return ResponseEntity.ok(deposito);
  }

  // GET /api/registros-operacoes/lucro-liquido-diario/{data}
  // Calcula o lucro líquido (lucro - depósito) para um dia específico
  @GetMapping("/lucro-liquido-diario/{data}")
  public ResponseEntity<BigDecimal> getLucroLiquidoDiario(
      @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
    BigDecimal lucroLiquido = registroOperacaoService.calcularLucroLiquidoDiario(data);
    return ResponseEntity.ok(lucroLiquido);
  }

  // GET /api/registros-operacoes/lucro-total-geral
  // Calcula o lucro total de todas as operações
  @GetMapping("/lucro-total-geral")
  public ResponseEntity<BigDecimal> getLucroTotalGeral() {
    BigDecimal lucro = registroOperacaoService.calcularLucroTotalGeral();
    return ResponseEntity.ok(lucro);
  }

  // GET /api/registros-operacoes/deposito-total-geral
  // Calcula o depósito total de todas as operações
  @GetMapping("/deposito-total-geral")
  public ResponseEntity<BigDecimal> getDepositoTotalGeral() {
    BigDecimal deposito = registroOperacaoService.calcularDepositoTotalGeral();
    return ResponseEntity.ok(deposito);
  }

  // GET /api/registros-operacoes/lucro-liquido-geral
  // Calcula o lucro líquido total de todas as operações
  @GetMapping("/lucro-liquido-geral")
  public ResponseEntity<BigDecimal> getLucroLiquidoGeral() {
    BigDecimal lucroLiquido = registroOperacaoService.calcularLucroLiquidoGeral();
    return ResponseEntity.ok(lucroLiquido);
  }

  // GET /api/registros-operacoes/lucro-por-plataforma/{linkPlataforma}
  // Calcula o lucro total para uma plataforma específica
  @GetMapping("/lucro-por-plataforma/{linkPlataforma}")
  public ResponseEntity<BigDecimal> getLucroTotalPorPlataforma(@PathVariable String linkPlataforma) {
    BigDecimal lucro = registroOperacaoService.calcularLucroTotalPorPlataforma(linkPlataforma);
    return ResponseEntity.ok(lucro);
  }

  // GET /api/registros-operacoes/lucro-por-conta/{nomeConta}
  // Calcula o lucro total para uma conta específica
  @GetMapping("/lucro-por-conta/{nomeConta}")
  public ResponseEntity<BigDecimal> getLucroTotalPorConta(@PathVariable String nomeConta) {
    BigDecimal lucro = registroOperacaoService.calcularLucroTotalPorConta(nomeConta);
    return ResponseEntity.ok(lucro);
  }

  // GET /api/registros-operacoes/sumario-diario/{data}
  // Retorna um resumo de lucro e depósito para um dia específico
  @GetMapping("/sumario-diario/{data}")
  public ResponseEntity<Map<String, BigDecimal>> getSumarioDiario(
      @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
    BigDecimal totalLucro = registroOperacaoService.calcularLucroTotalDiario(data);
    BigDecimal totalDeposito = registroOperacaoService.calcularDepositoTotalDiario(data);
    BigDecimal lucroLiquido = registroOperacaoService.calcularLucroLiquidoDiario(data);

    Map<String, BigDecimal> sumario = Map.of(
        "lucroTotal", totalLucro,
        "depositoTotal", totalDeposito,
        "lucroLiquido", lucroLiquido);
    return ResponseEntity.ok(sumario);
  }

  // GET /api/registros-operacoes/lucro-total-por-dia
  // Retorna o lucro total agrupado por dia para todas as operações
  @GetMapping("/lucro-total-por-dia")
  public ResponseEntity<Map<LocalDate, BigDecimal>> getLucroTotalPorDia() {
    Map<LocalDate, BigDecimal> lucroPorDia = registroOperacaoService.getLucroTotalPorDia();
    return ResponseEntity.ok(lucroPorDia);
  }
}