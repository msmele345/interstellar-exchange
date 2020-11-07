package com.mitchmele.interstellarexchange.security.service;

import com.mitchmele.interstellarexchange.exchangeuser.ExchangeUser;
import com.mitchmele.interstellarexchange.exchangeuser.InterstellarUser;
import com.mitchmele.interstellarexchange.exchangeuser.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Service
@RequiredArgsConstructor
public class ExchangeUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    //check what type the user has (admin, user) add column in db
    //this then goes to the auth service
    //do we need the auth service?
    //account registration form

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByUsername(username)
                .map(account -> {
                    boolean active = account.isActive();
                    return User.builder()
                          .username(account.getUsername())
                          .password("{noop}"+account.getPassword())
                          .accountExpired(!active)
                          .accountLocked(!active)
                          .authorities(AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER"))
                          .build();
                })
                .orElseThrow(
                        () -> new UsernameNotFoundException(String.format("username %s not found!", username))
                );
    }

    private List<GrantedAuthority> mapAuthorities(List<String> authRoles) {
        return authRoles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
