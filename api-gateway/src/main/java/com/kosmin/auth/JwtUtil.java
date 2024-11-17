package com.kosmin.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
  @Value("${spring.auth.jwt.key}")
  private String jwtSecret;

  private Algorithm getAlgorithm() {
    return Algorithm.HMAC256(jwtSecret);
  }

  public String generateToken(String username) {
    return JWT.create()
        .withSubject(username)
        .withIssuedAt(new Date())
        .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
        .sign(getAlgorithm());
  }

  public String extractUsername(String token) {
    return decodeToken(token).getSubject();
  }

  public boolean validateToken(String token, String username) {
    JWTVerifier verifier = JWT.require(getAlgorithm()).withSubject(username).build();
    verifier.verify(token);
    return true;
  }

  private DecodedJWT decodeToken(String token) {
    return JWT.decode(token);
  }
}
