package be.xplore.pricescraper;

import be.xplore.pricescraper.services.ItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Background service for scraping items.
 */
@Component
@AllArgsConstructor
@Slf4j
public class ScrapeItemsJob {
  private final ItemService itemService;

  /**
   * Running every 5000ms (5s)
   * Uses the last 5 tracked items every, and only scrapes them if lastAttempt was at least 1h ago.
   */
  @Scheduled(fixedDelay = 5000)
  @Profile("!test")
  private void scrapeOldestItems() {
    itemService.updateOldestTrackedItems(5);
  }
}
