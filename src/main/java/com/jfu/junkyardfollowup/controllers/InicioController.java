package com.jfu.junkyardfollowup.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/jfu")
public class InicioController {
    @GetMapping("/")
    public ModelAndView principal(){
        ModelAndView mv = new ModelAndView("inicio-principal");
        return mv;
    }
    @GetMapping("/compra")
    public ModelAndView compra(){
        ModelAndView mv = new ModelAndView("inicio-compra");
        mv.addObject("ativa", "compra");
        return mv;
    }

    @GetMapping("/venda")
    public ModelAndView venda(){
        ModelAndView mv = new ModelAndView("inicio-venda");
        mv.addObject("ativa", "venda");
        return mv;
    }

    @GetMapping("/estoque")
    public ModelAndView estoque(){
        ModelAndView mv = new ModelAndView("inicio-estoque");
        mv.addObject("ativa", "estoque");
        return mv;
    }
}
