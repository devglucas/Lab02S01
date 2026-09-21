package com.aluguel.carros.controller;

import com.aluguel.carros.domain.Cliente;
import com.aluguel.carros.dto.ClienteForm;
import com.aluguel.carros.dto.VinculoForm;
import com.aluguel.carros.service.ClienteService;
import com.aluguel.carros.service.RegraNegocioException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador Web (camada de apresentação — "thin"). Traduz requisições HTTP em
 * chamadas ao {@link ClienteService} e escolhe as views Thymeleaf. Não acessa a
 * persistência diretamente, respeitando a direção Web → Serviços → Domínio.
 */
@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    /** Lista de clientes. */
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", service.listar());
        return "clientes/lista";
    }

    /** Formulário de novo cliente. */
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("clienteForm", ClienteForm.novo());
        model.addAttribute("modoEdicao", false);
        return "clientes/form";
    }

    /** Tela de detalhe. */
    @GetMapping("/{id}")
    public String detalhar(@PathVariable Long id, Model model) {
        Cliente cliente = service.buscarPorId(id);
        model.addAttribute("cliente", cliente);
        return "clientes/detalhe";
    }

    /** Formulário de edição preenchido. */
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Cliente cliente = service.buscarPorId(id);
        model.addAttribute("clienteForm", ClienteForm.de(cliente));
        model.addAttribute("modoEdicao", true);
        return "clientes/form";
    }

    /** Cria ou atualiza (o id no form decide). */
    @PostMapping
    public String salvar(@Valid @ModelAttribute("clienteForm") ClienteForm form,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirect) {
        boolean edicao = form.getId() != null;

        if (result.hasErrors()) {
            model.addAttribute("modoEdicao", edicao);
            return "clientes/form";
        }

        try {
            Cliente salvo = edicao
                    ? service.atualizar(form.getId(), form)
                    : service.criar(form);
            redirect.addFlashAttribute("mensagemSucesso",
                    "Cliente " + salvo.getNome() + " salvo com sucesso.");
            return "redirect:/clientes/" + salvo.getId();
        } catch (RegraNegocioException e) {
            // Regra de negócio violada: reexibe o form com a mensagem de erro.
            model.addAttribute("modoEdicao", edicao);
            model.addAttribute("mensagemErro", e.getMessage());
            return "clientes/form";
        }
    }

    /** Exclusão. */
    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        service.excluir(id);
        redirect.addFlashAttribute("mensagemSucesso", "Cliente excluído com sucesso.");
        return "redirect:/clientes";
    }

    /**
     * Garante ao menos uma linha de vínculo vazia ao reexibir o formulário com
     * erros (evita lista vazia quebrando a iteração no Thymeleaf).
     */
    @ModelAttribute
    public void prepararForm(@ModelAttribute("clienteForm") ClienteForm form) {
        if (form != null && form.getVinculos() != null && form.getVinculos().isEmpty()) {
            form.getVinculos().add(new VinculoForm());
        }
    }
}
