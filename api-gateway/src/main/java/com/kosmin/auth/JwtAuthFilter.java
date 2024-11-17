package com.kosmin.auth;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  @Value("${spring.auth.jwt.key}")
  private String jwtSecret;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    String authHeader = request.getHeader("Authorization");

    String jsonResponse = "{\"errorMessage\": \"%s\"}";

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String token = authHeader.substring(7);
      try {
        String username = jwtUtil.extractUsername(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
          if (jwtUtil.validateToken(token, username)) {
            UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(username, null, null);
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
            if (!validateApiKey(request, response, jsonResponse)) return;
          }
        }
      } catch (JWTVerificationException e) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response
            .getWriter()
            .write(String.format(jsonResponse, "Invalid JWT Token: " + e.getMessage()));
        return;
      }
    }

    filterChain.doFilter(request, response);
  }

  private boolean validateApiKey(
      HttpServletRequest request, HttpServletResponse response, String jsonResponse)
      throws IOException {
    String apiKey = request.getHeader("x-api-key");

    if (apiKey != null) {
      boolean validKey = apiKey.equalsIgnoreCase(jwtSecret);
      if (!validKey) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(String.format(jsonResponse, "Invalid API Key"));
        return false;
      }
    } else {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      response.getWriter().write(String.format(jsonResponse, "Missing API Key"));
      return false;
    }
    return true;
  }
}
