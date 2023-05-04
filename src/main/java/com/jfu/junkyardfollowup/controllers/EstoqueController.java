package com.jfu.junkyardfollowup.controllers;

import com.jfu.junkyardfollowup.dto.RequisicaoMaterial;
import com.jfu.junkyardfollowup.models.Material;
import com.jfu.junkyardfollowup.repositories.MaterialRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/materiais")
public class EstoqueController {
    @Autowired
    MaterialRepository materialRepository;
    @GetMapping("/add")
    public ModelAndView telaAddMaterial(){
        ModelAndView mv = new ModelAndView("est-adicionar-material");
        mvObjetos(mv);
        mv.addObject("material", new RequisicaoMaterial());
        mv.addObject("sucesso", false);
        return mv;
    }

    @PostMapping("/add")
    public ModelAndView addMaterial(@ModelAttribute("material") @Valid RequisicaoMaterial requisicaoMaterial,
                                    BindingResult result, RedirectAttributes redirect){
        ModelAndView mv = new ModelAndView("est-adicionar-material");
        mvObjetos(mv);
        if(result.hasErrors()){
            mv.addObject("material", requisicaoMaterial);
            return mv;
        }
        materialRepository.save(requisicaoMaterial.toMaterial());
        mv.addObject("material", new RequisicaoMaterial());
        mv.addObject("sucesso", true);
        return mv;
    }

    @GetMapping("/consulta")
    public ModelAndView consultarEstoque(@RequestParam(defaultValue = "") String searchKey){
        ModelAndView mv = new ModelAndView("est-consultar-estoque");
        mvObjetos(mv);
        List<Material> materiais = new ArrayList<>();
        if(!searchKey.equals("") && searchKey.matches("[+-]?\\d*(\\.\\d+)?")){
            Optional<Material> optional = materialRepository.findById(Long.parseLong(searchKey));
            if(optional.isPresent()){
                materiais.add(optional.get());
            }
        }else{
            materiais.addAll(materialRepository.findByNomeContainingIgnoreCase(searchKey));
        }
        mv.addObject("materiais", materiais);
        mv.addObject("status", "nenhum");
        return mv;
    }

    @GetMapping("/{id}edit")
    public ModelAndView edit(@PathVariable Long id){
        Optional<Material> optional = materialRepository.findById(id);
        if (optional.isPresent()) {
            Material material = optional.get();
            ModelAndView mv = new ModelAndView("est-atualizar-material");
            mvObjetos(mv);
            mv.addObject("materialId", material.getId());
            mv.addObject("material", material);
            return mv;
        }
        return new ModelAndView("redirect:/materiais/consulta");
    }

    @PostMapping("/{id}edit")
    public ModelAndView update(@PathVariable Long id, @ModelAttribute("material") @Valid RequisicaoMaterial requisicaoMaterial,
                               BindingResult result, RedirectAttributes redirect){
        ModelAndView mv = new ModelAndView("est-atualizar-material");
        mvObjetos(mv);
        mv.addObject("material", requisicaoMaterial);
        if(result.hasErrors()){
            return mv;
        }
        Optional<Material> optional = materialRepository.findById(id);
        if (optional.isEmpty()) {
            return mv;
        }
        Material material = optional.get();
        requisicaoMaterial.toMaterial(material); // altera nome e preco de material
        materialRepository.save(material);
        mv = new ModelAndView("est-consultar-estoque");
        mvObjetos(mv);
        List<Material> materiais = materialRepository.findAll();
        mv.addObject("materiais", materiais);
        mv.addObject("status", "atualizado");
        return mv;
    }

    @GetMapping("/{id}delete-confirm")
    public ModelAndView deleteConfirm(@PathVariable Long id){
        Optional<Material> optional = materialRepository.findById(id);
        if(optional.isPresent()){
            Material material = optional.get();
            ModelAndView mv = new ModelAndView("est-consultar-estoque");
            mvObjetos(mv);
            mv.addObject("status", "excluir");
            mv.addObject("materialNome", material.getNome());
            mv.addObject("materialId", material.getId());
            return mv;
        }
        return new ModelAndView("redirect:/materiais/consulta");
    }

    @GetMapping("/{id}delete")
    public ModelAndView delete(@PathVariable Long id) {
        Optional<Material> optional = materialRepository.findById(id);
        if(optional.isPresent()){
            materialRepository.delete(optional.get());
        }
        return new ModelAndView("redirect:/materiais/consulta");
    }

    private void mvObjetos(ModelAndView mv){
        mv.addObject("ativa", "estoque");
    }
}
