package com.mitchmele.interstellarexchange.exchangeuser;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.microsoft.sqlserver.jdbc.StringUtils.isEmpty;

//@RequiredArgsConstructor
public class InterstellarUser extends User implements UserDetails {


    public InterstellarUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities); //empty quote for password?
    }

//    private final ExchangeUser exchangeUser;



    //extends User with super call?

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return AuthorityUtils.createAuthorityList("ADMIN", "USER");
//    }
//
//    @Override
//    public String getPassword() {
//        return exchangeUser.getPassword();
//    }
//
//    @Override
//    public String getUsername() {
//        return exchangeUser.getUsername();
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return exchangeUser.isActive();
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return exchangeUser.isActive();
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
}
