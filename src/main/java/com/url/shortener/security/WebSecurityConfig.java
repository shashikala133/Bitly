package com.url.shortener.security;

import com.url.shortener.security.jwt.JwtAuthenticationFilter;
import com.url.shortener.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class WebSecurityConfig {

    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
       DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
       authenticationProvider().setUserDetailsService(userDetailsService);
       authenticationProvider().setPasswordEncoder(passwordEncoder());
       return authenticationProvider();
    }

    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf(csrf-> {
            try {
                csrf.disable()
                        .authorizeHttpRequests(auth->auth
                                .requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/api/urls/**").authenticated()
                                .requestMatchers("/{shortUrl}").permitAll()
                                .anyRequest().authenticated());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
