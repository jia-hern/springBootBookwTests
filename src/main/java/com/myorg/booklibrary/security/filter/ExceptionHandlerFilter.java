package com.myorg.booklibrary.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;
import com.auth0.jwt.exceptions.JWTVerificationException;

import com.myorg.booklibrary.exception.EntityNotFoundException;

public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (EntityNotFoundException e) {
            setStatusAndUseWriter(HttpServletResponse.SC_NOT_FOUND, "Username does not exist", response);
        } catch (JWTVerificationException e) {
            setStatusAndUseWriter(HttpServletResponse.SC_FORBIDDEN, "JWT not valid", response);
        } catch (RuntimeException e) {
            setStatusAndUseWriter(HttpServletResponse.SC_BAD_REQUEST, "Bad request", response);
        }
    }

    private void setStatusAndUseWriter(int status, String message,
            HttpServletResponse response) throws IOException {
        response.setStatus(status);
        response.getWriter().write(message);
        response.getWriter().flush();
    }

}
