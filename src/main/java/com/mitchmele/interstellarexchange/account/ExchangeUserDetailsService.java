package com.mitchmele.interstellarexchange.account;

import com.mitchmele.interstellarexchange.exchangeuser.ExchangeUser;
import com.mitchmele.interstellarexchange.exchangeuser.MyUserPrincipal;
import com.mitchmele.interstellarexchange.exchangeuser.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

//@Service
@RequiredArgsConstructor
public class ExchangeUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(MyUserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("username %s not found!", username)));
    }
}

