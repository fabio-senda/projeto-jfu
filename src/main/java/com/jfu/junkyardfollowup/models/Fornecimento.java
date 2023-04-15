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
    private long id;

    @ManyToOne
    private Material material;

    private BigDecimal precoDeCompra;

    @ManyToOne
    private RegistroDeCompra registroDeCompra;
}
