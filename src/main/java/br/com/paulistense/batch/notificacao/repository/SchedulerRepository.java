package br.com.paulistense.batch.notificacao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.paulistense.batch.notificacao.model.Scheduler;

@Repository
public interface SchedulerRepository extends JpaRepository<Scheduler, Long>{

	List<Scheduler> findByAtivoAndSistemaId(String ativo, Long sistemaId);
}
