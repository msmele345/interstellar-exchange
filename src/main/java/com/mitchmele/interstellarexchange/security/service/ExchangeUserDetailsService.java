package com.mitchmele.interstellarexchange.security.service;

import com.mitchmele.interstellarexchange.exchangeuser.InterstellarUser;
import com.mitchmele.interstellarexchange.exchangeuser.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

//@Service
@RequiredArgsConstructor
public class ExchangeUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    //check what type the user has (admin, user) add column in db
    //this then goes to the auth service

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> {
                    
                    return new InterstellarUser(user.getUsername(), user.getPassword(), emptyList());
                })
                .orElseThrow(() -> new UsernameNotFoundException(String.format("username %s not found!", username)));
    }


    private List<GrantedAuthority> mapAuthorities(List<String> authRoles) {
        return authRoles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}

