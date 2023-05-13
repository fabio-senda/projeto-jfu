package com.jfu.junkyardfollowup.dtos;

import com.jfu.junkyardfollowup.enums.StatusLocal;
import com.jfu.junkyardfollowup.models.Local;
import com.jfu.junkyardfollowup.models.Material;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocalDto {
    @NotBlank(message = "Preencha o campo")
    @Size(max = 60)
    private String nome;

    @NotNull(message = "Preencha o campo")
    private Material material;

    @NotNull(message = "Preencha o campo")
    private StatusLocal status;

    public void fromLocal(Local local){
        this.setNome(local.getNome());
        this.setMaterial(local.getMaterial());
        this.setStatus(local.getStatus());
    }

    public Local toLocal(){
        Local local = new Local();
        local.setNome(this.nome);
        local.setMaterial(this.material);
        if(this.status == null){
            local.setStatus(StatusLocal.Ativo);
        } else{
            local.setStatus(this.status);
        }
        return local;
    }

}
