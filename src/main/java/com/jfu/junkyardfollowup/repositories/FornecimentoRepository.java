package com.jfu.junkyardfollowup.repositories;

import com.jfu.junkyardfollowup.models.Fornecimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FornecimentoRepository extends JpaRepository<Fornecimento, Long> {
}
