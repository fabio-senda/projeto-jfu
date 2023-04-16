package com.jfu.junkyardfollowup.controllers;

import com.jfu.junkyardfollowup.dto.LoginDto;
import com.jfu.junkyardfollowup.models.Fornecimento;
import com.jfu.junkyardfollowup.models.RegistroDeCompra;
import com.jfu.junkyardfollowup.service.CatalogoMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/jfu")
public class MenuController {
    @Autowired
    private CatalogoMaterialService materialService;
    @GetMapping("/compra")
    public String getCompra(Model model){
        model.addAttribute("paginaAtiva", "compra");
        model.addAttribute("fornecimento", new Fornecimento());
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
