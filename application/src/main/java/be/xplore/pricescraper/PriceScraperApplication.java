package be.xplore.pricescraper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entrypoint of the shop scraper application.
 */
@SpringBootApplication(scanBasePackages = "be.xplore.pricescraper")
public class PriceScraperApplication {

  public static void main(String[] args) {
    SpringApplication.run(PriceScraperApplication.class, args);
  }

}
