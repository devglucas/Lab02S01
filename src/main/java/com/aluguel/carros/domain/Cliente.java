package com.aluguel.carros.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Cliente do sistema — subclasse de {@link Usuario} (herança JOINED).
 * Atributos próprios (diagrama de classes): rg, cpf, nome, profissao.
 * Relações: 1:1 com {@link Endereco} e 0..3 vínculos empregatícios.
 *
 * Os métodos de pedido (criarPedido, etc.) estão fora do escopo desta entrega.
 */
@Entity
@Table(name = "cliente")
public class Cliente extends Usuario {

    @Column(nullable = false, length = 20)
    private String rg;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, length = 80)
    private String profissao;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
    @JoinColumn(name = "endereco_id", nullable = false)
    private Endereco endereco;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VinculoEmpregaticio> vinculos = new ArrayList<>();

    public Cliente() {
    }

    /** Máximo de vínculos empregatícios permitidos por cliente. */
    public static final int MAX_VINCULOS = 3;

    /** Adiciona um vínculo mantendo os dois lados da associação sincronizados. */
    public void adicionarVinculo(VinculoEmpregaticio vinculo) {
        vinculo.setCliente(this);
        this.vinculos.add(vinculo);
    }

    /** Remove todos os vínculos (usado ao regravar a lista na edição). */
    public void limparVinculos() {
        this.vinculos.clear();
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public List<VinculoEmpregaticio> getVinculos() {
        return vinculos;
    }

    public void setVinculos(List<VinculoEmpregaticio> vinculos) {
        this.vinculos = vinculos;
    }
}
