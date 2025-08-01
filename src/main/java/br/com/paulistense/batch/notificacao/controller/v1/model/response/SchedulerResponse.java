package br.com.paulistense.batch.notificacao.controller.v1.model.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchedulerResponse {
    private Long id;
    private String nome;
    private String descricao;
    private String ativo;
    private String cronExpression;
	private String lockAtMostFor;
	private String lockAtLeastFor;
	private LocalDateTime ultimaExecucao;
	private LocalDateTime proximaExecucao;
	private LocalDateTime criadoEm;
	private LocalDateTime atualizadoEm;
	private SistemaResponse sistema;
	private UsuarioResponse usuario;
}
