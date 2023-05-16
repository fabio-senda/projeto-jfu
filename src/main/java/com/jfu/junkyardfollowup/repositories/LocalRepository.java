package com.jfu.junkyardfollowup.repositories;

import com.jfu.junkyardfollowup.enums.StatusLocal;
import com.jfu.junkyardfollowup.models.Local;
import com.jfu.junkyardfollowup.models.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface LocalRepository extends JpaRepository<Local, Long> {
    public List<Local> findAllByNomeIgnoreCase(String nome);
    public List<Local> findAllByNomeContainingIgnoreCase(String nome);
    public List<Local> findByStatus(StatusLocal status);
    public List<Local> findByMaterialAndStatus(Material material, StatusLocal status);
    public List<Local> findByMaterial(Material material);
    @Query("SELECT u FROM Local u WHERE u.material.nome LIKE %:nome%")
    public List<Local> findAllByMaterialNome(@Param("nome") String nome);
    public List<Local> findAllByStatus(StatusLocal status);
}
