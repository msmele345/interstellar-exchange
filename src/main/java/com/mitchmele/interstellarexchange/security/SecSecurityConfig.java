package com.mitchmele.interstellarexchange.security;

import com.mitchmele.interstellarexchange.security.service.ExchangeUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecSecurityConfig extends WebSecurityConfigurerAdapter {

    private final ExchangeUserDetailsService exchangeUserDetailsService;

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) {

        auth.authenticationProvider(getAuthenticationProvider());
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        http.formLogin()
                .and()
                .authorizeRequests().anyRequest()
                .fullyAuthenticated();
    }

    @Bean
    public DaoAuthenticationProvider getAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(exchangeUserDetailsService);
        return authenticationProvider;
    }
}