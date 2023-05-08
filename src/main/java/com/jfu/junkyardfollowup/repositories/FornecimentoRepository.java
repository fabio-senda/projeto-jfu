package com.jfu.junkyardfollowup.repositories;

import com.jfu.junkyardfollowup.models.Fornecimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FornecimentoRepository extends JpaRepository<Fornecimento, Long> {
    public List<Fornecimento> findAllByMaterial_Id(Long id);
}
