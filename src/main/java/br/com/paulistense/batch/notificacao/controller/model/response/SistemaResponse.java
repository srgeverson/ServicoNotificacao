package br.com.paulistense.batch.notificacao.controller.model.response;

import lombok.Data;

@Data
public class SistemaResponse {
	private Integer cdSistema;
	private Integer cdEmpresaGestora;
	private String cdSiglaSistema;
	private String dsSistema;
	private String ativo;

}
