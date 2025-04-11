package com.example.eyerecognitionservice.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Filter để xử lý JWT token cho mỗi request
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String jwt = extractJwtFromRequest(request);

            if (StringUtils.hasText(jwt)) {
                try {
                    Claims claims = jwtUtil.extractAllClaims(jwt);
                    String username = claims.getSubject();

                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        String rolesString = claims.get("roles", String.class);
                        Collection<SimpleGrantedAuthority> authorities = null;

                        if (rolesString != null) {
                            authorities = Arrays.stream(rolesString.split(","))
                                    .map(SimpleGrantedAuthority::new)
                                    .collect(Collectors.toList());
                        }

                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(username, null, authorities);

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        log.debug("Set Authentication for user '{}' with roles: {}", username, rolesString);
                    }
                } catch (ExpiredJwtException e) {
                    log.error("JWT token is expired: {}", e.getMessage());
                    request.setAttribute("expired", e.getMessage());
                } catch (UnsupportedJwtException e) {
                    log.error("JWT token is unsupported: {}", e.getMessage());
                } catch (MalformedJwtException e) {
                    log.error("JWT token is invalid: {}", e.getMessage());
                } catch (IllegalArgumentException e) {
                    log.error("JWT claims string is empty: {}", e.getMessage());
                } catch (Exception e) {
                    log.error("Could not validate JWT token: {}", e.getMessage());
                }
            }
        } catch (Exception ex) {
            log.error("Failed to process JWT token: {}", ex.getMessage());
        }


        filterChain.doFilter(request, response);
    }


    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}