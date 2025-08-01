package br.com.paulistense.batch.notificacao.model.exception;

public class EntidadeNaoEncontradaException extends NegocioException {
	private static final long serialVersionUID = 1L;
	private static final String MSG_ENTIDADE_NAO_ENCONTRADA = "Não existe ou está inativo o cadastro de %s com código %d";
	private static final String MSG_ENTIDADE_NAO_ENCONTRADA_COM_CHAVE = "Não existe ou está inativo o cadastro de %s com identificador %s";

	public EntidadeNaoEncontradaException(String mensagem) {
		super(mensagem);
	}

	public EntidadeNaoEncontradaException(Class<?> classType, Long id) {
		super(String.format(MSG_ENTIDADE_NAO_ENCONTRADA,
				classType.getName().replace("br.com.paulistense.batch.envio.relatorio.domain.model.", ""), id));
	}

	public EntidadeNaoEncontradaException(Class<?> classType, String id) {
		super(String.format(MSG_ENTIDADE_NAO_ENCONTRADA_COM_CHAVE,
				classType.getName().replace("br.com.paulistense.batch.envio.relatorio.domain.model.", ""),
				id));
	}
}