package com.jfu.junkyardfollowup.service;

import com.jfu.junkyardfollowup.dto.FornecimentoDto;
import com.jfu.junkyardfollowup.models.Material;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FornecimentoService {
    @Autowired
    private CatalogoMaterialService materialService;

    public FornecimentoDto adicionarAtributosDto(FornecimentoDto fornecimento){
        Material material = materialService.materialById(fornecimento.getIdMaterial());
        // observation
        fornecimento.setNome(material.getNome());
        fornecimento.calcularPrecoTotal();
        return fornecimento;
    }
}
