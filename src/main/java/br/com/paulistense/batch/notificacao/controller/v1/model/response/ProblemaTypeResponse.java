package br.com.paulistense.batch.notificacao.controller.v1.model.response;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.paulistense.batch.notificacao.model.dto.SistemaDTO;
import lombok.Getter;

@Getter
public enum ProblemaTypeResponse {

	DADOS_INVALIDOS("/dados-invalidos", "Dados inválidos"),
	ACESSO_NEGADO("/acesso-negado", "Acesso negado"),
	ERRO_DE_SISTEMA("/erro-de-sistema", "Erro de sistema"),
	PARAMETRO_INVALIDO("/parametro-invalido", "Parâmetro inválido"),
	MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem incompreensível"),
	RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", "Recurso não encontrado"),
	ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso"),
	ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio");
	
	private String title;
	private String uri;
    @Autowired
    private SistemaDTO sistemaDTO;
	
	ProblemaTypeResponse(String path, String title) {
		this.uri = sistemaDTO.getUrl().concat(path);
		this.title = title;
	}
	
}
