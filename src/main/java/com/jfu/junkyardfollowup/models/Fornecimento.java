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
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @ManyToOne
    @JoinColumn(name = "compra_id", nullable = false)
    private RegistroDeCompra registroDeCompra;

    private BigDecimal preco;

    private BigDecimal quantidade;

    private BigDecimal total;

    public void calcularTotal() {
        this.total = preco.multiply(quantidade);
    }

    @Override
    public String toString() {
        return "Fornecimento{" +
                "id=" + id +
                ", material=" + material +
                ", registroDeCompra=" + registroDeCompra +
                ", preco=" + preco +
                ", quantidade=" + quantidade +
                ", total=" + total +
                '}';
    }
}
