package com.jfu.junkyardfollowup.controllers;

import com.jfu.junkyardfollowup.dtos.CompraDto;
import com.jfu.junkyardfollowup.dtos.FornecimentoDto;
import com.jfu.junkyardfollowup.models.Fornecedor;
import com.jfu.junkyardfollowup.models.Fornecimento;
import com.jfu.junkyardfollowup.models.RegistroDeCompra;
import com.jfu.junkyardfollowup.repositories.FornecedorRepository;
import com.jfu.junkyardfollowup.services.CompraService;
import com.jfu.junkyardfollowup.services.MaterialService;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/compras")
public class CompraController {
    @Autowired
    CompraService compraService;

    @Autowired
    MaterialService materialService;

    @Autowired
    FornecedorRepository fornecedorRepository;

    private List<Fornecimento> itens1 = new ArrayList<>();
    private List<Fornecimento> itens2 = new ArrayList<>();

    @GetMapping
    public ModelAndView consultarCompras(@RequestParam(defaultValue = "") String searchKey){
        return mvConsultarAddObjetos(searchKey, 0l, "nenhum", "nehum");
    }

    @GetMapping("/add")
    public ModelAndView telaAdd(){
        return mvAdicionarAddObjetos(new FornecimentoDto(), itens1,
                "nenhum", "nenhum", new Fornecedor(),
                null, calcularTotal());
    }

