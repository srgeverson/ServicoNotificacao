package br.com.serviconotificacao.job;

import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.serviconotificacao.service.LogService;
import br.com.serviconotificacao.service.SistemaService;

@Component
public class SmsJob {

	@Autowired
	private SistemaService sistemaService;

	@Autowired
	private LogService logService;
	
	@Scheduled(fixedDelay = 10000)
	public void execute() throws InterruptedException{
		System.out.println("<---------------------------------------------------------------------------->");
		var sistema = sistemaService.consultarSistema("ServicoNotificacao");
		System.out.println(String.format("Enviando SMS -> %s, sistema -> %s, logs registrados -> %d", 
				LocalTime.now().toString(),
				sistema.getNome(),
				logService.consultarLogs().size()));
		System.out.println("<---------------------------------------------------------------------------->");
	}
}
