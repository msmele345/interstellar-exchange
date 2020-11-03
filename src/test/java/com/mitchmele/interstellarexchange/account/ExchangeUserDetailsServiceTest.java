package com.mitchmele.interstellarexchange.account;

import com.mitchmele.interstellarexchange.exchangeuser.ExchangeUser;
import com.mitchmele.interstellarexchange.exchangeuser.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeUserDetailsServiceTest {

    @Mock
    private UserRepository exUserRepository;

    @InjectMocks
    private ExchangeUserDetailsService exchangeUserDetailsService;

    @Test
    void loadUserByUsername() {

        ExchangeUser user = ExchangeUser.builder()
                .username("user")
                .password("pass")
                .active(true)
                .build();

        when(exUserRepository.findByUsername(anyString()))
                .thenReturn(Optional.of(user));

        UserDetails actual = exchangeUserDetailsService.loadUserByUsername("user");

        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList("ADMIN", "USER");

        assertThat(actual.getAuthorities()).isEqualTo(authorityList);
    }

    @Test
    void loadByUsername_throwsUsernameNotFoundException_ifUserDoesntExist() {

        when(exUserRepository.findByUsername(anyString()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> exchangeUserDetailsService.loadUserByUsername("bad"))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}