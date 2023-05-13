package com.jfu.junkyardfollowup.controllers;

import com.jfu.junkyardfollowup.dtos.MaterialDto;
import com.jfu.junkyardfollowup.models.Fornecimento;
import com.jfu.junkyardfollowup.models.Material;
import com.jfu.junkyardfollowup.repositories.FornecimentoRepository;
import com.jfu.junkyardfollowup.repositories.MaterialRepository;
import com.jfu.junkyardfollowup.services.MaterialService;
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
public class MaterialController {
    @Autowired
    MaterialService materialService;

    @Autowired
    FornecimentoRepository fornecimentoRepository;

    @GetMapping
    public ModelAndView consultarEstoque(@RequestParam(defaultValue = "") String searchKey){
        return mvConsultarAddObjetos(searchKey,0l,"nenhum","nenhum","nenhum");
    }
    @GetMapping("/add")
    public ModelAndView telaAddMaterial(){
        return mvAdicionarAddObjetos(false, new MaterialDto());
    }

    @PostMapping("/add")
    public ModelAndView addMaterial(@ModelAttribute("material") @Valid MaterialDto materialDto,
                                    BindingResult result, RedirectAttributes redirect){
        if(result.hasErrors()){
            return mvAdicionarAddObjetos(false, materialDto);
        }
        materialService.save(materialDto.toMaterial());
        return mvAdicionarAddObjetos(true, new MaterialDto());
    }

    @GetMapping("/{id}edit")
    public ModelAndView edit(@PathVariable Long id){
        Optional<Material> optional = materialService.findById(id);
        if (optional.isPresent()) {
            Material material = optional.get();
            return mvAtualizarAddObjetos(false, new MaterialDto(material), material.getId());
        }
        return new ModelAndView("redirect:/materiais");
    }

    @PostMapping("/{id}edit")
    public ModelAndView update(@PathVariable Long id, @ModelAttribute("material") @Valid MaterialDto materialDto,
                               BindingResult result, RedirectAttributes redirect){
        Optional<Material> optional = materialService.findById(id);
        if (result.hasErrors()) {
            return mvAtualizarAddObjetos(false, materialDto, id);
        }
        if (optional.isEmpty()) {
            return mvAtualizarAddObjetos(true, materialDto, id);
        }
        Material material = optional.get();
        materialService.save(materialDto.toMaterial(material));
        return mvConsultarAddObjetos("", 0l, "mensagem","Material atualizado com sucesso", "nenhum");
    }

    @GetMapping("/{id}delete-confirm")
    public ModelAndView deleteConfirm(@PathVariable Long id){
        Optional<Material> optional = materialService.findById(id);
        if(optional.isPresent()){
            Material material = optional.get();
            return mvConsultarAddObjetos("", id, "excluir","nenhum", material.getNome());
        }
        return new ModelAndView("redirect:/materiais");
    }

    @GetMapping("/{id}delete")
    public ModelAndView delete(@PathVariable Long id) {
        Optional<Material> optional = materialService.findById(id);
        List<Fornecimento> fornecimentos = fornecimentoRepository.findAllByMaterial_Id(id);
        if(optional.isPresent() && fornecimentos.isEmpty()){
            Material material = optional.get();
            materialService.delete(material);
            return mvConsultarAddObjetos("", id, "mensagem","Material excluído com sucesso", material.getNome());
        }
        return mvConsultarAddObjetos("", id, "mensagem","Ocorreu um erro ao excluir, podem haver registros de compra com este material","nenhum");
    }

    private void mvObjetos(ModelAndView mv){
        mv.addObject("ativa", "estoque");
    }

    private ModelAndView mvAdicionarAddObjetos(Boolean sucesso, MaterialDto materialDto){
        ModelAndView mv = new ModelAndView("est-adicionar-material");
        mvObjetos(mv);
        mv.addObject("sucesso", sucesso);
        mv.addObject("material", materialDto);
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

    private ModelAndView mvAtualizarAddObjetos(Boolean falha, MaterialDto materialDto, Long materialId){
        ModelAndView mv = new ModelAndView("est-atualizar-material");
        mvObjetos(mv);
        mv.addObject("falha", falha);
        mv.addObject("material", materialDto);
        mv.addObject("materialId", materialId);
        return mv;
    }

    private List<Material> criarListaMateriais(String searchKey){
        List<Material> materiais = new ArrayList<>();
        if(!searchKey.equals("") && searchKey.matches("[+-]?\\d*(\\.\\d+)?")){
            Optional<Material> optional = materialService.findById(Long.parseLong(searchKey));
            if(optional.isPresent()){
                materiais.add(optional.get());
            }
        }else{
            materiais.addAll(materialService.findByNomeContainingIgnoreCase(searchKey));
        }
        return materiais;
    }
}
