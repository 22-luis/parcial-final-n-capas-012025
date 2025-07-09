package com.uca.parcialfinalncapas.config;

import com.uca.parcialfinalncapas.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Extraer el header Authorization
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 2. Verificar si existe el header y si comienza con "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Si no hay token, continuar con el siguiente filtro
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraer el token JWT (remover "Bearer " del inicio)
        jwt = authHeader.substring(7);

        userEmail = jwtService.extractUsername(jwt);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 7. Cargar los detalles del usuario desde la base de datos
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 8. Validar el token contra los detalles del usuario
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // 9. Crear el token de autenticación de Spring Security
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,           // Principal (usuario)
                        null,                  // Credentials (null para JWT)
                        userDetails.getAuthorities() // Authorities (roles)
                );

                // 10. Establecer detalles adicionales del request
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 11. Establecer la autenticación en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 12. Continuar con el siguiente filtro en la cadena
        filterChain.doFilter(request, response);
    }
}
