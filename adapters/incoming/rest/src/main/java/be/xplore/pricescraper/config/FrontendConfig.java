package be.xplore.pricescraper.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Contains the URL of the frontend, which the backend will redirect to after authenticating.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "frontend")
public class FrontendConfig {
  private String url;
}