    @PostMapping("/addItem")
    public ModelAndView addItem(@ModelAttribute("fornecimento") @Valid FornecimentoDto fornecimentoDto,
                                BindingResult result, RedirectAttributes redirect){
        boolean flag = true;
        if(result.hasErrors()){
            return mvAdicionarAddObjetos(fornecimentoDto, itens1,
                    "nenhum", "nenhum", new Fornecedor(),
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
        return mvAdicionarAddObjetos(new FornecimentoDto(), itens1,
                "nenhum", "nenhum", new Fornecedor(),
                null, total);
    }

    @GetMapping("/confirmar-registro")
    public ModelAndView finalizar(){
        return mvAdicionarAddObjetos(new FornecimentoDto(), itens1,
                "registrar", "Confirmar registro de compra?", new Fornecedor(),
                listaFornecedores(), calcularTotal());
    }

    @PostMapping("/new")
    public ModelAndView novo(@ModelAttribute("fornecedor") @Valid Fornecedor fornecedor,
                             BindingResult result, RedirectAttributes redirect){
        if(result.getErrorCount() > 1){
            return mvAdicionarAddObjetos(new FornecimentoDto(), itens1,
                    "registrar", "Confirmar registro de compra?", fornecedor,
                    listaFornecedores(), calcularTotal());
        }
        if(itens1.size() == 0){
            return mvAdicionarAddObjetos(new FornecimentoDto(), itens1,
                    "mensagem", "É preciso adicionar pelo menos um item",
                    new Fornecedor(), null, calcularTotal());
        }
        RegistroDeCompra compra = new RegistroDeCompra();
        compra.setData(LocalDateTime.now().atZone(ZoneId.systemDefault()).toLocalDateTime());
        compra.setFornecedor(fornecedor);
        compra.setQuantidadeDeItens((long) itens1.size());
        compra = compraService.save(compra, itens1);
        compra.setFornecimentos(new ArrayList<>(itens1));
        CompraDto compraDto = CompraDto.fromRegistroDeCompra(compra);

        itens1.clear();
        return mvDetalhesAddObjetos(compra.getId(), compra.getFornecedor().getNome(), compraDto.getData(), compraService.calcularTotal(compra), compra.getFornecimentos(), true);
    }

    @GetMapping("/{id}delete-item-confirm")
    public ModelAndView confirmar(@PathVariable Long id){
        ModelAndView mv = mvAdicionarAddObjetos(new FornecimentoDto(), itens1,
                "excluir", "Excluir item?", new Fornecedor(),
                listaFornecedores(), calcularTotal());
        mv.addObject("id", id);
        return mv;
    }

    @GetMapping("/{id}delete-item")
    public ModelAndView deleteItem(@PathVariable Long id){
        Fornecimento f = null;
        for (Fornecimento fornecimento : itens1){
            if(fornecimento.getMaterial().getId().equals(id)){
                f = fornecimento;
            }
        }
        if(f != null) {
            itens1.remove(f);
        }
        return new ModelAndView("redirect:/compras/add");
    }

    @GetMapping("/{id}delete-confirm")
    public ModelAndView deleteConfirm(@PathVariable Long id){
        RegistroDeCompra compra = compraService.findById(id);
        if (compra != null){
            return mvConsultarAddObjetos("", id, "excluir","");
        }
        return new ModelAndView("redirect:/compras");
    }

    @GetMapping("/{id}delete")
    public ModelAndView delete(@PathVariable Long id){
        RegistroDeCompra compra = compraService.findById(id);
        if (compra != null){
            compraService.delete(compra);
            return mvConsultarAddObjetos("", id, "mensagem","Compra #" + id + " excluída com sucesso!");
        }
        return mvConsultarAddObjetos("", id, "mensagem","Ocorreu um erro ao excluir a compra!");
    }

    @GetMapping("/{id}gerar-pdf")
    public void exportToPDF(@PathVariable Long id, HttpServletResponse response) throws DocumentException, IOException {
        compraService.gerarRecibo(id, response);
    }

    @GetMapping("/{id}detalhes")
    public ModelAndView detalhes(@PathVariable Long id){
        RegistroDeCompra compra = compraService.findById(id);
        if(compra != null){
            CompraDto compraDto = CompraDto.fromRegistroDeCompra(compra);
            return mvDetalhesAddObjetos(id, compra.getFornecedor().getNome(), compraDto.getData(), compraService.calcularTotal(compra), compra.getFornecimentos(), false);
        }
        return new ModelAndView("redirect:/compras");
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

    private void mvObjetos(ModelAndView mv){
        mv.addObject("ativa", "compra");
    }

    private ModelAndView mvConsultarAddObjetos(String searchKey, Long compraId, String status, String mensagem){
        ModelAndView mv = new ModelAndView("comp-consultar-compra");
        mvObjetos(mv);
        mv.addObject("compras", compraService.criarListaCompras(searchKey));
        mv.addObject("compraId", compraId);
        mv.addObject("status", status);
        mv.addObject("mensagem", mensagem);
        return mv;
    }

    private ModelAndView mvAdicionarAddObjetos(FornecimentoDto fornecimento, List<Fornecimento> fornecimentos, String status, String mensagem, Fornecedor fornecedor, List<Fornecedor> fornecedores, BigDecimal total){
        ModelAndView mv = new ModelAndView("comp-adicionar-compra");
        mvObjetos(mv);
        mv.addObject("materiais", materialService.listaMaterialComLocais());
        mv.addObject("fornecimento", fornecimento);
        mv.addObject("fornecimentos", fornecimentos);
        mv.addObject("status", status);
        mv.addObject("mensagem", mensagem);
        mv.addObject("fornecedor", fornecedor);
        mv.addObject("fornecedores", fornecedores);
        mv.addObject("total", total);
        return mv;
    }

    private ModelAndView mvDetalhesAddObjetos(Long idCompra, String fornecedor, String data, BigDecimal total, List<Fornecimento> fornecimentos, Boolean sucesso){
        ModelAndView mv = new ModelAndView("comp-ver-compra");
        mvObjetos(mv);
        mv.addObject("idCompra", idCompra);
        mv.addObject("fornecedor", fornecedor);
        mv.addObject("data", data);
        mv.addObject("total", total);
        mv.addObject("fornecimentos", fornecimentos);
        mv.addObject("sucesso", sucesso);
        return mv;
    }


}
