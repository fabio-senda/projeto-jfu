package com.jfu.junkyardfollowup.service;

import com.jfu.junkyardfollowup.models.Material;
import com.jfu.junkyardfollowup.repositories.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogoMaterialService {
    @Autowired
    private MaterialRepository materialRepository;

    public List<Material> listarMateriais(){
        return materialRepository.findAll();
    }
    public Material materialById(Long materialId){
        return materialRepository.findById(materialId)
                .orElse(null);
    }
}
