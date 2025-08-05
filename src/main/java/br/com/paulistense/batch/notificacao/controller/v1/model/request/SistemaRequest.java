package br.com.paulistense.batch.notificacao.controller.v1.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class SistemaRequest {
    @NotNull(message = "Id é obrigatório")
    @Positive(message = "Id deve ser maior que zero")
    private Long id;
}
