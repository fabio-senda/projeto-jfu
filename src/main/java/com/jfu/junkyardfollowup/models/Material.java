package com.jfu.junkyardfollowup.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

@Getter
@Setter
@Table(name = "Material")
@Entity(name = "material")
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 60)
    private String nome;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer=9, fraction=2)
    private BigDecimal preco;

    @NotNull
    @Value("0")
    private BigDecimal quantidade = BigDecimal.valueOf(0.0);

    @Override
    public String toString() {
        return getId() + "-" + getNome() + "-" + getPreco();
    }
}
