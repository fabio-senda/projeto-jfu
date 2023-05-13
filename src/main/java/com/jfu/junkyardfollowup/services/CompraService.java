package com.jfu.junkyardfollowup.services;

import com.jfu.junkyardfollowup.dtos.CompraDto;
import com.jfu.junkyardfollowup.enums.StatusLocal;
import com.jfu.junkyardfollowup.models.Fornecimento;
import com.jfu.junkyardfollowup.models.Local;
import com.jfu.junkyardfollowup.models.Material;
import com.jfu.junkyardfollowup.models.RegistroDeCompra;
import com.jfu.junkyardfollowup.others.Recibo;
import com.jfu.junkyardfollowup.repositories.CompraRepository;
import com.jfu.junkyardfollowup.repositories.FornecedorRepository;
import com.jfu.junkyardfollowup.repositories.FornecimentoRepository;
import com.jfu.junkyardfollowup.repositories.LocalRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CompraService {
    @Autowired
    CompraRepository compraRepository;

    @Autowired
    FornecimentoRepository fornecimentoRepository;

    @Autowired
    LocalRepository localRepository;

    @Autowired
    MaterialService materialService;

    public RegistroDeCompra findById(Long id){
        return compraRepository.findById(id).orElse(null);
    }

    @Transactional
    public RegistroDeCompra save(RegistroDeCompra registroDeCompra, List<Fornecimento> itens){
        registroDeCompra = compraRepository.save(registroDeCompra);
        for (Fornecimento f: itens) {
            f.setRegistroDeCompra(registroDeCompra);
            f.getMaterial().setQuantidade(f.getMaterial().getQuantidade().add(f.getQuantidade()));
            materialService.save(f.getMaterial());
            List<Local> locais = localRepository.findByMaterialAndStatus(f.getMaterial(), StatusLocal.Ativo);
            locais.get(0).setQuantidade(f.getQuantidade());
            localRepository.save(locais.get(0));
            fornecimentoRepository.save(f);
        }
        return registroDeCompra;
    }

    @Transactional
    public void delete(RegistroDeCompra registroDeCompra){
        for (Fornecimento f : registroDeCompra.getFornecimentos()){
            fornecimentoRepository.delete(f);
        }
        compraRepository.delete(registroDeCompra);
    }

    public BigDecimal calcularTotal(RegistroDeCompra compra){
        BigDecimal total = new BigDecimal(0);
        for (Fornecimento item : compra.getFornecimentos()){
            total = total.add(item.getTotal());
        }
        return total;
    }

    public List<CompraDto> criarListaCompras(String searchKey){
        List<RegistroDeCompra> compras = new ArrayList<>();
        if(!searchKey.equals("") && searchKey.matches("[+-]?\\d*(\\.\\d+)?")){
            Long n = Long.parseLong(searchKey);
            Optional<RegistroDeCompra> optional = compraRepository.findById(n);
            List<Fornecimento> fornecimentos = fornecimentoRepository.findAllByMaterial_Id(n);
            if(optional.isPresent()){
                compras.add(optional.get());
            }
            for (Fornecimento f  : fornecimentos){
                optional = compraRepository.findById(f.getRegistroDeCompra().getId());
                if(optional.isPresent() && f.getRegistroDeCompra().getId() != f.getId()){
                    compras.add(optional.get());
                }
            }
        }else{
            compras.addAll(compraRepository.findAll());
        }
        List<CompraDto> compraDtos = compras.stream().map(CompraDto::fromRegistroDeCompra).toList();
        return compraDtos;
    }

    public List<Fornecimento> listaDeItens(RegistroDeCompra compra){
        return (List<Fornecimento>) fornecimentoRepository.findAllByCompraId(compra.getId());
    }

    public void gerarRecibo(Long id, HttpServletResponse response) throws IOException {
        RegistroDeCompra compra = findById(id);
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Compra#" + id + "_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        Recibo exporter = new Recibo(compra, listaDeItens(compra), calcularTotal(compra));
        exporter.export(response);
    }
}
