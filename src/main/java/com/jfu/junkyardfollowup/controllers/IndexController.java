package com.jfu.junkyardfollowup.controllers;

import com.jfu.junkyardfollowup.dto.LoginDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {
    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/jfu")
    public String bemVindo(){
        return "menu.html";
    }
}
