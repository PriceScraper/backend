package be.xplore.pricescraper.scrapers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.xplore.pricescraper.utils.scrapers.Scraper;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AhBeScraperTests {
  private final Scraper scraper;

  public AhBeScraperTests(@Qualifier("scraper-ah.be") Scraper scraper) {
    this.scraper = scraper;
  }

  @Test
  void constructed() {
    assertNotNull(scraper);
  }

  @Test
  void getItemResults() throws IOException {
    var response = scraper.scrape("wi445543/duvel-blond-4-pack");
    assertTrue(response.isPresent());
    assertNotNull(response.get());
    assertNotNull(response.get().title());
    assertTrue(response.get().price() > 0);
  }
}
