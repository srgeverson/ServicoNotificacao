package br.com.paulistense.batch.notificacao.controller.v1.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SchedulerRequest {
    private Long id;
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String descricao;
    @Pattern(regexp = "S|N", message = "Ativo deve ser 'S' ou 'N'")
    private String ativo;
    @NotBlank(message = "Expressão CRON é obrigatória")
    private String cronExpression;
    @Pattern(regexp = "(\\d+[SMHsmh])", message = "lockAtMostFor deve seguir o formato, exemplo: '5M', '10s'")
	private String lockAtMostFor;
    @Pattern(regexp = "(\\d+[SMHsmh])", message = "lockAtLeastFor deve seguir o formato, exemplo: '5M', '10s'")
	private String lockAtLeastFor;
    @NotNull(message = "Sistema é obrigatório")
	private SistemaRequest sistema;
    @NotNull(message = "Usuário é obrigatório")
	private UsuarioRequest usuario;
}
