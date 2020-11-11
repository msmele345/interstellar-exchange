package com.mitchmele.interstellarexchange.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthService implements AuthenticationUserDetailsService<Authentication> {

    private final ExchangeUserDetailsService exchangeUserDetailsService;

    //this is for some other way of identifying the user like an auth token

    @Override
    public UserDetails loadUserDetails(Authentication token) throws UsernameNotFoundException {
        String username = (String)token.getPrincipal();
        return exchangeUserDetailsService.loadUserByUsername(username);
    }
}
