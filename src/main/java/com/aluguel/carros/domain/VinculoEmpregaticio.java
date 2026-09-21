package com.aluguel.carros.domain;

import jakarta.persistence.*;

/**
 * Classe de associação entre {@link Cliente} e {@link EntidadeEmpregadora}
 * (diagrama de classes: "(Cliente, EntidadeEmpregadora) .. VinculoEmpregaticio").
 *
 * Carrega o atributo próprio da associação: o <b>rendimento</b> auferido pelo
 * cliente naquela entidade empregadora. Regra: no máximo 3 vínculos por cliente
 * (garantida no serviço).
 */
@Entity
@Table(name = "vinculo_empregaticio")
public class VinculoEmpregaticio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    /**
     * A entidade empregadora é própria de cada vínculo (cascade ALL): ao remover
     * um vínculo do cliente, sua entidade empregadora associada também é removida.
     */
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
    @JoinColumn(name = "entidade_empregadora_id", nullable = false)
    private EntidadeEmpregadora entidadeEmpregadora;

    /** Atributo da classe de associação. */
    @Column(nullable = false)
    private double rendimento;

    public VinculoEmpregaticio() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public EntidadeEmpregadora getEntidadeEmpregadora() {
        return entidadeEmpregadora;
    }

    public void setEntidadeEmpregadora(EntidadeEmpregadora entidadeEmpregadora) {
        this.entidadeEmpregadora = entidadeEmpregadora;
    }

    public double getRendimento() {
        return rendimento;
    }

    public void setRendimento(double rendimento) {
        this.rendimento = rendimento;
    }
}
