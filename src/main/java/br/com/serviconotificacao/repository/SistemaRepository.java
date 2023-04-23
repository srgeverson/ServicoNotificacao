package br.com.serviconotificacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.serviconotificacao.model.SistemaModel;

@Repository
public interface SistemaRepository extends JpaRepository<SistemaModel, Long> {

	SistemaModel findByNome(String nome);
}
