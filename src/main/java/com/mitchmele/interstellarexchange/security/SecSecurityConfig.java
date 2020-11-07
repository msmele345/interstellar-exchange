package com.mitchmele.interstellarexchange.security;

import com.mitchmele.interstellarexchange.security.service.ExchangeUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
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

//    @Autowired
//    @Qualifier("accountUserDetails")
//    UserDetailsService userDetailsService;

//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    //change to users - spring security may want that name

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder encoder =
                PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth
                .inMemoryAuthentication()
                .withUser("admin")
                .password(encoder.encode("admin1"))
                .roles("USER", "ADMIN");
//

//        auth.userDetailsService(userDetailsService)
//                .passwordEncoder(encoder);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {

//        http.authorizeRequests().anyRequest().hasAnyRole("ADMIN", "USER")
//                .and()
//                .formLogin()
//                .and()
//                .logout().permitAll().logoutSuccessUrl("/login")
//                .and()
//                .csrf().disable();


        //basic auth web based popup
        http
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();

//        http
//                .csrf().disable()
//                .authorizeRequests()
////                .antMatchers("/").hasAnyRole("USER", "ADMIN")
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .permitAll()
//                .and().logout().permitAll();

    }
}