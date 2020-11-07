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

    private final PasswordEncoder encoder =
            PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {

        PasswordEncoder encoder =
                PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        auth
//                .inMemoryAuthentication()
//                .withUser("admin")
//                .password(encoder.encode("admin1"))
//                .roles("USER", "ADMIN");
//

        auth.authenticationProvider(getAuthenticationProvider());
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        http.formLogin()
                .and()
                .authorizeRequests().anyRequest()
                .fullyAuthenticated();
//                .hasAnyRole("ROLE_ADMIN", "ROLE_USER");
//                .antMatchers("/").hasAnyRole("ROLE_USER", "ROLE_ADMIN")

//                .csrf().disable()
//        http
//                .authorizeRequests()
//                .anyRequest().fullyAuthenticated()
//                .and()
//                .httpBasic();
    }

    @Bean
    public DaoAuthenticationProvider getAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(exchangeUserDetailsService);
        authenticationProvider.setPasswordEncoder(encoder);
        return authenticationProvider;
    }
}