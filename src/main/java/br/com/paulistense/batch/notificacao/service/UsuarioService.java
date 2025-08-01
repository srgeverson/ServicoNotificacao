package br.com.paulistense.batch.notificacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.paulistense.batch.notificacao.model.Usuario;
import br.com.paulistense.batch.notificacao.model.exception.EntidadeNaoEncontradaException;
import br.com.paulistense.batch.notificacao.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;

	public Usuario buscarUsuarioPorNome(String nome) {
		return repository.findByNome(nome)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(Usuario.class, nome));
	}

	public Usuario buscarUsuarioPorId(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(Usuario.class, id));
	}
}