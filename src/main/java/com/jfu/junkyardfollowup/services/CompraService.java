package com.jfu.junkyardfollowup.services;

import com.jfu.junkyardfollowup.models.Fornecimento;
import com.jfu.junkyardfollowup.models.Material;
import com.jfu.junkyardfollowup.models.RegistroDeCompra;
import com.jfu.junkyardfollowup.repositories.CompraRepository;
import com.jfu.junkyardfollowup.repositories.FornecedorRepository;
import com.jfu.junkyardfollowup.repositories.FornecimentoRepository;
import com.jfu.junkyardfollowup.repositories.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CompraService {
    @Autowired
    CompraRepository compraRepository;

    @Autowired
    FornecimentoRepository fornecimentoRepository;

    @Autowired
    FornecedorRepository fornecedorRepository;

    @Autowired
    MaterialRepository materialRepository;

    public List<RegistroDeCompra> criarListaCompras(String searchKey){
        List<RegistroDeCompra> compras = new ArrayList<>();
        if(!searchKey.equals("") && searchKey.matches("[+-]?\\d*(\\.\\d+)?")){
            Long n = Long.parseLong(searchKey);
            Optional<RegistroDeCompra> optional = compraRepository.findById(n);

            List<Fornecimento> fornecimentos = fornecimentoRepository.findAllByMaterial_Id(n);
            if(optional.isPresent()){
                compras.add(optional.get());
            }
            if (fornecimentos != null && !fornecimentos.isEmpty()) {
                for (Fornecimento f  : fornecimentos){
                    optional = compraRepository.findById(f.getRegistroDeCompra().getId());
                    if(optional.isPresent()){
                        compras.add(optional.get());
                    }
                }
            }
        }else{
            compras.addAll(compraRepository.findAll());
        }
        return compras;
    }
}
