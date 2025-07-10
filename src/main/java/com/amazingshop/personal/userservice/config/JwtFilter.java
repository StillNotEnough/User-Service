package com.amazingshop.personal.userservice.config;

import com.amazingshop.personal.userservice.security.JwtUtil;
import com.amazingshop.personal.userservice.services.PeopleDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final PeopleDetailsService peopleDetailsService;

    @Autowired
    public JwtFilter(JwtUtil jwtUtil, PeopleDetailsService peopleDetailsService) {
        this.jwtUtil = jwtUtil;
        this.peopleDetailsService = peopleDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
                String jwt = authHeader.substring(7);

                if (jwt.isEmpty()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token in Bearer Header");
                    return;
                }
                String username = jwtUtil.validateTokenAndRetrieveClaim(jwt);
                UserDetails userDetails = peopleDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                userDetails.getPassword(),
                                userDetails.getAuthorities());
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } finally {
            // Продвигаем запрос дальше по цепочке фильтров
            filterChain.doFilter(request, response);
        }
    }
}