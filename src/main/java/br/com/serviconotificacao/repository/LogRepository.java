package br.com.serviconotificacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.serviconotificacao.model.LogModel;

@Repository
public interface LogRepository extends JpaRepository<LogModel, Long> {

}
