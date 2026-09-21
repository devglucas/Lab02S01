package com.aluguel.carros;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada da aplicação (Subsistema Web / Servidor Central).
 * Sistema de Aluguel de Carros — entrega: CRUD de Cliente.
 */
@SpringBootApplication
public class AluguelCarrosApplication {

    public static void main(String[] args) {
        SpringApplication.run(AluguelCarrosApplication.class, args);
    }
}
