package be.xplore.pricescraper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Entrypoint of the shop scraper application.
 */
@SpringBootApplication
@ConfigurationPropertiesScan("be.xplore.pricescraper.config")
public class PriceScraperApplication {

  public static void main(String[] args) {
    SpringApplication.run(PriceScraperApplication.class, args);
  }

}
