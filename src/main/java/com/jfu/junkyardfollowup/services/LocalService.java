package com.jfu.junkyardfollowup.services;

import com.jfu.junkyardfollowup.dtos.LocalDto;
import com.jfu.junkyardfollowup.enums.StatusLocal;
import com.jfu.junkyardfollowup.models.Local;
import com.jfu.junkyardfollowup.repositories.LocalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.*;

@Service
public class LocalService {
    @Autowired
    LocalRepository localRepository;

    public Optional<Local> findById(Long id){
        return localRepository.findById(id);
    }
    public List<Local> findAllByNomeContainingIgnoreCase(String nome){
        return localRepository.findAllByNomeContainingIgnoreCase(nome);
    }

    @Transactional
    public Local save(Local local){
        return localRepository.save(local);
    }

    @Transactional
    public String update(Local local, LocalDto localDto){
        if (local.getStatus() == StatusLocal.Ativo) { // eh ativo?
            List<Local> locais = localRepository.findByMaterialAndStatus(local.getMaterial(), StatusLocal.Ativo);
            if (locais.size() == 1) // nao existem outros ativos?
                if(localDto.getStatus() != StatusLocal.Ativo || !localDto.getMaterial().equals(local.getMaterial())) {
                    // alteracao muda ativo ou muda material?
                    return "falha ativo";
                }
        }
        if (local.getQuantidade().doubleValue() == 0 ||
                local.getMaterial().getId().equals(localDto.getMaterial().getId())) {
            Local localNovo = localDto.toLocal();
            localNovo.setId(local.getId());
            localNovo.setQuantidade(local.getQuantidade());
            localRepository.save(localNovo);
            return "sucesso";
        }
        return "falha quantidade";
    }

    @Transactional
    public String delete(Local local){
        if (local.getQuantidade().doubleValue() == 0){
            if (local.getStatus() == StatusLocal.Ativo) {
                List<Local> locais = localRepository.findByMaterialAndStatus(local.getMaterial(), StatusLocal.Ativo);
                if (locais.size() == 1) return "falha ativo";
            }
            localRepository.delete(local);
            return "sucesso";
        }
        return "falha quantidade";
    }

    public boolean mover(Local local1, Local local2){
        return true;
    }

    public List<Local> findAllByNomeIgnoreCase(String nome){
        return localRepository.findAllByNomeIgnoreCase(nome);
    }

    public List<Local> criarListaLocais(String searchKey){
        List<Local> locais = new ArrayList<>();
        Optional<Local> optional = null;
        if (!searchKey.isBlank()) {
            if (searchKey.matches("[+-]?\\d*(\\.\\d+)?")) {
                Long n = Long.parseLong(searchKey);
                optional = localRepository.findById(n);
                locais.add(optional.get());
            }
            Set<Local> localSet = new HashSet<>();
            for (StatusLocal s : StatusLocal.values()) {
                if (searchKey.equalsIgnoreCase(s.toString())) {
                    localSet.addAll(localRepository.findAllByStatus(StatusLocal.valueOf(StringUtils.capitalize(searchKey))));
                }
            }
            localSet.addAll(localRepository.findAllByNomeContainingIgnoreCase(searchKey));
            localSet.addAll(localRepository.findAllByMaterialNome(searchKey));
            if(optional != null) {
                localSet.remove(optional.get());
            }
            locais.addAll(localSet.stream().sorted().toList());
        } else {
            locais.addAll(localRepository.findAll());
        }
        return locais;
    }
}
