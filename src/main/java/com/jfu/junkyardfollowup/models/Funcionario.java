package com.jfu.junkyardfollowup.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table
public class Funcionario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    private String cpf;

    @NotBlank
    private String contato;

    @NotBlank
    private String usuario;

    @NotBlank
    private String senha;
}
