package scrapers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import be.xplore.pricescraper.item.LocalDummyScraper;
import be.xplore.pricescraper.item.Scraper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = LocalDummyScraper.class)
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
