package com.jfu.junkyardfollowup.repositories;

import com.jfu.junkyardfollowup.models.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    public List<Funcionario> findByUsuario(String usuario);
}
