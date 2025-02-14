package dev.marco.chatbot_site.Security;// src/main/java/dev/marco/chatbot_site/Security/JwtAuthFilter.java

import dev.marco.chatbot_site.Service.JwtUtils;
import dev.marco.chatbot_site.Service.UserServices;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private UserServices userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        String jwt = null;
        String username = null;

        // Estrarre il token JWT dall'header Authorization
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
                logger.debug("Extracted username: {}", username);
            } catch (Exception e) {
                logger.error("Errore nell'estrazione del username dal token JWT", e);
            }
        }

        // Verifica se l'utente è già autenticato
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Carica i dettagli dell'utente
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            logger.debug("UserDetails loaded for username: {}", username);

            // Valida il token JWT
            if (jwtUtil.validateToken(jwt, userDetails)) {
                // Crea l'oggetto di autenticazione
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Imposta l'autenticazione nel contesto di sicurezza
                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.debug("Authentication set in SecurityContext for user: {}", username);
            } else {
                logger.debug("Token validation failed for user: {}", username);
            }
        }

        // Prosegui con la catena di filtri
        filterChain.doFilter(request, response);
    }
}
