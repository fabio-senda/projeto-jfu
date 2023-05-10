package com.jfu.junkyardfollowup.repositories;

import com.jfu.junkyardfollowup.models.Fornecimento;
import com.jfu.junkyardfollowup.models.RegistroDeCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface FornecimentoRepository extends JpaRepository<Fornecimento, Long> {
    public List<Fornecimento> findAllByMaterial_Id(Long id);
    @Query("SELECT u FROM Fornecimento u WHERE u.registroDeCompra.id = :id")
    Collection<Fornecimento> findAllByCompraId(@Param("id") long id);
}
