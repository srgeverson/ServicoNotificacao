package br.com.paulistense.batch.notificacao.controller.v1.model.response;

import lombok.Data;

@Data
public class SistemaResponse {
	private Integer id;
	private String descricao;
	private String nome;
	private String ativo;

}
