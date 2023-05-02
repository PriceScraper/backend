package be.xplore.pricescraper.utils.security;

import be.xplore.pricescraper.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Generates, decodes & validates JWT tokens.
 */
@Component
@Slf4j
public class JwtProvider {
  private final JwtConfig jwtConfig;

  public JwtProvider(JwtConfig jwtConfig) {
    this.jwtConfig = jwtConfig;
  }

  /**
   * Generates a JSON Web Token.
   */
  public String generate(Long userId, String username, String avatar) {
    return Jwts.builder()
        .claim("id", userId)
        .claim("username", username)
        .claim("avatar", avatar)
        .setIssuedAt(Date.from(Instant.now()))
        .setExpiration(Date.from(Instant.now().plus(10, ChronoUnit.MINUTES)))
        .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret())
        .compact();
  }

  /**
   * Checks the validity of a token.
   */
  public boolean validateToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(jwtConfig.getSecret()).parseClaimsJws(authToken);
      return true;
    } catch (MalformedJwtException ex) {
      log.error("Invalid JWT token");
    } catch (ExpiredJwtException ex) {
      log.error("Expired JWT token");
    } catch (UnsupportedJwtException ex) {
      log.error("Unsupported JWT token");
    } catch (IllegalArgumentException ex) {
      log.error("JWT claims string is empty.");
    }
    return false;
  }

  /**
   * Finds the User Id from a JWT string.
   */
  public long getUserIdFromToken(String token) {
    var claims = getAllClaimsFromToken(token);
    return Long.parseLong(String.valueOf(claims.get("id")));
  }

  /**
   * Retrieves all claims from a JWT token.
   */
  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey(jwtConfig.getSecret()).parseClaimsJws(token).getBody();
  }
}
