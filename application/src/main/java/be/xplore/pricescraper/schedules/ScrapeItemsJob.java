package be.xplore.pricescraper.schedules;

import be.xplore.pricescraper.domain.shops.TrackedItem;
import be.xplore.pricescraper.exceptions.ScraperNotFoundException;
import be.xplore.pricescraper.services.ItemService;
import be.xplore.pricescraper.services.ScraperService;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Background service for scraping items.
 */
@Component
@AllArgsConstructor
@Slf4j
public class ScrapeItemsJob {
  private final ScraperService scraperService;
  private final ItemService itemService;

  /**
   * Running every 5000ms (5s)
   * Uses the last 5 tracked items every, and only scrapes them if lastAttempt was at least 1h ago.
   */
  @Scheduled(fixedDelay = 5000)
  private void scrapeOldestItems() {
    log.info("started");
    var itemsToTrack = itemService.oldestTrackedItems(5);
    itemsToTrack
        .forEach(item -> {
          if (hasBeenScrapedRecently(item)) {
            log.info(
                "Last attempt: " + item.getLastAttempt() + ", skipping scrape of " + item.getUrl());
          } else {
            scrapeTrackedItem(item);
          }
        });
  }

  /**
   * Execute scrape of item.
   *
   * @param trackedItem item to scrape.
   */
  private void scrapeTrackedItem(TrackedItem trackedItem) {
    try {
      var start = Timestamp.from(Instant.now());
      var scraped = scraperService.scrapeTrackedItem(trackedItem);
      itemService.modifyTrackedItemPrice(trackedItem, scraped);
      var end = Timestamp.from(Instant.now());
      log.info("Last attempt: "
          + trackedItem.getLastAttempt()
          + ", took "
          + (end.getTime() - start.getTime())
          + "ms to scrape "
          + trackedItem.getUrl());
    } catch (ScraperNotFoundException scraperNotFoundException) {
      itemService.setLastAttemptToNow(trackedItem);
      log.warn("Failed to find scraper for tracked item " + trackedItem.getUrl());
    }
  }

  /**
   * Checks if the item has been scraped recently.
   *
   * @param trackedItem item to check.
   * @return true if it has been scraped recently.
   */
  private boolean hasBeenScrapedRecently(TrackedItem trackedItem) {
    return trackedItem.getLastAttempt() != null
        && !trackedItem.getLastAttempt().before(
        Timestamp.from(Instant.now().minus(1, ChronoUnit.HOURS)));
  }
}
