package com.jfu.junkyardfollowup.dto;

import com.jfu.junkyardfollowup.service.FornecimentoService;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class FornecimentoDto {
    private Long idMaterial;
    private String nome;
    private Double quantidade;
    private Double precoDeCompra;
    private Double precoTotal;

    public void calcularPrecoTotal(){
        this.precoTotal = 12d;
    }
}
