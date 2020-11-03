package com.mitchmele.interstellarexchange.exchangeuser;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@RequiredArgsConstructor
public class MyUserPrincipal implements UserDetails {

    private final ExchangeUser exchangeUser;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ADMIN", "USER");
    }

    @Override
    public String getPassword() {
        return exchangeUser.getPassword();
    }

    @Override
    public String getUsername() {
        return exchangeUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return exchangeUser.isActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return exchangeUser.isActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return exchangeUser.isActive();
    }

    @Override
    public boolean isEnabled() {
        return exchangeUser.isActive();
    }
}
