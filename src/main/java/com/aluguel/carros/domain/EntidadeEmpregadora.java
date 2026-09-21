package com.aluguel.carros.domain;

import jakarta.persistence.*;

/**
 * Entidade empregadora (empresa onde o cliente trabalha): nome e cnpj.
 * Ligada ao Cliente pela classe de associação {@link VinculoEmpregaticio}.
 */
@Entity
@Table(name = "entidade_empregadora")
public class EntidadeEmpregadora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, length = 18)
    private String cnpj;

    public EntidadeEmpregadora() {
    }

    public EntidadeEmpregadora(String nome, String cnpj) {
        this.nome = nome;
        this.cnpj = cnpj;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}
