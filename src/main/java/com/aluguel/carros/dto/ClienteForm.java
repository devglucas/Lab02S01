package com.aluguel.carros.dto;

import com.aluguel.carros.domain.Cliente;
import com.aluguel.carros.domain.EntidadeEmpregadora;
import com.aluguel.carros.domain.Endereco;
import com.aluguel.carros.domain.VinculoEmpregaticio;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de transporte / backing bean do formulário de cliente.
 * Isola a camada Web das entidades JPA e concentra a validação de entrada.
 */
public class ClienteForm {

    private Long id;

    // ----- Usuario -----
    @NotBlank(message = "Informe o e-mail")
    @Email(message = "E-mail inválido")
    private String email;

    @NotBlank(message = "Informe a senha")
    @Size(min = 4, message = "A senha deve ter ao menos 4 caracteres")
    private String senha;

    // ----- Cliente -----
    @NotBlank(message = "Informe o nome")
    private String nome;

    @NotBlank(message = "Informe o RG")
    @Pattern(regexp = "\\d{2}\\.\\d{3}\\.\\d{3}(-[\\dxX])?",
            message = "RG deve estar no formato 00.000.000-0")
    private String rg;

    @NotBlank(message = "Informe o CPF")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}",
            message = "CPF deve estar no formato 000.000.000-00")
    private String cpf;

    @NotBlank(message = "Informe a profissão")
    private String profissao;

    // ----- Endereco -----
    @NotBlank(message = "Informe o logradouro")
    private String logradouro;

    @NotBlank(message = "Informe o número")
    private String numero;

    @NotBlank(message = "Informe a cidade")
    private String cidade;

    @NotBlank(message = "Informe o estado (UF)")
    @Size(min = 2, max = 2, message = "UF deve ter 2 letras")
    private String estado;

    @NotBlank(message = "Informe o CEP")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP deve estar no formato 00000-000")
    private String cep;

    // ----- Vínculos empregatícios (0..3) -----
    @Valid
    private List<VinculoForm> vinculos = new ArrayList<>();

    // ---------- Fábricas / mapeamento ----------

    /** Cria um form em branco já com uma linha de vínculo para o formulário. */
    public static ClienteForm novo() {
        ClienteForm f = new ClienteForm();
        f.vinculos.add(new VinculoForm());
        return f;
    }

    /** Constrói o form a partir de uma entidade existente (edição). */
    public static ClienteForm de(Cliente c) {
        ClienteForm f = new ClienteForm();
        f.id = c.getId();
        f.email = c.getEmail();
        f.senha = c.getSenha();
        f.nome = c.getNome();
        f.rg = c.getRg();
        f.cpf = c.getCpf();
        f.profissao = c.getProfissao();

        Endereco e = c.getEndereco();
        if (e != null) {
            f.logradouro = e.getLogradouro();
            f.numero = e.getNumero();
            f.cidade = e.getCidade();
            f.estado = e.getEstado();
            f.cep = e.getCep();
        }

        for (VinculoEmpregaticio v : c.getVinculos()) {
            VinculoForm vf = new VinculoForm();
            vf.setNomeEmpregador(v.getEntidadeEmpregadora().getNome());
            vf.setCnpj(v.getEntidadeEmpregadora().getCnpj());
            vf.setRendimento(v.getRendimento());
            f.vinculos.add(vf);
        }
        if (f.vinculos.isEmpty()) {
            f.vinculos.add(new VinculoForm());
        }
        return f;
    }

    /** Aplica os dados do form sobre uma entidade (nova ou existente). */
    public void aplicarEm(Cliente c) {
        c.setEmail(email);
        c.setSenha(senha);
        c.setNome(nome);
        c.setRg(rg);
        c.setCpf(cpf);
        c.setProfissao(profissao);

        Endereco e = c.getEndereco() != null ? c.getEndereco() : new Endereco();
        e.setLogradouro(logradouro);
        e.setNumero(numero);
        e.setCidade(cidade);
        e.setEstado(estado != null ? estado.toUpperCase() : null);
        e.setCep(cep);
        c.setEndereco(e);

        // Regrava a lista de vínculos (orphanRemoval cuida dos removidos).
        c.limparVinculos();
        for (VinculoForm vf : vinculos) {
            if (vf == null || vf.isVazio()) {
                continue;
            }
            VinculoEmpregaticio v = new VinculoEmpregaticio();
            v.setEntidadeEmpregadora(new EntidadeEmpregadora(vf.getNomeEmpregador(), vf.getCnpj()));
            v.setRendimento(vf.getRendimento());
            c.adicionarVinculo(v);
        }
    }

    /** Quantidade de vínculos preenchidos (não vazios). */
    public long vinculosPreenchidos() {
        return vinculos.stream().filter(v -> v != null && !v.isVazio()).count();
    }

    // ---------- getters / setters ----------

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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public List<VinculoForm> getVinculos() {
        return vinculos;
    }

    public void setVinculos(List<VinculoForm> vinculos) {
        this.vinculos = vinculos;
    }
}
