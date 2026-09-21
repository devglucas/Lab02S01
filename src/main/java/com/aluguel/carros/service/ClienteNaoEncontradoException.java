package com.aluguel.carros.service;

/** Lançada quando um cliente não é encontrado pelo id. */
public class ClienteNaoEncontradoException extends RuntimeException {

    public ClienteNaoEncontradoException(Long id) {
        super("Cliente não encontrado: " + id);
    }
}
