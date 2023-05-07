package com.jfu.junkyardfollowup.controllers;

import com.jfu.junkyardfollowup.dtos.Login;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class InicioController {

    @GetMapping("/")
    public String index(){
        return "inicio-principal";
    }

    @GetMapping("/login")
    public ModelAndView login(){
        return new ModelAndView("login");
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
