package be.xplore.pricescraper.scrapers.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Contains the URL of the dummy webshop.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "dummy-webshop")
public class LocalDummyConfig {
  private String url;
}
