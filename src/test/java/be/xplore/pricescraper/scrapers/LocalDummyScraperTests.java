package be.xplore.pricescraper.scrapers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import be.xplore.pricescraper.utils.scrapers.Scraper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LocalDummyScraperTests {
  private final Scraper scraper;

  public LocalDummyScraperTests(@Qualifier("scraper-localhost:9000") Scraper scraper) {
    this.scraper = scraper;
  }

  @Test
  void constructed() {
    assertNotNull(scraper);
  }
}
