package br.com.paulistense.batch.notificacao.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import br.com.paulistense.batch.notificacao.model.Scheduler;
import br.com.paulistense.batch.notificacao.model.dto.SistemaDTO;
import br.com.paulistense.batch.notificacao.repository.SchedulerRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.core.SimpleLock;

@Service
@Slf4j
public class SchedulerService {

	@Autowired
	private SchedulerRepository schedulerRepository;
	@Autowired
	private TaskScheduler scheduler;
	private Map<String, ScheduledFuture<?>> tarefasAgendadas = new ConcurrentHashMap<>();
	@Autowired
	private LockProvider lockProvider;
	@Autowired
	private SistemaDTO sistemaDTO;

	@PostConstruct
	public void agendarTarefas() {
		var tarefas = schedulerRepository.findByAtivoAndSistemaId("S", sistemaDTO.getId());
		if (tarefas.isEmpty())
			log.info("Nenhuma tarefa encontrada para para o sistema "
			.concat(sistemaDTO.getDescricao() == null ? "" : sistemaDTO.getDescricao()));
		else
			log.info("Agendando tarefas: ".concat(String.valueOf(tarefas.size())).concat(" tafefas."));

		tarefas.forEach(this::agendarTarefa);
	}

	public void agendarTarefa(Scheduler tarefa) {
		Runnable job = () -> executarTarefa(tarefa);
		CronTrigger trigger = new CronTrigger(tarefa.getCronExpr());
		ScheduledFuture<?> future = scheduler.schedule(job, trigger);
		tarefasAgendadas.put(tarefa.getNome(), future);
		log.info("Agendada tarefa: ".concat(tarefa.getNome())
				.concat(" com a expressão cron: ").concat(tarefa.getCronExpr()));
	}

	public void executarTarefa(Scheduler tarefa) {
    Duration lockAtMostFor = Duration.parse("PT" + tarefa.getLockAtMostFor().toUpperCase());
    Duration lockAtLeastFor = Duration.parse("PT" + tarefa.getLockAtLeastFor().toUpperCase());

    LockConfiguration config = new LockConfiguration(
        Instant.now(),
        tarefa.getNome(),
        lockAtMostFor,
        lockAtLeastFor
    );

    SimpleLock lock = lockProvider.lock(config).orElse(null);

    if (lock == null) {
        log.info("Lock em uso para a tarefa: {}", tarefa.getNome());
        return;
    }

    long inicio = System.currentTimeMillis();

    try {
        log.info("Executando tarefa: {}", tarefa.getNome());

        // Simula lógica de tarefa
        executarLogicaTarefa(tarefa);

        // Garante que a execução dure ao menos o lockAtLeastFor
        long duracaoAtual = System.currentTimeMillis() - inicio;
        long minimoMillis = lockAtLeastFor.toMillis();

        if (duracaoAtual < minimoMillis) {
            long esperar = minimoMillis - duracaoAtual;
            log.info("Aguardando {}ms para cumprir lockAtLeastFor", esperar);
            Thread.sleep(esperar);
        }

    } catch (Exception e) {
        log.error("Erro na tarefa {}: {}", tarefa.getNome(), e.getMessage(), e);
    } finally {
        lock.unlock();
        log.info("Lock liberado para a tarefa: {}", tarefa.getNome());
    }
}

	private void executarLogicaTarefa(Scheduler tarefa) {
		log.info(null == tarefa.getNome() ? "Executando tarefa sem descrição"
				: "Executando tarefa: ".concat(tarefa.getNome()));
	}
}
