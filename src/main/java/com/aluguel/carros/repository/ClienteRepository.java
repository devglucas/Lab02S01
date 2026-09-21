package com.aluguel.carros.repository;

import com.aluguel.carros.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Camada de Persistência (Spring Data JPA) para {@link Cliente}.
 * Só é acessada pelos serviços de aplicação — nunca diretamente pelos controllers.
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByCpf(String cpf);

    boolean existsByCpf(String cpf);

    boolean existsByCpfAndIdNot(String cpf, Long id);
}
