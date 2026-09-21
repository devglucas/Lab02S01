package com.aluguel.carros.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** Redireciona a raiz para a listagem de clientes. */
@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/clientes";
    }
}
