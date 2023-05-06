package com.jfu.junkyardfollowup.dto;

import com.jfu.junkyardfollowup.models.Material;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;

@Getter
@Setter
public class RequisicaoMaterial {
    @NotBlank
    @Size(max = 60)
    private String nome;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer=12, fraction=2)
    private BigDecimal preco;

    public RequisicaoMaterial(){

    }

    public RequisicaoMaterial(Material material){
        this.fromMaterial(material);
    }

    public Material toMaterial(){
        Material material = new Material();
        material.setNome(this.getNome());
        material.setPreco(this.getPreco());
        return material;
    }

    public Material toMaterial(Material material){
        material.setNome(this.getNome());
        material.setPreco(this.getPreco());
        return material;
    }

    public void fromMaterial(Material material){
        this.setNome(material.getNome());
        this.setPreco(material.getPreco());
    }
}
