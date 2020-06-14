package com.lab.labbook.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final static String LOGIN_PROCESSING = "/login";
    private final static String LOGIN_SUCCESS = "/labbook";
    private final static String LOGOUT_SUCCESS = "http://localhost:8081/";

    private final MyUserDetailService userDetailService;

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {

        builder
                .userDetailsService(userDetailService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/register").permitAll()
                .anyRequest().hasAnyRole("USER", "MODERATOR", "ADMIN", "DEFAULT")
                .and()
                .formLogin().permitAll()
                .loginProcessingUrl(LOGIN_PROCESSING)
                .defaultSuccessUrl(LOGIN_SUCCESS)
                .and()
                .logout().permitAll().logoutSuccessUrl(LOGOUT_SUCCESS);    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/VAADIN/**",
                "/favicon.ico",
                "/robots.txt",
                "/manifest.webmanifest",
                "/sw.js",
                "/offline-page.html",
                "/icons/**",
                "/images/**",
                "/frontend/**",
                "/webjars/**"
        );
    }
}
