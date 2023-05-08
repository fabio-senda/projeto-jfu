package com.jfu.junkyardfollowup.controllers;

import com.jfu.junkyardfollowup.services.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/compras")
public class CompraController {
    @Autowired
    CompraService compraService;

    @GetMapping
    public ModelAndView consultarCompras(@RequestParam(defaultValue = "") String searchKey){
        return mvConsultarAddObjetos(searchKey, 0l, "nenhum", "nehum");
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
