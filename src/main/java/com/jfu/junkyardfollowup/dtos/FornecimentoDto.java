package com.jfu.junkyardfollowup.dtos;

import com.jfu.junkyardfollowup.models.Fornecedor;
import com.jfu.junkyardfollowup.models.Fornecimento;
import com.jfu.junkyardfollowup.models.Material;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
public class FornecimentoDto {
    @NotNull(message = "Preencha o campo")
    private Material material;
    @NotBlank
    private String preco;
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal quantidade;

    private BigDecimal total;


    public Fornecimento toFornecimento(){
        Fornecimento fornecimento = new Fornecimento();
        fornecimento.setMaterial(this.material);
        fornecimento.setPreco(new BigDecimal(this.preco));
        fornecimento.setQuantidade(this.quantidade);
        fornecimento.setTotal(fornecimento.getPreco().multiply(this.quantidade).setScale(2, RoundingMode.HALF_EVEN));
        return fornecimento;
    }

    public void fromFornecimento(Fornecimento fornecimento){
        FornecimentoDto fornecimentoDto = new FornecimentoDto();
        fornecimentoDto.setMaterial(fornecimento.getMaterial());
        fornecimentoDto.setPreco(fornecimentoDto.getPreco());
        fornecimentoDto.setQuantidade(fornecimento.getQuantidade());
        fornecimentoDto.setTotal(fornecimento.getTotal());
    }

    @Override
    public String toString() {
        return "FornecimentoDto{" +
                "material=" + material.getNome() +
                ", preco=" + preco +
                ", quantidade=" + quantidade +
                ", total=" + total +
                '}';
    }
}
