package com.jfu.junkyardfollowup.repositories;

import com.jfu.junkyardfollowup.models.Material;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material, Long> {
}
