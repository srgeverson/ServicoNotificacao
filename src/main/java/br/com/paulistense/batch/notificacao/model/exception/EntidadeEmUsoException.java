package br.com.paulistense.batch.notificacao.model.exception;

public class EntidadeEmUsoException extends NegocioException {
	private static final long serialVersionUID = 1L;
	private static final String MSG_ENTIDADE_EM_USO = "Já existe o cadastro %s com código %d";
	private static final String MSG_ENTIDADE_EM_USO_COM_NOME = "Já existe o cadastro %s com nome %s";
	private static final String MSG_ENTIDADE_EM_USO_COM_CHAVE = "Não existe ou está inativo o cadastro de %s com nome %s";

	public EntidadeEmUsoException(String mensagem) {
		super(mensagem);
	}

	public EntidadeEmUsoException(String name, Long id) {
		super(String.format(MSG_ENTIDADE_EM_USO,
				name.replace("br.com.paulistense.batch.envio.relatorio.domain.model.", ""), id));
	}

	public EntidadeEmUsoException(String name, String applicationName) {
		super(String.format(MSG_ENTIDADE_EM_USO_COM_NOME,
				name.replace("br.com.paulistense.batch.envio.relatorio.domain.model.", ""),
				applicationName.replace("Model", "")));
	}

	public EntidadeEmUsoException(Class<?> className, String id) {
		super(String.format(MSG_ENTIDADE_EM_USO_COM_CHAVE,
				className.getName().replace("br.com.paulistense.batch.envio.relatorio.domain.model.", ""),
				id));
	}
}