package br.com.paulistense.batch.notificacao.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SistemaDTO {
	private Long id;
	private String descricao;
	private String nome;
	private String ativo;
	private String url;
	private String porta;
	private String versao;
	private String major;

	public String getMajor() {
		var versaoes = versao.split("\\.");
		if (versaoes.length > 0)
			major = versaoes[0];
		return major;
	}
}
