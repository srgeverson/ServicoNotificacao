package br.com.paulistense.batch.notificacao.model.exception;

public class EntidadeEmUsoException extends NegocioException {
	private static final long serialVersionUID = 1L;
	private static final String MSG_ENTIDADE_EM_USO = "Já existe o cadastro '%s' com código '%d'";
	private static final String MSG_ENTIDADE_EM_USO_COM_CHAVE = "Já existe o cadastro '%s' com nome '%s'";

	public EntidadeEmUsoException(String mensagem) {
		super(mensagem);
	}

	public EntidadeEmUsoException(Class<?> className, Long id) {
		super(String.format(MSG_ENTIDADE_EM_USO,
				className.getName().replace("br.com.paulistense.batch.notificacao.model.", ""), id));
	}

	public EntidadeEmUsoException(Class<?> className, String id) {
		super(String.format(MSG_ENTIDADE_EM_USO_COM_CHAVE,
				className.getName().replace("br.com.paulistense.batch.notificacao.model.", ""),
				id));
	}
}