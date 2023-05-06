package com.jfu.junkyardfollowup.repositories;

import com.jfu.junkyardfollowup.models.Fornecedor;
import com.jfu.junkyardfollowup.models.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
    public List<Fornecedor> findByNomeContainingIgnoreCase(String key);
}
