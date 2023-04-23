package br.com.serviconotificacao.model;

import java.time.OffsetDateTime;

import org.springframework.data.domain.AbstractAggregateRoot;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity(name = "usuarios")
public class UsuarioModel extends AbstractAggregateRoot<UsuarioModel> {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = true)
	private String nome;

	@Column(nullable = true)
	private String cpf;
	
	@Column(nullable = false)
	private String login;

	@Column(nullable = true)
	private String senha;
	
	@Column(nullable = true)
	private Boolean status;

	@Column(nullable = true, name = "ultimo_acesso")
	private OffsetDateTime ultimoAcesso;

	@ManyToOne
	@JoinColumn(nullable = true, name = "usuario_id")
	private UsuarioModel usuario;
}
