package com.myorg.booklibrary.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.nio.charset.StandardCharsets;

import com.myorg.booklibrary.constants.SecurityConstants;

public class SecurityUtils {

    public static SimpleGrantedAuthority convertToAuthority(String role) {
        String formattedRole = role.startsWith(SecurityConstants.ROLE_PREFIX) ? role
                : SecurityConstants.ROLE_PREFIX + role;
        return new SimpleGrantedAuthority(formattedRole);
    }

    public static Claims getAllClaimsFromToken(String token) {
        Claims claims;
        if (token == null) {
            claims = null;
        }
        try {
            Key key = Keys.hmacShaKeyFor(SecurityConstants.SECRET_KEY.getBytes(StandardCharsets.UTF_8));

            claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            // https://github.com/jwtk/jjwt#jws-create-key
        } catch (JwtException e) {
            claims = null;
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    };
}
