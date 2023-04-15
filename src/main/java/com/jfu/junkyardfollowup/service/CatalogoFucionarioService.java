package com.jfu.junkyardfollowup.service;

import com.jfu.junkyardfollowup.dto.LoginDto;
import com.jfu.junkyardfollowup.models.Funcionario;
import com.jfu.junkyardfollowup.repositories.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class CatalogoFucionarioService {
    @Autowired
    private FuncionarioRepository funcionarioRepository;
    private Funcionario funcionario;

    public Boolean autenticaLogin(LoginDto login){
        List<Funcionario> funcionarios = funcionarioRepository.findByUsuario(login.getUsuario());
        if(funcionarios.isEmpty()) {
            return false;
        }else {
            for (Funcionario funcionario: funcionarios) {
                if(login.getSenha().equals(funcionario.getSenha())){
                    this.funcionario = funcionario;
                    return true;
                };
            }
        }
        return false;
    }

    public Funcionario getfucionarioLogado(){
        return this.funcionario;
    }

}
