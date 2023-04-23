package br.com.serviconotificacao.job;

import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.serviconotificacao.model.SistemaModel;
import br.com.serviconotificacao.service.LogService;
import br.com.serviconotificacao.util.ConsoleUtil;

@Component
public class EmailJob {

	@Autowired
	@Qualifier("sistema")
	private SistemaModel sistema;

	@Autowired
	private LogService logService;

	@Scheduled(fixedDelay = 5000)
	public void execute() throws InterruptedException {

		ConsoleUtil.mensagem("E-mail", String.format("Enviando E-mail -> %s, sistema -> %s, logs registrados -> %d",
				LocalTime.now().toString(), sistema.getNome(), logService.consultarLogs().size()));
	}
}
