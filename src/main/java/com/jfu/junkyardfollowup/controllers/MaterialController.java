package com.jfu.junkyardfollowup.controllers;

import com.jfu.junkyardfollowup.models.Material;
import com.jfu.junkyardfollowup.repositories.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/materiais")
public class MaterialController {
    @Autowired
    MaterialRepository materialRepository;

    @GetMapping
    public String listarTodosMateriais(Model model){
        model.addAttribute("materiais", materialRepository.findAll());
        System.out.println("Requisição recebida!");
        return "index";
    }
}
