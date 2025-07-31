package br.com.paulistense.batch.notificacao.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.paulistense.batch.notificacao.model.dto.SistemaDTO;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {
	@Autowired
	private SistemaDTO sistemaDTO;

	@Bean
	OpenAPI openAPI() {
		return new OpenAPI().info(new Info().title("Batch Notificações").version("v".concat(sistemaDTO.getMajor()))
				.description(
						"O Batch de Noficações é responsável por enviar as notificações da empresa")
				.license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0.html")))
				.externalDocs(new ExternalDocumentation().description("Documentação completa do Batch Notificações")
						.url(sistemaDTO.getUrl().concat("/docs")));
	}
}