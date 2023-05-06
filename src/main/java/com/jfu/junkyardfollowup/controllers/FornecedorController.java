package com.jfu.junkyardfollowup.controllers;

import com.jfu.junkyardfollowup.models.Fornecedor;
import com.jfu.junkyardfollowup.repositories.FornecedorRepository;
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
@RequestMapping("/fornecedores")
public class FornecedorController {
    @Autowired
    FornecedorRepository fornecedorRepository;

    @GetMapping
    public ModelAndView consultarFornecedor(@RequestParam(defaultValue = "") String searchKey){
        return mvConsultarAddObjetos(searchKey,0l,"nenhum","nenhum","nenhum");
    }
    @GetMapping("/add")
    public ModelAndView telaAddFornecedor(){
        return mvAdicionarAddObjetos(false, new Fornecedor());
    }

    @PostMapping("/add")
    public ModelAndView addFornecedor(@ModelAttribute("fornecedor") @Valid Fornecedor fornecedor,
                                    BindingResult result, RedirectAttributes redirect){
        if(result.hasErrors()){
            return mvAdicionarAddObjetos(false, fornecedor);
        }
        fornecedorRepository.save(fornecedor);
        return mvAdicionarAddObjetos(true, new Fornecedor());
    }

    @GetMapping("/{id}edit")
    public ModelAndView edit(@PathVariable Long id){
        Optional<Fornecedor> optional = fornecedorRepository.findById(id);
        if (optional.isPresent()) {
            Fornecedor fornecedor = optional.get();
            return mvAtualizarAddObjetos(false, fornecedor, fornecedor.getId());
        }
        return new ModelAndView("redirect:/fornecedores");
    }

    @PostMapping("/{id}edit")
    public ModelAndView update(@PathVariable Long id, @ModelAttribute("fornecedor") @Valid Fornecedor fornecedor,
                               BindingResult result, RedirectAttributes redirect){
        Optional<Fornecedor> optional = fornecedorRepository.findById(id);
        if (result.hasErrors()) {
            return mvAtualizarAddObjetos(false, fornecedor, id);
        }
        if (optional.isEmpty()) {
            return mvAtualizarAddObjetos(true, fornecedor, id);
        }
        Fornecedor fornecedor1 = optional.get();
        fornecedor1.setNome(fornecedor.getNome());
        fornecedorRepository.save(fornecedor);
        return mvConsultarAddObjetos("", 0l, "mensagem","Fornecedor atualizado com sucesso", "nenhum");
    }

    @GetMapping("/{id}delete-confirm")
    public ModelAndView deleteConfirm(@PathVariable Long id){
        Optional<Fornecedor> optional = fornecedorRepository.findById(id);
        if(optional.isPresent()){
            Fornecedor fornecedor = optional.get();
            return mvConsultarAddObjetos("", id, "excluir","nenhum", fornecedor.getNome());
        }
        return new ModelAndView("redirect:/fornecedores");
    }

    @GetMapping("/{id}delete")
    public ModelAndView delete(@PathVariable Long id) {
        Optional<Fornecedor> optional = fornecedorRepository.findById(id);
        if(optional.isPresent()){
            Fornecedor fornecedor = optional.get();
            fornecedorRepository.delete(fornecedor);
            return mvConsultarAddObjetos("", id, "mensagem","Fornecedor excluído com sucesso", fornecedor.getNome());
        }
        return mvConsultarAddObjetos("", id, "mensagem","Ocorreu um erro ao excluir","nenhum");
    }

    private void mvObjetos(ModelAndView mv){
        mv.addObject("ativa", "compra");
    }

    /**
     * View comp-adicionar-fornecedor
     * @param sucesso flag para apresentar cadastro bem sucedido após envio do formulário
     * @param fornecedor fornecedor utilizado para cadastro de usuário
     * @return view com flags adicionadas
     */
    private ModelAndView mvAdicionarAddObjetos(Boolean sucesso, Fornecedor fornecedor){
        ModelAndView mv = new ModelAndView("comp-adicionar-fornecedor");
        mvObjetos(mv);
        mv.addObject("sucesso", sucesso);
        mv.addObject("fornecedor", fornecedor);
        return mv;
    }

    /**
     * View comp-consultar-fornecedor
     * @param searchKey String utilizada para pesquisar dados na tabela por Nome ou Id
     * @param fornecedorId indica o id do fornecedor a ser excluído no modal
     * @param status indica se haverá ou não modal na view ('mensagem', 'excluir')
     * @param mensagem mensagem do modal se o status = 'mensagem'
     * @param fornecedorNome Nome do fornecedor a ser apresentado no modal de status = 'excluir'
     * @return view com flags adicionadas
     */
    private ModelAndView mvConsultarAddObjetos(String searchKey, Long fornecedorId, String status, String mensagem, String fornecedorNome){
        ModelAndView mv = new ModelAndView("comp-consultar-fornecedor");
        mvObjetos(mv);
        mv.addObject("fornecedores", criarListaFornecedores(searchKey));
        mv.addObject("fornecedorNome", fornecedorNome);
        mv.addObject("fornecedorId", fornecedorId);
        mv.addObject("status", status);
        mv.addObject("mensagem", mensagem);
        return mv;
    }

    /**
     * View "comp-atualizar-fornecedor"
     * @param falha indica na interface se houve algum erro ao atualizar fornecedor após envio do formulário
     * @param fornecedor objeto fornecedor a ser utilizado no formulário
     * @param fornecedorId Id do fornecedor a ser alterado
     * @return
     */
    private ModelAndView mvAtualizarAddObjetos(Boolean falha, Fornecedor fornecedor, Long fornecedorId){
        ModelAndView mv = new ModelAndView("comp-atualizar-fornecedor");
        mvObjetos(mv);
        mv.addObject("falha", falha);
        mv.addObject("fornecedor", fornecedor);
        mv.addObject("fornecedorId", fornecedorId);
        return mv;
    }

    private List<Fornecedor> criarListaFornecedores(String searchKey){
        List<Fornecedor> fornecedores = new ArrayList<>();
        if(!searchKey.equals("") && searchKey.matches("[+-]?\\d*(\\.\\d+)?")){
            Optional<Fornecedor> optional = fornecedorRepository.findById(Long.parseLong(searchKey));
            if(optional.isPresent()){
                fornecedores.add(optional.get());
            }
        }else{
            fornecedores.addAll(fornecedorRepository.findByNomeContainingIgnoreCase(searchKey));
        }
        return fornecedores;
    }
}
