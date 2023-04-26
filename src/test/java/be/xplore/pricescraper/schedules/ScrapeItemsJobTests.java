package be.xplore.pricescraper.schedules;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ScrapeItemsJobTests {
  private final ScrapeItemsJob scrapeItemsJob;

  public ScrapeItemsJobTests() {
    this.scrapeItemsJob = new ScrapeItemsJob(null, null);
  }

  @Test
  void constructed() {
    assertNotNull(scrapeItemsJob);
  }
}
