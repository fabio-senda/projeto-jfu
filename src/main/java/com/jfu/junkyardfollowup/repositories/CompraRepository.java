package com.jfu.junkyardfollowup.repositories;

import com.jfu.junkyardfollowup.models.RegistroDeCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompraRepository extends JpaRepository<RegistroDeCompra, Long>{

}
