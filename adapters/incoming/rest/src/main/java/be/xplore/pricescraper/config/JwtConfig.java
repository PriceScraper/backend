package be.xplore.pricescraper.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Contains the secret signature of the JWT tokens.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
  private String secret;
}
