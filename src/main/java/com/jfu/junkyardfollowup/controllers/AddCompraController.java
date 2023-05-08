package com.jfu.junkyardfollowup.controllers;

import com.jfu.junkyardfollowup.dtos.FornecimentoDto;
import com.jfu.junkyardfollowup.models.Fornecedor;
import com.jfu.junkyardfollowup.models.Fornecimento;
import com.jfu.junkyardfollowup.models.Material;
import com.jfu.junkyardfollowup.models.RegistroDeCompra;
import com.jfu.junkyardfollowup.repositories.CompraRepository;
import com.jfu.junkyardfollowup.repositories.FornecedorRepository;
import com.jfu.junkyardfollowup.repositories.FornecimentoRepository;
import com.jfu.junkyardfollowup.repositories.MaterialRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/compras")
public class AddCompraController {
    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private FornecimentoRepository fornecimentoRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    private List<Fornecimento> itens1 = new ArrayList<>();
    private List<Fornecimento> itens2 = new ArrayList<>();


    @GetMapping("/add")
    public ModelAndView telaAdd(){
        return mvAdicionarAddObjetos(listaMaterialOrdenada(), new FornecimentoDto(), itens1,
                "nenhum", "nenhum",
                null, calcularTotal());
    }

    @PostMapping("/addItem")
    public ModelAndView addItem(@ModelAttribute("fornecimento") @Valid FornecimentoDto fornecimentoDto,
                                BindingResult result, RedirectAttributes redirect){
        boolean flag = true;
        if(result.hasErrors()){
            return mvAdicionarAddObjetos(listaMaterialOrdenada(), fornecimentoDto, itens1,
                    "nenhum", "nenhum",
                    null, calcularTotal());
        }
        BigDecimal total = new BigDecimal(0);
        for (Fornecimento item : itens1) {
            if (item.getMaterial().getId().equals(fornecimentoDto.getMaterial().getId())){
                item.setQuantidade(item.getQuantidade().add(fornecimentoDto.getQuantidade()));
                item.calcularTotal();
                flag = false;
            }
            total = total.add(item.getTotal());
        }
        if (flag) {
            String preco = fornecimentoDto.getPreco().substring(3);
            if (preco.matches("[+-]?\\d*(\\.\\d+)?")) {
                fornecimentoDto.setPreco(preco);
                Fornecimento fornecimento = fornecimentoDto.toFornecimento();
                total = total.add(fornecimento.getTotal());
                itens1.add(fornecimento);
            }
        }
        return mvAdicionarAddObjetos(listaMaterialOrdenada(), new FornecimentoDto(), itens1,
                "nenhum", "nenhum",
                null, total);
    }


    @GetMapping("/confirmar-registro")
    public ModelAndView finalizar(){
        return mvAdicionarAddObjetos(null, new FornecimentoDto(), itens1,
                "registrar", "Confirmar registro de compra?",
                listaFornecedores(), calcularTotal());
    }

    @PostMapping("/new")
    public ModelAndView novo(@ModelAttribute("fornecedor") @Valid Fornecedor fornecedor,
                             BindingResult result, RedirectAttributes redirect){
        if(itens1.size() == 0){
            return mvAdicionarAddObjetos(listaMaterialOrdenada(), new FornecimentoDto(), itens1,
                    "mensagem", "É preciso adicionar pelo menos um item",
                    null, calcularTotal());
        }
        RegistroDeCompra registroDeCompra = new RegistroDeCompra();
        registroDeCompra.setData(LocalDateTime.now());
        registroDeCompra.setFornecedor(fornecedor);
        registroDeCompra.setQuantidadeDeItens((long) itens1.size());
        compraRepository.save(registroDeCompra);
        for (Fornecimento f: itens1) {
            f.setRegistroDeCompra(registroDeCompra);
            fornecimentoRepository.save(f);
        }
        itens1.clear();
        return new ModelAndView("inicio-principal");
    }

    @GetMapping("/{id}delete-item-confirm")
    public ModelAndView confirmar(@PathVariable Long id){
        ModelAndView mv = mvAdicionarAddObjetos(null, new FornecimentoDto(), itens1,
                "excluir", "Excluir item?",
                listaFornecedores(), calcularTotal());
        mv.addObject("id", id);
        return mv;
    }

    @GetMapping("/{id}delete-item")
    public ModelAndView deleteItem(@PathVariable Long id){
        Fornecimento f = null;
        for (Fornecimento fornecimento : itens1){
            if(fornecimento.getMaterial().getId() == id){
                f = fornecimento;
            }
        }
        if(f != null) {
            itens1.remove(f);
        }
        return new ModelAndView("redirect:/compras/add");
    }

    private List<Material> listaMaterialOrdenada(){
        return materialRepository.findAll().stream().sorted(Comparator.comparing(Material::getNome)).toList();
    }

    private BigDecimal calcularTotal(){
        BigDecimal total = new BigDecimal(0);
        for (Fornecimento item : itens1){
            total = total.add(item.getTotal());
        }
        return total;
    }

    private List<Fornecedor> listaFornecedores(){
        return fornecedorRepository.findAll();
    }

    private void mvAddObjetos(ModelAndView mv){
        mv.addObject("ativa", "compra");
    }

    /**
     *
     * @param materiais options do select material
     * @param fornecimento form adicionar item
     * @param fornecimentos dados da tabela
     * @param status modal de tipos diferentes 'excluir', 'registrar' ou 'mensagem'
     * @param mensagem texto de ambos status
     * @param fornecedores lista de fornecedores para o modal
     * @param total texto do preço total da compra
     * @return view
     */
    private ModelAndView mvAdicionarAddObjetos(List<Material> materiais, FornecimentoDto fornecimento, List<Fornecimento> fornecimentos, String status, String mensagem, List<Fornecedor> fornecedores, BigDecimal total){
        ModelAndView mv = new ModelAndView("comp-adicionar-compra");
        mvAddObjetos(mv);
        mv.addObject("materiais", materiais);
        mv.addObject("fornecimento", fornecimento);
        mv.addObject("fornecimentos", fornecimentos);
        mv.addObject("status", status);
        mv.addObject("mensagem", mensagem);
        mv.addObject("fornecedor", new Fornecedor());
        mv.addObject("fornecedores", fornecedores);
        mv.addObject("total", total);
        return mv;
    }
}
