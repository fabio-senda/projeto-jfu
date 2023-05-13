package com.jfu.junkyardfollowup.controllers;

import com.jfu.junkyardfollowup.dtos.LocalDto;
import com.jfu.junkyardfollowup.enums.StatusLocal;
import com.jfu.junkyardfollowup.models.Local;
import com.jfu.junkyardfollowup.models.Material;
import com.jfu.junkyardfollowup.repositories.LocalRepository;
import com.jfu.junkyardfollowup.services.MaterialService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/locais")
public class LocalController {
    @Autowired
    LocalRepository localRepository;

    @Autowired
    MaterialService materialService;

    @GetMapping("")
    public ModelAndView consultarLocais(@RequestParam(defaultValue = "") String searchKey){
        return mvConsultarAddObjetos(searchKey, 0l, "nenhum", "nenhum");
    }

    @GetMapping("/add")
    public ModelAndView telaAddLocal(){
        return mvAdicionarAddObjetos(false, new LocalDto());
    }

    @PostMapping("/add")
    public ModelAndView addLocal(@ModelAttribute("local") @Valid LocalDto localDto, BindingResult result, RedirectAttributes redirect){
        if (result.getErrorCount() > 1){
            return mvAdicionarAddObjetos(false, localDto);
        }
        localRepository.save(localDto.toLocal());
        return mvAdicionarAddObjetos(true, new LocalDto());
    }

    @GetMapping("/{id}edit")
    public ModelAndView edit(@PathVariable Long id){
        Optional<Local> optional = localRepository.findById(id);
        if (optional.isPresent()){
            LocalDto local = new LocalDto();
            local.fromLocal(optional.get());
            return mvAtualizarAddObjetos(false, "", local, id);
        }
        return new ModelAndView("redirect:/locais");
    }

    @PostMapping("/{id}edit")
    public ModelAndView atualizar(@PathVariable Long id, @ModelAttribute("local") @NotBlank @NotNull LocalDto localDto,
                                  BindingResult result){
        if(result.hasErrors()){
            return mvAtualizarAddObjetos(true, "Preencha os campos corretamente", localDto, id);
        }
        Optional<Local> optional = localRepository.findById(id);
        if(optional.isPresent()){
            Local local = optional.get();
            if (local.getStatus() == StatusLocal.Ativo) {
                List<Local> locais = localRepository.findByMaterialAndStatus(local.getMaterial(), StatusLocal.Ativo);
                if (locais.size() == 1)
                    if(localDto.getStatus() != StatusLocal.Ativo || !localDto.getMaterial().equals(local.getMaterial())) {
                    return mvAtualizarAddObjetos(true, "Não há locais ativos para " + local.getMaterial().getNome(), localDto, id);
                }
            }
            if (local.getQuantidade().doubleValue() == 0 ||
                    local.getMaterial().getId().equals(localDto.getMaterial().getId())) {
                Local localNovo = localDto.toLocal();
                localNovo.setId(id);
                localNovo.setQuantidade(local.getQuantidade());
                localRepository.save(localNovo);
                return mvConsultarAddObjetos("", id, "mensagem", "Local atualizado com sucesso!");
            }
            String alerta = "Ainda há " + local.getMaterial().getNome() + " no local";
            return mvAtualizarAddObjetos(true, alerta, localDto, id);
        }
        return new ModelAndView("redirect:/locais");
    }

    @GetMapping("/{id}delete-confirm")
    public ModelAndView deleteConfirm(@PathVariable Long id){
        Optional<Local> optional = localRepository.findById(id);
        if (optional.isPresent()){
            LocalDto local = new LocalDto();
            local.fromLocal(optional.get());
            return mvConsultarAddObjetos("", id,"excluir", "Ex").addObject("localNome", local.getNome());
        }
        return new ModelAndView("redirect:/locais");
    }

    @GetMapping("/{id}delete")
    public ModelAndView delete(@PathVariable Long id){
        Optional<Local> optional = localRepository.findById(id);
        if(optional.isPresent()){
            Local local = optional.get();
            if (local.getQuantidade().doubleValue() == 0){
                localRepository.delete(local);
                return mvConsultarAddObjetos("", id, "mensagem", "Local excluído com sucesso!");
            }
            String alerta = "Não foi possível excluir local!\nAinda há " + local.getMaterial().getNome() + " no local";
            return mvConsultarAddObjetos("", id, "mensagem", alerta);
        }
        return new ModelAndView("redirect:/locais");
    }

    private List<Local> criarListaLocais(String searchKey){
        List<Local> locais = new ArrayList<>();
        Optional<Local> optional = null;
        if (!searchKey.isBlank()) {
            if (searchKey.matches("[+-]?\\d*(\\.\\d+)?")) {
                Long n = Long.parseLong(searchKey);
                optional = localRepository.findById(n);
                locais.add(optional.get());
            }
            Set<Local> localSet = new HashSet<>();
            for (StatusLocal s : StatusLocal.values()) {
                if (searchKey.equalsIgnoreCase(s.toString())) {
                    localSet.addAll(localRepository.findAllByStatus(StatusLocal.valueOf(StringUtils.capitalize(searchKey))));
                }
            }
            localSet.addAll(localRepository.findAllByNomeContainingIgnoreCase(searchKey));
            localSet.addAll(localRepository.findAllByMaterialNome(searchKey));
            if(optional != null) {
                localSet.remove(optional.get());
            }
            locais.addAll(localSet.stream().sorted().toList());
        } else {
            locais.addAll(localRepository.findAll());
        }
        return locais;
    }

    private List<Material> listaMateriais(Material material){
        List<Material> materiais = new ArrayList<>(materialService.listaMateriais());
        materiais.remove(material);
        materiais.add(0, material);
        return materiais;
    }

    private void mvObjetos(ModelAndView mv){
        mv.addObject("ativa", "estoque");
    }

    private ModelAndView mvAdicionarAddObjetos(Boolean sucesso, LocalDto localDto){
        ModelAndView mv = new ModelAndView("est-adicionar-local");
        mvObjetos(mv);
        mv.addObject("sucesso", sucesso);
        mv.addObject("local", localDto);
        mv.addObject("materiais", materialService.listaMateriais());
        return mv;
    }

    private ModelAndView mvAtualizarAddObjetos(Boolean falha, String alerta, LocalDto localDto, Long localId){
        ModelAndView mv = new ModelAndView("est-atualizar-local");
        mv.addObject("falha", falha);
        mv.addObject("alerta", alerta);
        mv.addObject("local", localDto);
        mv.addObject("localId", localId);
        mv.addObject("materiais", materialService.listaMateriais());
        mv.addObject("statusLocal", StatusLocal.values());
        return mv;
    }

    private ModelAndView mvConsultarAddObjetos(String searchKey, Long localId, String status, String mensagem){
        ModelAndView mv = new ModelAndView("est-consultar-local");
        mvObjetos(mv);
        mv.addObject("locais", criarListaLocais(searchKey));
        mv.addObject("localId", localId);
        mv.addObject("status", status);
        mv.addObject("mensagem", mensagem);
        return mv;
    }

}
