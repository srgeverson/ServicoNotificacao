package br.com.serviconotificacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.serviconotificacao.model.SistemaModel;
import br.com.serviconotificacao.repository.SistemaRepository;

@Service
public class SistemaService {

	@Autowired
	private SistemaRepository sistemaRepository;

	public SistemaModel consultarSistema(String nome) {
		return sistemaRepository.findByNome(nome);
	}
	
	public SistemaModel cadastrarSistema(SistemaModel sistema) {
		return sistemaRepository.save(sistema);
	}
}
