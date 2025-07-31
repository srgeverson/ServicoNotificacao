package br.com.paulistense.batch.notificacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.paulistense.batch.notificacao.model.Sistema;
import br.com.paulistense.batch.notificacao.model.exception.EntidadeNaoEncontradaException;
import br.com.paulistense.batch.notificacao.repository.SistemaRepository;

@Service
public class SistemaService {

	@Autowired
	private SistemaRepository repository;

	public Sistema buscarSistemaPorTitulo(String nome) {
		return repository.findByNome(nome)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(Sistema.class.getName()));
	}
}
