package br.com.paulistense.batch.notificacao.model.exception;

public class EntidadeNaoEncontradaException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private static final String MSG_ENTIDADE_NAO_ENCONTRADA = "Não existe ou está inativo o cadastro de %s com código %d";
	private static final String MSG_ENTIDADE_NAO_ENCONTRADA_COM_NOME = "Não existe ou está inativo o cadastro de %s com nome %s";

	public EntidadeNaoEncontradaException(String mensagem) {
		super(mensagem);
	}

	public EntidadeNaoEncontradaException(String name, Long id) {
		super(String.format(MSG_ENTIDADE_NAO_ENCONTRADA,
				name.replace("br.com.paulistense.batch.envio.relatorio.domain.model.", ""), id));
	}

	public EntidadeNaoEncontradaException(String name, String applicationName) {
		super(String.format(MSG_ENTIDADE_NAO_ENCONTRADA_COM_NOME,
				name.replace("br.com.paulistense.batch.envio.relatorio.domain.model.", ""),
				applicationName.replace("Model", "")));
	}
}