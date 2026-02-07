package com.max_orlov.music_albums_store.security;

import com.max_orlov.music_albums_store.exception.JwtAuthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

  private static final String JWT_COOKIE_NAME = "jwtToken";
  private static final String JWT_AUTHENTICATION_EXCEPTION_MESSAGE = "JWT token is expired or invalid";

  private final JwtTokenUtil jwtTokenUtil;

  @Override
  public void doFilterInternal(HttpServletRequest request,
                               @NonNull HttpServletResponse response,
                               @NonNull FilterChain filterChain) throws IOException, ServletException {
    Cookie[] cookies = request.getCookies();
    String token = null;
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals(JWT_COOKIE_NAME)) {
          token = cookie.getValue();
        }
      }
    }
    try {
      if (token != null) {
        UserDetails userDetails = jwtTokenUtil.getUserDetails(token);
        if (jwtTokenUtil.isTokenValid(token, userDetails)) {
          Authentication authentication = jwtTokenUtil.getAuthentication(token);
          if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
          }
        }
      }
      filterChain.doFilter(request, response);
    } catch (JwtAuthenticationException e) {
      SecurityContextHolder.clearContext();
      logger.error(e.getMessage(), e);
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      throw new JwtAuthenticationException(JWT_AUTHENTICATION_EXCEPTION_MESSAGE, HttpStatus.UNAUTHORIZED);
    }
  }

}
