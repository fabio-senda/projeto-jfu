package com.jfu.junkyardfollowup.repositories;

import com.jfu.junkyardfollowup.models.Fornecedor;
import com.jfu.junkyardfollowup.models.Material;
import com.jfu.junkyardfollowup.models.RegistroDeCompra;
import jakarta.persistence.criteria.From;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<RegistroDeCompra, Long>{
    public List<RegistroDeCompra> findByFornecedor(Fornecedor fornecedor);
}
