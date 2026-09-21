package com.aluguel.carros.domain;

import jakarta.persistence.*;

/**
 * Classe abstrata do domínio (pacote Usuários).
 * Representa a superclasse da hierarquia do diagrama de classes: Usuario <|-- Cliente.
 *
 * Estratégia de herança JOINED: cada subclasse ganha sua própria tabela,
 * ligada à tabela "usuario" pela PK — refletindo fielmente a generalização do modelo.
 */
@Entity
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(nullable = false, length = 120)
    private String senha;

    protected Usuario() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
