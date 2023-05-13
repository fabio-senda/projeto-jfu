package com.jfu.junkyardfollowup.dtos;

import com.jfu.junkyardfollowup.models.Fornecedor;
import com.jfu.junkyardfollowup.models.Fornecimento;
import com.jfu.junkyardfollowup.models.RegistroDeCompra;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
public class CompraDto {
    private Long id;

    private Fornecedor fornecedor;

    private List<Fornecimento> fornecimentos;

    private Long quantidadeDeItens;

    private String data;

    public static CompraDto fromRegistroDeCompra(RegistroDeCompra registroDeCompra) {
        CompraDto compra = new CompraDto();
        compra.id = registroDeCompra.getId();
        compra.fornecedor = registroDeCompra.getFornecedor();
        compra.fornecimentos = registroDeCompra.getFornecimentos();
        compra.quantidadeDeItens = registroDeCompra.getQuantidadeDeItens();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");
        compra.data = registroDeCompra.getData().format(formatter);
        return compra;
    }
}
