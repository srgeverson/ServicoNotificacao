package br.com.serviconotificacao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.serviconotificacao.model.SistemaModel;

@Repository
public interface SistemaRepository extends JpaRepository<SistemaModel, Long> {

	Optional<SistemaModel> findByNome(String nome);
}
