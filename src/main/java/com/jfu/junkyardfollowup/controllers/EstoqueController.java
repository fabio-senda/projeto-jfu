package com.jfu.junkyardfollowup.controllers;

import com.jfu.junkyardfollowup.models.Material;
import com.jfu.junkyardfollowup.repositories.MaterialRepository;
import jakarta.validation.Valid;
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
public class EstoqueController {
    @Autowired
    MaterialRepository materialRepository;
    @GetMapping("/adicionarMaterial")
    public ModelAndView telaAddMaterial(){
        ModelAndView mv = new ModelAndView("est-adicionar-material");
        mvObjetos(mv);
        mv.addObject("material", new Material());
        return mv;
    }

    @PostMapping("/adicionarMaterial")
    public ModelAndView addMaterial(@ModelAttribute("material") @Valid Material material,
                                    BindingResult result, RedirectAttributes redirect){
        ModelAndView mv = new ModelAndView("est-adicionar-material");
        mvObjetos(mv);
        if(result.hasErrors()){
            return mv;
        }
        materialRepository.save(material);
        return new ModelAndView("redirect:/jfu/est-adicionar-material");
    }

    @GetMapping("/consultarEstoque")
    public ModelAndView consultarEstoque(){
        ModelAndView mv = new ModelAndView("est-consultar-estoque");
        mvObjetos(mv);
        List<Material> materiais = materialRepository.findAll();
        mv.addObject("materiais", materiais);
        return mv;
    }

    private void mvObjetos(ModelAndView mv){
        mv.addObject("ativa", "estoque");
    }
}
