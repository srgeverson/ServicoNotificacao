package br.com.serviconotificacao.model;

import java.time.OffsetDateTime;

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
@Entity(name = "logs")
public class LogModel {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = true, name = "id_tabela")
	private Long idTabela;
	
	@Column(nullable = true, name = "nome_tabela")
	private String nomeTabela;

	@Column(nullable = true, name = "campo_modificado")
	private String campoModificado;
	
	@Column(nullable = true, name = "valor_antigo")
	private String valorAntigo;

	@Column(nullable = true, name = "valor_atual")
	private String valorAtual;
	
	@Column(nullable = true, name = "data_operacao")
	private OffsetDateTime dataOperacao;

	@ManyToOne
	@JoinColumn(nullable = true, name = "usuario_id")
	private UsuarioModel usuario;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdTabela() {
		return idTabela;
	}

	public void setIdTabela(Long idTabela) {
		this.idTabela = idTabela;
	}

	public String getNomeTabela() {
		return nomeTabela;
	}

	public void setNomeTabela(String nomeTabela) {
		this.nomeTabela = nomeTabela;
	}

	public String getCampoModificado() {
		return campoModificado;
	}

	public void setCampoModificado(String campoModificado) {
		this.campoModificado = campoModificado;
	}

	public String getValorAntigo() {
		return valorAntigo;
	}

	public void setValorAntigo(String valorAntigo) {
		this.valorAntigo = valorAntigo;
	}

	public String getValorAtual() {
		return valorAtual;
	}

	public void setValorAtual(String valorAtual) {
		this.valorAtual = valorAtual;
	}

	public OffsetDateTime getDataOperacao() {
		return dataOperacao;
	}

	public void setDataOperacao(OffsetDateTime dataOperacao) {
		this.dataOperacao = dataOperacao;
	}

	public UsuarioModel getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioModel usuario) {
		this.usuario = usuario;
	}
}
