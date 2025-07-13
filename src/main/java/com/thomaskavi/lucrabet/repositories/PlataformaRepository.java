package com.thomaskavi.lucrabet.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thomaskavi.lucrabet.entities.Plataforma;

@Repository
public interface PlataformaRepository extends JpaRepository<Plataforma, Long> {

  Optional<Plataforma> findByLinkPlataforma(String linkPlataforma);
}