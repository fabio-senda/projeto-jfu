package com.jfu.junkyardfollowup.services;

import com.jfu.junkyardfollowup.models.Funcionario;
import com.jfu.junkyardfollowup.repositories.FuncionarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    FuncionarioRepository funcionarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Funcionario funcionario = funcionarioRepository.findByUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario nao encontrado, usuario: " + username));
        return new User(funcionario.getUsername(), funcionario.getPassword(),
                true, true, true, true,
                funcionario.getAuthorities());
    }
}
