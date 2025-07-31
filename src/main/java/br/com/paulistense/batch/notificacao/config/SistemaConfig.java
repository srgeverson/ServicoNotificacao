package br.com.paulistense.batch.notificacao.config;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.paulistense.batch.notificacao.mapper.SistemaMapper;
import br.com.paulistense.batch.notificacao.model.dto.SistemaDTO;
import br.com.paulistense.batch.notificacao.service.SistemaService;
import lombok.Data;

@Configuration	
@ConfigurationProperties(prefix = "br.com.paulistense.batch.notificacao")
@Data
public class SistemaConfig {
	private String url;

	private String nome;

	private String versao;
	
	private String porta;

	@Autowired
	private SistemaService sistemaSegService;
	
	@Autowired
	private SistemaMapper sistemaMapper;

	@Bean
	SistemaDTO buscarSistema() throws AccountNotFoundException {
		var sistema = sistemaSegService.buscarSistemaPorTitulo(nome);
		var sistemaDTO = sistemaMapper.toDTO(sistema);
		if (url != null&& !url.isEmpty() && porta != null && !porta.isEmpty())
			url = url.concat(":").concat(porta);
		sistemaDTO.setUrl(url);
		sistemaDTO.setPorta(porta);
		sistemaDTO.setVersao(versao);
		return sistemaDTO;
	}
}
