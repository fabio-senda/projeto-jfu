package com.jfu.junkyardfollowup.controllers;

import com.jfu.junkyardfollowup.models.RegistroDeCompra;
import com.jfu.junkyardfollowup.others.Recibo;
import com.jfu.junkyardfollowup.services.CompraService;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/compras")
public class CompraController {
    @Autowired
    CompraService compraService;

    @GetMapping
    public ModelAndView consultarCompras(@RequestParam(defaultValue = "") String searchKey){
        return mvConsultarAddObjetos(searchKey, 0l, "nenhum", "nehum");
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
            return mvConsultarAddObjetos("", id, "mensagem","Compra #" + id + "excluída com sucesso!");
        }
        return mvConsultarAddObjetos("", id, "mensagem","Ocorreu um erro ao excluir a compra!");
    }

    @GetMapping("/{id}gerar-pdf")
    public void exportToPDF(@PathVariable Long id, HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=compra" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        Recibo recibo = compraService.gerarRecibo(id, response);
        try {
            recibo.export(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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


}
