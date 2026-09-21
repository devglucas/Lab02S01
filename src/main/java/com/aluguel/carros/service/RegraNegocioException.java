package com.aluguel.carros.service;

/**
 * Exceção de regra de negócio (violação de invariante do domínio).
 * Ex.: CPF duplicado, limite de vínculos excedido.
 */
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
