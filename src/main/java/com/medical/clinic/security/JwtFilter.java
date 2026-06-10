package com.medical.clinic.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medical.clinic.model.ApiResponse;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {

            String path = request.getRequestURI();

            // ❗ CRITICAL FIX: skip auth endpoints
            if (path.startsWith("/api/auth/")
                    || path.startsWith("/api/public/")
                    || path.startsWith("/oauth2/")
                    || path.startsWith("/login/oauth2/")
                    || path.equals("/api/payments/payhere/notify")) {
                filterChain.doFilter(request, response);
                return;
            }

            String header = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (header == null || !header.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = header.substring(7);

            if (!jwtUtil.isValid(token)) {
                sendError(response, "Invalid or expired token");
                return;
            }

            String email = jwtUtil.getUserEmailFromToken(token);
            String role = jwtUtil.getRoleFromToken(token);
            System.out.println( "role"+role);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + role))
                    );

            SecurityContextHolder.getContext().setAuthentication(auth);

            request.setAttribute("email", email);
            request.setAttribute("role", role);

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            sendError(response, "Authentication failed: " + e.getMessage());
        }
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        ApiResponse apiResponse = new ApiResponse(message, true, false, null);
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}