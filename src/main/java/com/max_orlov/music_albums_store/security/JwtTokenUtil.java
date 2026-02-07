package com.max_orlov.music_albums_store.security;

import com.max_orlov.music_albums_store.exception.JwtAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenUtil {

    private static final String JWT_AUTHENTICATION_EXCEPTION_MESSAGE = "JWT token is expired or invalid";
    private static final String USER_DETAILS_SERVICE_QUALIFIER = "userDetailsService";
    private static final String CLAIMS_KEY = "role";

    private final Logger logger = LogManager.getLogger();
    private final UserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long validityInMilliseconds;

    public JwtTokenUtil(@Qualifier(USER_DETAILS_SERVICE_QUALIFIER) UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String username, String role) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(CLAIMS_KEY, role);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validityInMilliseconds))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            return !Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration()
                    .before(new Date())
                    && getUsername(token).equals(userDetails.getUsername());
        } catch (JwtException | IllegalArgumentException e) {
            logger.error(JWT_AUTHENTICATION_EXCEPTION_MESSAGE);
            throw new JwtAuthenticationException(e.getMessage());
        }
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = getUserDetails(token);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public @NonNull UserDetails getUserDetails(String token) {
      return this.userDetailsService.loadUserByUsername(getUsername(token));
    }

    public String getUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}
