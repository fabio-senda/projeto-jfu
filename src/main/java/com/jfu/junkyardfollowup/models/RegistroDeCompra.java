package com.jfu.junkyardfollowup.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class RegistroDeCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Fornecedor fornecedor;

    //@DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime data;

}
