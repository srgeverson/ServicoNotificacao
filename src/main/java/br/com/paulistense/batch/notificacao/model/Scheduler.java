package br.com.paulistense.batch.notificacao.model;

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
    @SequenceGenerator(
        name = "scheduler_seq",
        sequenceName = "SCHEDULERS_SEQ",
        allocationSize = 1
    )
	@Column(name = "ID")
	private Long id;
	@Column(name = "NOME")
	private String nome;
	@Column(name = "CRON_EXPR")
	private String cronExpr;
	@Column(name = "ATIVO")
	private String ativo;
	@Column(name = "LOCK_AT_MOST_FOR")
	private String lockAtMostFor;
	@Column(name = "LOCK_AT_LEAST_FOR")
	private String lockAtLeastFor;
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SISTEMA_ID", referencedColumnName = "ID")
    private Sistema sistema;
}
