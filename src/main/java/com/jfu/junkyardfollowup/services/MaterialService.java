package com.jfu.junkyardfollowup.services;

import com.jfu.junkyardfollowup.enums.StatusLocal;
import com.jfu.junkyardfollowup.models.Local;
import com.jfu.junkyardfollowup.models.Material;
import com.jfu.junkyardfollowup.repositories.LocalRepository;
import com.jfu.junkyardfollowup.repositories.MaterialRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class MaterialService {
    @Autowired
    MaterialRepository materialRepository;

    @Autowired
    LocalRepository localRepository;

    public Material save(Material material){
        return materialRepository.save(material);
    }

    public void delete(Material entity) {
        materialRepository.delete(entity);
    }

    public Optional<Material> findById(Long id) {
        return materialRepository.findById(id);
    }

    public List<Material> findByNomeContainingIgnoreCase(String key) {
        return materialRepository.findByNomeContainingIgnoreCase(key);
    }

    public List<Material> listaMateriais(){
        return materialRepository.findAll().stream().sorted(Comparator.comparing(Material::getNome)).toList();
    }


    public List<Material> listaMaterialComLocais(){
        List<Local> locais = localRepository.findByStatus(StatusLocal.Ativo);
        Set<Material> materiais = new HashSet<>();
        for (Local l : locais){
            materiais.add(l.getMaterial());
        }
        return materiais.stream().toList().stream().sorted(Comparator.comparing(Material::getNome)).toList();
    }
}
