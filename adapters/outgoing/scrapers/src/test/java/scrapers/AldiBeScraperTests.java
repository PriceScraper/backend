package scrapers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.xplore.pricescraper.item.AldiBeScraper;
import be.xplore.pricescraper.item.Scraper;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = AldiBeScraper.class)
public class AldiBeScraperTests {
  private final Scraper scraper;

  public AldiBeScraperTests(@Qualifier("scraper-aldi.be") Scraper scraper) {
    this.scraper = scraper;
  }

  @Test
  void constructed() {
    assertNotNull(scraper);
  }

  @Test
  void getItemResults() throws IOException {
    var response = scraper.scrape("jupiler-15-st-3001592-1-0.article.html");
    assertTrue(response.isPresent());
    assertNotNull(response.get());
    assertNotNull(response.get().title());
    assertTrue(response.get().price() > 0);
  }
}
