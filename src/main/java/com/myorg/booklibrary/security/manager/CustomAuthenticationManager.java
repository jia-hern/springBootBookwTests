package com.myorg.booklibrary.security.manager;

import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.myorg.booklibrary.entity.User;
import com.myorg.booklibrary.service.UserService;
import com.myorg.booklibrary.security.SecurityUtils;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {
    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User user = userService.getUser(authentication.getName());
        if (!bCryptPasswordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())) {
            throw new BadCredentialsException("You provided an incorrect password");
        }
        Set<GrantedAuthority> authorities = Set.of(SecurityUtils.convertToAuthority(user.getRole()));
        return new UsernamePasswordAuthenticationToken(authentication.getName(), user.getPassword(),
                authorities);
    }

}
