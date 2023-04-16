package com.jfu.junkyardfollowup.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class RegistroDeCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Fornecedor fornecedor;

    @OneToMany
    private List<Fornecimento> fornecimentos = new ArrayList<>();

    //@DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime data;

}
