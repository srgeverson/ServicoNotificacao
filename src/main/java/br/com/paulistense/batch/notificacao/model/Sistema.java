package br.com.paulistense.batch.notificacao.model;

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
@Table(name = "SISTEMAS")
public class Sistema {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sistemas_seq")
	@SequenceGenerator(name = "sistemas_seq", sequenceName = "SISTEMAS_SEQ", allocationSize = 1)
	@Column(name = "ID")
	private Long id;

	@Column(name = "NOME", nullable = false)
	private String nome;

	@Column(name = "DESCRICAO")
	private String descricao;

	@Column(name = "ATIVO")
	private String Ativo;

	@ManyToOne
	@JoinColumn(name = "USUARIO_ID", foreignKey = @ForeignKey(name = "SISTEMAS_FK_USUARIO"))
	private Usuario usuario;
}
