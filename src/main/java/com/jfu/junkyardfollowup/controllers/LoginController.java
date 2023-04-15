package com.jfu.junkyardfollowup.controllers;

import com.jfu.junkyardfollowup.dto.LoginDto;
import com.jfu.junkyardfollowup.service.CatalogoFucionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    CatalogoFucionarioService funcionarioService;

    @GetMapping
    public String login(Model model){
        model.addAttribute("login", new LoginDto());
        return "index.html";
    }

    @PostMapping("/autenticar")
    public String autentica(@ModelAttribute("login") LoginDto login,
                            RedirectAttributes redirect){
        if(!funcionarioService.autenticaLogin(login)){
            redirect.addFlashAttribute("mensagem",
                    "login ou senha incorretos");
            return "redirect:/login";
        }
        return "redirect:/jfu";
    }
}
