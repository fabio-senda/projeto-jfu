package com.jfu.junkyardfollowup.controllers;

import com.jfu.junkyardfollowup.dto.FornecimentoDto;
import com.jfu.junkyardfollowup.dto.RegistroDeCompraDto;
import com.jfu.junkyardfollowup.models.Fornecimento;
import com.jfu.junkyardfollowup.models.Material;
import com.jfu.junkyardfollowup.models.RegistroDeCompra;
import com.jfu.junkyardfollowup.service.CatalogoMaterialService;
import com.jfu.junkyardfollowup.service.FornecimentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/jfu")
public class CompraController {
    @Autowired
    CatalogoMaterialService materialService;
    @Autowired
    FornecimentoService fornecimentoService;

    @GetMapping("/adicionarCompra")
    public ModelAndView addCompra(){
        ModelAndView mv = new ModelAndView("menu");
        mv.addObject("paginaAtiva", "compra");
        mv.addObject("aba", "registrar");
        mv.addObject("compra", new RegistroDeCompraDto());
        mv.addObject("materiais", materialService.listarMateriais());
        return mv;
    }

    @PostMapping("/adicionarCompra")
    public String addCompra(@ModelAttribute("compra") RegistroDeCompraDto compra,
                            BindingResult result, RedirectAttributes redirect, Model model){
        for (FornecimentoDto fornecimento : compra.getFornecimentos()) {
            System.out.println(fornecimento.getQuantidade() + " " + fornecimento.getIdMaterial()
                                + " " + fornecimento.getPrecoDeCompra());
            fornecimentoService.adicionarAtributosDto(fornecimento);
        }
        model.addAttribute("compra", compra);
        model.addAttribute("paginaAtiva", "compra");
        model.addAttribute("aba", "registrar");
        model.addAttribute("materiais", materialService.listarMateriais());
        return "menu";
    }
    @PostMapping("/finalizarCompra")
    public String finalizarCompra(@ModelAttribute("compra") RegistroDeCompraDto compra,
                                  RedirectAttributes redirect, Model model){
        model.addAttribute("paginaAtiva", "compra");
        model.addAttribute("aba", "registrar");
        model.addAttribute("compra", new RegistroDeCompra());
        model.addAttribute("materiais", materialService.listarMateriais());
        return "menu";
    }
}
