package com.jfu.junkyardfollowup.controllers;

import com.jfu.junkyardfollowup.models.Material;
import com.jfu.junkyardfollowup.repositories.MaterialRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/materiais")
public class MaterialController {
    @Autowired
    MaterialRepository materialRepository;

    @GetMapping
    public String listarTodosMateriais(Model model){
        model.addAttribute("materiais", materialRepository.findAll());
        return "";
    }

    @GetMapping("/cadastrar")
    public ModelAndView telaAdicionarMaterial(){
        ModelAndView mv = new ModelAndView("cadastrarmaterial.html");
        mv.addObject("material", new Material());
        return mv;
    }

    @PostMapping("/cadastrar/salvar")
    public String adicionarMaterial(@ModelAttribute("material") @Valid Material material,
                                    BindingResult resultado, RedirectAttributes redirect){
        if(resultado.hasErrors()){
            redirect.addFlashAttribute("mensagem",
                    "Verifique se os campos estão corretos");
            return "redirect:/materiais/cadastrar";
        }
        materialRepository.save(material);
        return "salvarmaterial.html";
    }
}
