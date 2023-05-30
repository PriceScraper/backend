package be.xplore.pricescraper.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Config of the scrapers that discover new items.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "search.scraper")
public class SearchScraperConfig {
  private int itemLimit;
}
