package br.com.paulistense.batch.notificacao.controller.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.paulistense.batch.notificacao.controller.v1.model.response.SistemaResponse;
import br.com.paulistense.batch.notificacao.mapper.SistemaMapper;
import br.com.paulistense.batch.notificacao.model.dto.SistemaDTO;
import br.com.paulistense.batch.notificacao.model.exception.EntidadeNaoEncontradaException;
import br.com.paulistense.batch.notificacao.model.exception.NegocioException;

@RestController
@RequestMapping(path = "/v1/sistemas", produces = MediaType.APPLICATION_JSON_VALUE)
public class SistemaController {
	@Autowired
	private SistemaDTO sistemaDTO;
	@Autowired
	private SistemaMapper  sistemaMapper;

	@GetMapping("/health-check")
	public SistemaResponse healthCheck() {
		try {
			var response = sistemaMapper.toResponse(sistemaDTO);
			response.setAtivo(sistemaDTO.getAtivo().equals("S") ? "Sim" : "Não");
			return response;
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
}
