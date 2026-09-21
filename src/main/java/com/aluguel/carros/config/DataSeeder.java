package com.aluguel.carros.config;

import com.aluguel.carros.domain.Cliente;
import com.aluguel.carros.domain.Endereco;
import com.aluguel.carros.domain.EntidadeEmpregadora;
import com.aluguel.carros.domain.VinculoEmpregaticio;
import com.aluguel.carros.repository.ClienteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Popula alguns clientes de exemplo (dev/H2 ou prod/MySQL) para facilitar o
 * teste do CRUD pelo navegador. Só insere quando o banco está vazio.
 */
@Component
@Profile({"dev", "prod"})
public class DataSeeder implements CommandLineRunner {

    private final ClienteRepository repository;

    public DataSeeder(ClienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) {
            return;
        }

        Cliente ana = new Cliente();
        ana.setNome("Ana Souza");
        ana.setEmail("ana.souza@example.com");
        ana.setSenha("1234");
        ana.setRg("12.345.678-9");
        ana.setCpf("111.222.333-44");
        ana.setProfissao("Engenheira");
        ana.setEndereco(endereco("Rua das Flores", "100", "Belo Horizonte", "MG", "30110-000"));
        ana.adicionarVinculo(vinculo("Tech Solutions Ltda", "12.345.678/0001-90", 9500.0));
        ana.adicionarVinculo(vinculo("Consultoria XPTO", "98.765.432/0001-10", 2500.0));

        Cliente bruno = new Cliente();
        bruno.setNome("Bruno Lima");
        bruno.setEmail("bruno.lima@example.com");
        bruno.setSenha("1234");
        bruno.setRg("98.765.432-1");
        bruno.setCpf("555.666.777-88");
        bruno.setProfissao("Professor");
        bruno.setEndereco(endereco("Av. Central", "2000", "São Paulo", "SP", "01000-000"));
        bruno.adicionarVinculo(vinculo("Universidade Federal", "11.111.111/0001-11", 8000.0));

        repository.save(ana);
        repository.save(bruno);
    }

    private Endereco endereco(String logradouro, String numero, String cidade, String uf, String cep) {
        Endereco e = new Endereco();
        e.setLogradouro(logradouro);
        e.setNumero(numero);
        e.setCidade(cidade);
        e.setEstado(uf);
        e.setCep(cep);
        return e;
    }

    private VinculoEmpregaticio vinculo(String nome, String cnpj, double rendimento) {
        VinculoEmpregaticio v = new VinculoEmpregaticio();
        v.setEntidadeEmpregadora(new EntidadeEmpregadora(nome, cnpj));
        v.setRendimento(rendimento);
        return v;
    }
}
