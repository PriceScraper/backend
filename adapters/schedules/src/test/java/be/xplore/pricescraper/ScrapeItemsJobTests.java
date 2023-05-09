package be.xplore.pricescraper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ScrapeItemsJobTests {
  private final ScrapeItemsJob scrapeItemsJob;

  public ScrapeItemsJobTests() {
    this.scrapeItemsJob = new ScrapeItemsJob(null);
  }

  @Test
  void constructed() {
    Assertions.assertNotNull(scrapeItemsJob);
  }
}
