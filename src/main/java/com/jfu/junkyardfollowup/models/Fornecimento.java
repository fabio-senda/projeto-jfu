package com.jfu.junkyardfollowup.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table
public class Fornecimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Material material;

    protected BigDecimal precoDeCompra;

    protected BigDecimal quantidade;

    @ManyToOne
    private RegistroDeCompra registroDeCompra;
}
