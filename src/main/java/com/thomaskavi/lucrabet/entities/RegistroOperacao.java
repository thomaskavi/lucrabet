package com.thomaskavi.lucrabet.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_registros_operacoes")
public class RegistroOperacao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private LocalDate dataOperacao;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal valorDeposito;

  @Column(length = 255)
  private String nomeSlotGiros;

  @Column(nullable = false, length = 50)
  private String situacao;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal valorLucro;

  @Column(nullable = false)
  private Boolean saqueCompletoFeito;

  public RegistroOperacao() {
  }

  public RegistroOperacao(Long id, LocalDate dataOperacao, BigDecimal valorDeposito, String nomeSlotGiros,
      String situacao, BigDecimal valorLucro, Boolean saqueCompletoFeito) {
    this.id = id;
    this.dataOperacao = dataOperacao;
    this.valorDeposito = valorDeposito;
    this.nomeSlotGiros = nomeSlotGiros;
    this.situacao = situacao;
    this.valorLucro = valorLucro;
    this.saqueCompletoFeito = saqueCompletoFeito;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDate getDataOperacao() {
    return dataOperacao;
  }

  public void setDataOperacao(LocalDate dataOperacao) {
    this.dataOperacao = dataOperacao;
  }

  public BigDecimal getValorDeposito() {
    return valorDeposito;
  }

  public void setValorDeposito(BigDecimal valorDeposito) {
    this.valorDeposito = valorDeposito;
  }

  public String getNomeSlotGiros() {
    return nomeSlotGiros;
  }

  public void setNomeSlotGiros(String nomeSlotGiros) {
    this.nomeSlotGiros = nomeSlotGiros;
  }

  public String getSituacao() {
    return situacao;
  }

  public void setSituacao(String situacao) {
    this.situacao = situacao;
  }

  public BigDecimal getValorLucro() {
    return valorLucro;
  }

  public void setValorLucro(BigDecimal valorLucro) {
    this.valorLucro = valorLucro;
  }

  public Boolean getSaqueCompletoFeito() {
    return saqueCompletoFeito;
  }

  public void setSaqueCompletoFeito(Boolean saqueCompletoFeito) {
    this.saqueCompletoFeito = saqueCompletoFeito;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    RegistroOperacao other = (RegistroOperacao) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

}
