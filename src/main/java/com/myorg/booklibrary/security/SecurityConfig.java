package com.myorg.booklibrary.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.myorg.booklibrary.constants.Constants;
import com.myorg.booklibrary.constants.SecurityConstants;
import com.myorg.booklibrary.security.filter.AuthenticationFilter;
import com.myorg.booklibrary.security.filter.ExceptionHandlerFilter;
import com.myorg.booklibrary.security.filter.JWTAuthorizationFilter;
import com.myorg.booklibrary.security.manager.CustomAuthenticationManager;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class SecurityConfig {
    private final CustomAuthenticationManager customAuthenticationManager;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(customAuthenticationManager);
        authenticationFilter.setFilterProcessesUrl("/user/authenticate");

        http.headers().frameOptions().disable()
                .and()
                .csrf().disable()
                .authorizeHttpRequests()
                .antMatchers("/h2/**").permitAll()
                .antMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .antMatchers(SecurityConstants.REGISTER_PATH, SecurityConstants.RESET_PATH).permitAll()
                .antMatchers("/admin/**").hasRole(Constants.admin)
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new ExceptionHandlerFilter(), AuthenticationFilter.class)
                .addFilter(authenticationFilter)
                .addFilterAfter(new JWTAuthorizationFilter(), AuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();

    }
}
