package com.jfu.junkyardfollowup.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "fornecedor")
@Entity(name = "fornecedor")
public class Fornecedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max = 60)
    private String nome;
    @NotBlank
    @Size(max = 60)
    private String email;
    @NotBlank
    private String telefone;
}
