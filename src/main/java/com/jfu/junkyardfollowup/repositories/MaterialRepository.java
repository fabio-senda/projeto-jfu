package com.jfu.junkyardfollowup.repositories;

import com.jfu.junkyardfollowup.models.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    public List<Material> findByIdOrNomeContainingIgnoreCase(Long key1, String key2);
    public Optional<Material> findById(Long id);
    public List<Material> findByNomeContainingIgnoreCase(String key);
}
