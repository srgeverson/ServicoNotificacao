package br.com.paulistense.batch.notificacao.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "USUARIOS")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuarios_seq")
	@SequenceGenerator(name = "usuarios_seq", sequenceName = "USUARIOS_SEQ", allocationSize = 1)
	@Column(name = "ID")
	private Long id;

	@Column(name = "NOME")
	private String nome;

	@Column(name = "CPF")
	private String cpf;

	@Column(name = "LOGIN", nullable = false)
	private String login;

	@Column(name = "SENHA")
	private String senha;

	@Column(name = "ULTIMO_ACESSO")
	private LocalDateTime ultimoAcesso;

	@Column(name = "IMAGEM")
	private String imagem;

	@Column(name = "ATIVO")
	private String ativo;

	@ManyToOne
	@JoinColumn(name = "USUARIO_ID", foreignKey = @ForeignKey(name = "FK_USUARIOS_USUARIO"))
	private Usuario usuarioPai;
}
