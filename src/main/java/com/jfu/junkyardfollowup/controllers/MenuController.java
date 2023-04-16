package com.jfu.junkyardfollowup.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/jfu")
public class MenuController {
    @GetMapping("/compra")
    public String getCompra(Model model){
        model.addAttribute("paginaAtiva", "compra");
        return "menu";
    }
    @GetMapping("/venda")
    public String getVenda(Model model){
        model.addAttribute("paginaAtiva", "venda");
        return "menu";
    }
    @GetMapping("/estoque")
    public String getEstoque(Model model){
        model.addAttribute("paginaAtiva", "estoque");
        return "menu";
    }
}
