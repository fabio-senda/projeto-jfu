package com.jfu.junkyardfollowup.controllers;

import com.jfu.junkyardfollowup.dtos.RequisicaoMaterial;
import com.jfu.junkyardfollowup.models.Material;
import com.jfu.junkyardfollowup.repositories.MaterialRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    @GetMapping
    public ModelAndView consultarEstoque(@RequestParam(defaultValue = "") String searchKey){
        return mvConsultarAddObjetos(searchKey,0l,"nenhum","nenhum","nenhum");
    }
    @GetMapping("/add")
    public ModelAndView telaAddMaterial(){
        return mvAdicionarAddObjetos(false, new RequisicaoMaterial());
    }

    @PostMapping("/add")
    public ModelAndView addMaterial(@ModelAttribute("material") @Valid RequisicaoMaterial requisicaoMaterial,
                                    BindingResult result, RedirectAttributes redirect){
        if(result.hasErrors()){
            return mvAdicionarAddObjetos(false, requisicaoMaterial);
        }
        materialRepository.save(requisicaoMaterial.toMaterial());
        return mvAdicionarAddObjetos(true, new RequisicaoMaterial());
    }

    @GetMapping("/{id}edit")
    public ModelAndView edit(@PathVariable Long id){
        Optional<Material> optional = materialRepository.findById(id);
        if (optional.isPresent()) {
            Material material = optional.get();
            return mvAtualizarAddObjetos(false, new RequisicaoMaterial(material), material.getId());
        }
        return new ModelAndView("redirect:/materiais");
    }

    @PostMapping("/{id}edit")
    public ModelAndView update(@PathVariable Long id, @ModelAttribute("material") @Valid RequisicaoMaterial requisicaoMaterial,
                               BindingResult result, RedirectAttributes redirect){
        Optional<Material> optional = materialRepository.findById(id);
        if (result.hasErrors()) {
            return mvAtualizarAddObjetos(false, requisicaoMaterial, id);
        }
        if (optional.isEmpty()) {
            return mvAtualizarAddObjetos(true, requisicaoMaterial, id);
        }
        Material material = optional.get();
        materialRepository.save(requisicaoMaterial.toMaterial(material));
        return mvConsultarAddObjetos("", 0l, "mensagem","Material atualizado com sucesso", "nenhum");
    }

    @GetMapping("/{id}delete-confirm")
    public ModelAndView deleteConfirm(@PathVariable Long id){
        Optional<Material> optional = materialRepository.findById(id);
        if(optional.isPresent()){
            Material material = optional.get();
            return mvConsultarAddObjetos("", id, "excluir","nenhum", material.getNome());
        }
        return new ModelAndView("redirect:/materiais");
    }

    @GetMapping("/{id}delete")
    public ModelAndView delete(@PathVariable Long id) {
        Optional<Material> optional = materialRepository.findById(id);
        if(optional.isPresent()){
            Material material = optional.get();
            materialRepository.delete(material);
            return mvConsultarAddObjetos("", id, "mensagem","Material excluído com sucesso", material.getNome());
        }
        return mvConsultarAddObjetos("", id, "mensagem","Ocorreu um erro ao excluir","nenhum");
    }

    private void mvObjetos(ModelAndView mv){
        mv.addObject("ativa", "estoque");
    }

    private ModelAndView mvAdicionarAddObjetos(Boolean sucesso, RequisicaoMaterial requisicaoMaterial){
        ModelAndView mv = new ModelAndView("est-adicionar-material");
        mvObjetos(mv);
        mv.addObject("sucesso", sucesso);
        mv.addObject("material", requisicaoMaterial);
        return mv;
    }

    private ModelAndView mvConsultarAddObjetos(String searchKey, Long materialId, String status, String mensagem, String materialNome){
        ModelAndView mv = new ModelAndView("est-consultar-estoque");
        mvObjetos(mv);
        mv.addObject("materiais", criarListaMateriais(searchKey));
        mv.addObject("materialNome", materialNome);
        mv.addObject("materialId", materialId);
        mv.addObject("status", status);
        mv.addObject("mensagem", mensagem);
        return mv;
    }

    private ModelAndView mvAtualizarAddObjetos(Boolean falha, RequisicaoMaterial requisicaoMaterial, Long materialId){
        ModelAndView mv = new ModelAndView("est-atualizar-material");
        mvObjetos(mv);
        mv.addObject("falha", falha);
        mv.addObject("material", requisicaoMaterial);
        mv.addObject("materialId", materialId);
        return mv;
    }

    private List<Material> criarListaMateriais(String searchKey){
        List<Material> materiais = new ArrayList<>();
        if(!searchKey.equals("") && searchKey.matches("[+-]?\\d*(\\.\\d+)?")){
            Optional<Material> optional = materialRepository.findById(Long.parseLong(searchKey));
            if(optional.isPresent()){
                materiais.add(optional.get());
            }
        }else{
            materiais.addAll(materialRepository.findByNomeContainingIgnoreCase(searchKey));
        }
        return materiais;
    }
}
