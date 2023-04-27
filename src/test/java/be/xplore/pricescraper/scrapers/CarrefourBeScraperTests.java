package be.xplore.pricescraper.scrapers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.xplore.pricescraper.utils.scrapers.Scraper;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CarrefourBeScraperTests {
  private final Scraper scraper;

  public CarrefourBeScraperTests(@Qualifier("scraper-carrefour.be") Scraper carrefourBeScraper) {
    this.scraper = carrefourBeScraper;
  }

  @Test
  void constructed() {
    assertNotNull(scraper);
  }

  @Test
  void getItemResults() throws IOException {
    var response = scraper.scrape(
        "Dranken/Bier/Pils/In-krat/Jupiler%7CBlond-Bier-Krat-24-x-25-cl/p/00165431");
    assertTrue(response.isPresent());
    assertNotNull(response.get());
    assertNotNull(response.get().title());
    assertTrue(response.get().price() > 0);
  }
}
