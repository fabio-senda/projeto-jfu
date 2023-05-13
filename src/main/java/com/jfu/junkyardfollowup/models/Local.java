package com.jfu.junkyardfollowup.models;

import com.jfu.junkyardfollowup.enums.StatusLocal;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

@Getter
@Setter
@Table(name = "Local")
@Entity(name = "Local")
public class Local implements Comparable<Local>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 60)
    @Column
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusLocal status;

    @ManyToOne
    @JoinColumn(name="material_id", nullable = false)
    private Material material;

    @NotNull
    @Value("0")
    private BigDecimal quantidade = BigDecimal.valueOf(0.0);

    @Override
    public int compareTo(Local o) {
        if(this.getId().equals(o.getId())){
            return 0;
        } else if(this.getId() > o.getId()) {
            return 1;
        } else{
            return -1;
        }
    }
}
