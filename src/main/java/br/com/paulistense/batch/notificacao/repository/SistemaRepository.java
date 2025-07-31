package br.com.paulistense.batch.notificacao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.paulistense.batch.notificacao.model.Sistema;


@Repository
public interface SistemaRepository extends JpaRepository<Sistema, Long>{

	Optional<Sistema> findByNome(String nome);
}
