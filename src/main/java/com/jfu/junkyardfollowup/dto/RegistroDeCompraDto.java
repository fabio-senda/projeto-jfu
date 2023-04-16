package com.jfu.junkyardfollowup.dto;

import com.jfu.junkyardfollowup.models.Fornecedor;
import com.jfu.junkyardfollowup.models.Fornecimento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RegistroDeCompraDto {
    private List<FornecimentoDto> fornecimentos = new ArrayList<>();
}