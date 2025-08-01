package br.com.paulistense.batch.notificacao.controller.v1.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchedulerRequest {
    private Long id;
    private String nome;
    private String descricao;
    private String ativo;
    private String cronExpression;
	private String lockAtMostFor;
	private String lockAtLeastFor;
	private SistemaRequest sistema;
	private UsuarioRequest usuario;
}
