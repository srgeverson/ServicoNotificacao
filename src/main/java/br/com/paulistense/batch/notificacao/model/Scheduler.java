package br.com.paulistense.batch.notificacao.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "SCHEDULERS")
public class Scheduler {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scheduler_seq")
	@SequenceGenerator(name = "scheduler_seq", sequenceName = "SCHEDULERS_SEQ", allocationSize = 1)
	@Column(name = "ID")
	private Long id;
	@Column(name = "NOME", nullable = false, length = 100)
    private String nome;
    @Column(name = "DESCRICAO", length = 500)
    private String descricao;
    @Column(name = "ATIVO", nullable = false, length = 1)
    private String ativo;
    @Column(name = "CRON_EXPRESSION", nullable = false, length = 50)
    private String cronExpression;
	@Column(name = "LOCK_AT_MOST_FOR")
	private String lockAtMostFor;
	@Column(name = "LOCK_AT_LEAST_FOR")
	private String lockAtLeastFor;
	@Column(name = "ULTIMA_EXECUCAO")
	private LocalDateTime ultimaExecucao;
	@Column(name = "PROXIMA_EXECUCAO")
	private LocalDateTime proximaExecucao;
	@CreationTimestamp
	@Column(name = "CRIADO_EM", nullable = false, columnDefinition = "TIMESTAMP DEFAULT SYSTIMESTAMP")
	private LocalDateTime criadoEm;
	@UpdateTimestamp
	@Column(name = "ATUALIZADO_EM", nullable = false, columnDefinition = "TIMESTAMP DEFAULT SYSTIMESTAMP")
	private LocalDateTime atualizadoEm;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SISTEMA_ID", referencedColumnName = "ID")
	private Sistema sistema;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID")
	private Usuario usuario;
}
