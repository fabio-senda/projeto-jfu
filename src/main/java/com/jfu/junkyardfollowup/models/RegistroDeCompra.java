package com.jfu.junkyardfollowup.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table
public class RegistroDeCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="fornecedor_id", nullable = false)
    private Fornecedor fornecedor;

    @OneToMany
    private List<Fornecimento> fornecimentos;

    //@DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime data;
}
