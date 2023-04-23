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
public class SmsJob {

	@Autowired
	@Qualifier("sistema")
	private SistemaModel sistema;

	@Autowired
	private LogService logService;

	@Scheduled(fixedDelay = 10000)
	public void execute() throws InterruptedException {
		//var sistema = sistemaService.consultarSistema("ServicoNotificacao");
		ConsoleUtil.mensagem("SMS", String.format("Enviando SMS -> %s, sistema -> %s, logs registrados -> %d",
				LocalTime.now().toString(), sistema.getNome(), logService.consultarLogs().size()));
	}
}
