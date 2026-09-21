package com.aluguel.carros.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Dados de um vínculo empregatício no formulário:
 * a entidade empregadora (nome, cnpj) e o rendimento auferido.
 */
public class VinculoForm {

    @NotBlank(message = "Informe o nome da entidade empregadora")
    private String nomeEmpregador;

    @NotBlank(message = "Informe o CNPJ")
    private String cnpj;

    @PositiveOrZero(message = "O rendimento deve ser zero ou positivo")
    private double rendimento;

    public String getNomeEmpregador() {
        return nomeEmpregador;
    }

    public void setNomeEmpregador(String nomeEmpregador) {
        this.nomeEmpregador = nomeEmpregador;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public double getRendimento() {
        return rendimento;
    }

    public void setRendimento(double rendimento) {
        this.rendimento = rendimento;
    }

    /** True quando a linha está totalmente vazia (deve ser ignorada). */
    public boolean isVazio() {
        return (nomeEmpregador == null || nomeEmpregador.isBlank())
                && (cnpj == null || cnpj.isBlank())
                && rendimento == 0d;
    }
}
