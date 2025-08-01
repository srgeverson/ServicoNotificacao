package br.com.paulistense.batch.notificacao.controller.v1.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioResponse {
    private Integer id;
	private String nome;
	private String ativo;
}
