package com.aluguel.carros.service;

import com.aluguel.carros.domain.Cliente;
import com.aluguel.carros.dto.ClienteForm;
import com.aluguel.carros.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço de aplicação — "Gestão de cadastro" (diagrama de pacotes).
 * Concentra as regras de negócio do CRUD de Cliente. Os controllers falam
 * apenas com esta camada; a persistência é acessada só aqui.
 *
 * Regras:
 *  - CPF único entre clientes;
 *  - no máximo {@link Cliente#MAX_VINCULOS} vínculos empregatícios por cliente.
 */
@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Cliente> listar() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException(id));
    }

    /**
     * Cria um novo cliente a partir do formulário, aplicando as regras de negócio.
     */
    @Transactional
    public Cliente criar(ClienteForm form) {
        validarRegras(form, null);
        Cliente cliente = new Cliente();
        form.aplicarEm(cliente);
        return repository.save(cliente);
    }

    /**
     * Atualiza um cliente existente.
     */
    @Transactional
    public Cliente atualizar(Long id, ClienteForm form) {
        Cliente cliente = buscarPorId(id);
        validarRegras(form, id);
        form.aplicarEm(cliente);
        return repository.save(cliente);
    }

    @Transactional
    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new ClienteNaoEncontradoException(id);
        }
        repository.deleteById(id);
    }

    // ---------- Regras de negócio ----------

    /**
     * Valida as invariantes do domínio. {@code idAtual} é nulo na criação e o id
     * do cliente na edição (para não conflitar o CPF com ele mesmo).
     */
    void validarRegras(ClienteForm form, Long idAtual) {
        // Máximo de 3 vínculos empregatícios.
        long qtdVinculos = form.vinculosPreenchidos();
        if (qtdVinculos > Cliente.MAX_VINCULOS) {
            throw new RegraNegocioException(
                    "Um cliente pode ter no máximo " + Cliente.MAX_VINCULOS
                            + " entidades empregadoras (informado: " + qtdVinculos + ").");
        }

        // CPF único.
        String cpf = form.getCpf();
        boolean duplicado = (idAtual == null)
                ? repository.existsByCpf(cpf)
                : repository.existsByCpfAndIdNot(cpf, idAtual);
        if (duplicado) {
            throw new RegraNegocioException("Já existe um cliente cadastrado com o CPF " + cpf + ".");
        }
    }
}
