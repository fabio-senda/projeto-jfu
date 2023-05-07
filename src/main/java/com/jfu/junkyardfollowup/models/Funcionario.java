package com.jfu.junkyardfollowup.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table
public class Funcionario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String usuario;

    @NotBlank
    @Column(nullable = false)
    private String senha;

    @ManyToMany
    @JoinTable(name = "funcionario_cargo",
            joinColumns = @JoinColumn(name= "funcionario_id"),
            inverseJoinColumns = @JoinColumn(name = "cargo_id"))
    private List<Cargo> cargos;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { //cargos
           return List.of(new SimpleGrantedAuthority("CARGO_ADMIN"));
        // return this.cargos;
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.usuario;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
