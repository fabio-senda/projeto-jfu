package com.jfu.junkyardfollowup.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {
    @GetMapping("/index")
    public ModelAndView index() {
        System.out.println("Recebida");
        ModelAndView mv = new ModelAndView("index");
        return mv;
    }
}
