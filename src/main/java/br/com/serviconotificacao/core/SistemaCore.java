package br.com.serviconotificacao.core;

import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.serviconotificacao.model.SistemaModel;
import br.com.serviconotificacao.service.SistemaService;
import br.com.serviconotificacao.util.ConsoleUtil;

@Configuration
public class SistemaCore {

	@Autowired
	private SistemaService sistemaService;

	@Bean //(initMethod = "init", destroyMethod = "cleanup")
	public SistemaModel sistema() {
		var sistema = sistemaService.consultarSistema("ServicoNotificacaoJava");
		if (sistema == null) {
			sistema = new SistemaModel();
			sistema.setNome("ServicoNotificacaoJava");
			sistema.setDescricao("Serviço de Envio de Notificação Java");
			sistema.setStatus(true);
			// sistema.setUsuario(null);
			sistemaService.cadastrarSistema(sistema);
			ConsoleUtil.mensagem("Inicialização do sistema", String.format("Cadastrando sistema -> %s, sistema -> %s",
					LocalTime.now().toString(), sistema.getNome()));
		} else if (!sistema.getStatus()) {
			sistemaService.cadastrarSistema(sistema);
			ConsoleUtil.mensagem("Inicialização do sistema", String.format(
					"Sistema não está ativo -> %s, sistema -> %s", LocalTime.now().toString(), sistema.getNome()));
		} else {
			ConsoleUtil.mensagem("Inicialização do sistema",
					String.format("Sistema ativo -> %s, sistema -> %s", LocalTime.now().toString(), sistema.getNome()));

		}
		return sistema;
	}

//	@PostConstruct
//	public void init() {
//		ConsoleUtil.mensagem("init", "sistema carregado!");
//	}
}
