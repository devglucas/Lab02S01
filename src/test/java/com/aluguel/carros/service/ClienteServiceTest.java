package com.aluguel.carros.service;

import com.aluguel.carros.domain.Cliente;
import com.aluguel.carros.dto.ClienteForm;
import com.aluguel.carros.dto.VinculoForm;
import com.aluguel.carros.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários das regras de negócio do {@link ClienteService}
 * (repositório mockado, sem contexto Spring nem banco).
 */
@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository repository;

    @InjectMocks
    private ClienteService service;

    private ClienteForm formValido;

    @BeforeEach
    void setUp() {
        formValido = new ClienteForm();
        formValido.setEmail("cliente@example.com");
        formValido.setSenha("1234");
        formValido.setNome("Cliente Teste");
        formValido.setRg("11.111.111-1");
        formValido.setCpf("123.456.789-00");
        formValido.setProfissao("Analista");
        formValido.setLogradouro("Rua A");
        formValido.setNumero("10");
        formValido.setCidade("Cidade");
        formValido.setEstado("MG");
        formValido.setCep("30000-000");
    }

    private VinculoForm vinculo(String nome) {
        VinculoForm v = new VinculoForm();
        v.setNomeEmpregador(nome);
        v.setCnpj("00.000.000/0001-00");
        v.setRendimento(1000.0);
        return v;
    }

    @Test
    void criar_comDadosValidos_persisteCliente() {
        when(repository.existsByCpf(formValido.getCpf())).thenReturn(false);
        when(repository.save(any(Cliente.class))).thenAnswer(inv -> inv.getArgument(0));

        Cliente salvo = service.criar(formValido);

        assertThat(salvo.getNome()).isEqualTo("Cliente Teste");
        assertThat(salvo.getEndereco()).isNotNull();
        assertThat(salvo.getEndereco().getEstado()).isEqualTo("MG");
        verify(repository).save(any(Cliente.class));
    }

    @Test
    void criar_comCpfDuplicado_lancaRegraNegocio() {
        when(repository.existsByCpf(formValido.getCpf())).thenReturn(true);

        assertThatThrownBy(() -> service.criar(formValido))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessageContaining("CPF");

        verify(repository, never()).save(any());
    }

    @Test
    void criar_comMaisDeTresVinculos_lancaRegraNegocio() {
        formValido.getVinculos().add(vinculo("Empresa 1"));
        formValido.getVinculos().add(vinculo("Empresa 2"));
        formValido.getVinculos().add(vinculo("Empresa 3"));
        formValido.getVinculos().add(vinculo("Empresa 4")); // 4 -> excede o limite

        assertThatThrownBy(() -> service.criar(formValido))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessageContaining("máximo");

        verify(repository, never()).save(any());
        // Não deve nem consultar CPF: a validação de vínculos vem antes.
        verify(repository, never()).existsByCpf(any());
    }

    @Test
    void criar_comExatamenteTresVinculos_persiste() {
        formValido.getVinculos().add(vinculo("Empresa 1"));
        formValido.getVinculos().add(vinculo("Empresa 2"));
        formValido.getVinculos().add(vinculo("Empresa 3"));
        when(repository.existsByCpf(formValido.getCpf())).thenReturn(false);
        when(repository.save(any(Cliente.class))).thenAnswer(inv -> inv.getArgument(0));

        Cliente salvo = service.criar(formValido);

        assertThat(salvo.getVinculos()).hasSize(3);
    }

    @Test
    void atualizar_ignoraProprioCpf() {
        Cliente existente = new Cliente();
        existente.setId(7L);
        when(repository.findById(7L)).thenReturn(Optional.of(existente));
        when(repository.existsByCpfAndIdNot(formValido.getCpf(), 7L)).thenReturn(false);
        when(repository.save(any(Cliente.class))).thenAnswer(inv -> inv.getArgument(0));

        Cliente atualizado = service.atualizar(7L, formValido);

        assertThat(atualizado.getNome()).isEqualTo("Cliente Teste");
        verify(repository).existsByCpfAndIdNot(formValido.getCpf(), 7L);
    }

    @Test
    void buscarPorId_inexistente_lancaNaoEncontrado() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(99L))
                .isInstanceOf(ClienteNaoEncontradoException.class);
    }

    @Test
    void excluir_inexistente_lancaNaoEncontrado() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.excluir(99L))
                .isInstanceOf(ClienteNaoEncontradoException.class);
        verify(repository, never()).deleteById(any());
    }
}
