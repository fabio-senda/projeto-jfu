package com.jfu.junkyardfollowup.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "Fornecedor")
@Entity(name = "fornecedor")
public class Fornecedor{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive(message = "Preecha o campo")
    private Long id;

    @EqualsAndHashCode.Include
    @NotBlank(message = "Preencha o campo")
    @Size(max = 60)
    private String nome;
}
