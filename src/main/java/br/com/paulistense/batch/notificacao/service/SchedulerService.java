package br.com.paulistense.batch.notificacao.service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;
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
    private SchedulerRepository repository;

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private LockProvider lockProvider;

    @Autowired
    private SistemaDTO sistemaDTO;

    private final Map<String, ScheduledFuture<?>> tarefasAgendadas = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        carregarTarefas();
    }

    public void carregarTarefas() {
        List<Scheduler> tarefas = repository.findByAtivoAndSistemaId("S", sistemaDTO.getId());

        log.info("Cancelando tarefas existentes...");
        tarefasAgendadas.values().forEach(future -> future.cancel(false));
        tarefasAgendadas.clear();

        if (tarefas.isEmpty()) {
            log.info("Nenhuma tarefa ativa para o sistema: {}", sistemaDTO.getDescricao());
        } else {
            log.info("Agendando {} tarefas ativas", tarefas.size());
            tarefas.forEach(this::agendarTarefa);
        }
    }

    public void agendarTarefa(Scheduler tarefa) {
        Runnable job = () -> executarTarefa(tarefa);
        CronTrigger trigger = new CronTrigger(tarefa.getCronExpression());
        ScheduledFuture<?> future = taskScheduler.schedule(job, trigger);
        tarefasAgendadas.put(tarefa.getNome(), future);

        log.info("// Calcular próxima execução (opcional, pois TriggerContext não é persistente)");
        try {
            SimpleTriggerContext triggerContext = new SimpleTriggerContext();
            Date nextExecution = trigger.nextExecutionTime(triggerContext);
            if (nextExecution != null) {
                tarefa.setProximaExecucao(nextExecution.toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDateTime());
                repository.save(tarefa);
            }
        } catch (Exception e) {
            log.warn("Erro ao calcular próxima execução para tarefa: {}", tarefa.getNome(), e);
        }

        log.info("Tarefa '{}' agendada com cron '{}'", tarefa.getNome(), tarefa.getCronExpression());
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

            log.info("// Atualiza data de execução");
            tarefa.setUltimaExecucao(LocalDateTime.now());
            repository.save(tarefa);

            executarLogicaTarefa(tarefa);

            log.info("// Garantir execução mínima");
            long duracaoAtual = System.currentTimeMillis() - inicio;
            long minimoMillis = lockAtLeastFor.toMillis();

            if (duracaoAtual < minimoMillis)
                Thread.sleep(minimoMillis - duracaoAtual);

        } catch (Exception e) {
            log.error("Erro na execução da tarefa '{}': {}", tarefa.getNome(), e.getMessage(), e);
        } finally {
            lock.unlock();
            log.info("Lock liberado para tarefa: {}", tarefa.getNome());
        }
    }

    private void executarLogicaTarefa(Scheduler tarefa) {
        log.info("Lógica customizada para tarefa: {}", tarefa.getNome());

        switch (tarefa.getNome()) {
            default:
                log.warn("Tarefa '{}' ainda não tem lógica associada", tarefa.getNome());
        }
    }

    // Métodos públicos para atualização via Controller
    public void atualizarCron(String nome, String novaExpressao) {
        Scheduler tarefa = repository.findByNomeAndSistemaId(nome, sistemaDTO.getId())
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        tarefa.setCronExpression(novaExpressao);
        repository.save(tarefa);
        recarregarTarefa(nome);
    }

    public void ativarOuDesativar(String nome, boolean ativar) {
        Scheduler tarefa = repository.findByNomeAndSistemaId(nome, sistemaDTO.getId())
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        tarefa.setAtivo(ativar ? "S" : "N");
        if (!ativar) {
            tarefa.setProximaExecucao(null);
        }
        repository.save(tarefa);

        if (ativar) recarregarTarefa(nome);
        else cancelarTarefa(nome);
    }

    private void recarregarTarefa(String nome) {
        cancelarTarefa(nome);
        repository.findByNomeAndSistemaId(nome, sistemaDTO.getId())
                .ifPresent(this::agendarTarefa);
    }

    private void cancelarTarefa(String nome) {
        ScheduledFuture<?> future = tarefasAgendadas.remove(nome);
        if (future != null) future.cancel(false);
    }

	public Optional<Scheduler> buscarPorNome(String name) {
		return repository.findByNomeAndSistemaId(name, sistemaDTO.getId());
	}

    public Scheduler salvar(Scheduler config) {
        return repository.save(config);
    }

    public List<Scheduler> buscarTodos() {
        return repository.findBySistemaId(sistemaDTO.getId());
    }
}
