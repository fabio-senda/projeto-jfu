package com.jfu.junkyardfollowup.models;

import com.jfu.junkyardfollowup.enums.NomeCargo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
@Entity
@Table
public class Cargo implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private NomeCargo nomeCargo;

    @ManyToMany
    private Collection<Funcionario> funcionario;

    @Override
    public String getAuthority() {
        return this.nomeCargo.toString();
    }
}
