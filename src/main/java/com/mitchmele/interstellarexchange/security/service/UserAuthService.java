package com.mitchmele.interstellarexchange.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    private final ExchangeUserDetailsService exchangeUserDetailsService;


    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {

        //return user details from exUserDetailsService
        return null;
    }
}
