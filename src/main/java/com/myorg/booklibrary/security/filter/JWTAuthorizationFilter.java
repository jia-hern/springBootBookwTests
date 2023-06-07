package com.myorg.booklibrary.security.filter;

import java.io.IOException;
import java.util.Set;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.HashSet;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import io.jsonwebtoken.Claims;

import com.myorg.booklibrary.constants.SecurityConstants;
import com.myorg.booklibrary.security.SecurityUtils;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader(SecurityConstants.AUTHORIZATION);
        if (header == null || !header.startsWith(SecurityConstants.BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.replace(SecurityConstants.BEARER, "");
        String username = JWT.require(
                Algorithm.HMAC512(SecurityConstants.SECRET_KEY))
                .build()
                .verify(token)
                .getSubject();
        Claims claims = SecurityUtils.getAllClaimsFromToken(token);
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (claims != null) {
            authorities = Arrays.stream(claims.get("roles").toString().split(","))
                    .map(SecurityUtils::convertToAuthority)
                    .collect(Collectors.toSet());
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
