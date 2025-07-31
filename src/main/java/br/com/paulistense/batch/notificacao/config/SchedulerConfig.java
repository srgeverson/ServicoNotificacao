package br.com.paulistense.batch.notificacao.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import br.com.paulistense.batch.notificacao.model.dto.SistemaDTO;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.core.SimpleLock;

@Configuration
@Slf4j
public class SchedulerConfig {
	@Autowired
	private SistemaDTO sistemaDTO;

	@Bean
	TaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(5);
		scheduler.setThreadNamePrefix(sistemaDTO.getNome());
		scheduler.initialize();
		return scheduler;
	}

	@Bean
	LockProvider lockProvider() {
		return (lockConfiguration) -> Optional.of(new SimpleLock() {
			@Override
			public void unlock() {
				log.info("// Nada a fazer aqui, pois está sendo controlado junto com a tabela do CRONs");
			}
		});
	}
}
