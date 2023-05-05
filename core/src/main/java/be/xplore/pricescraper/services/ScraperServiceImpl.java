package be.xplore.pricescraper.services;

import be.xplore.pricescraper.domain.shops.TrackedItem;
import be.xplore.pricescraper.dtos.ShopItem;
import be.xplore.pricescraper.exceptions.ScraperNotFoundException;
import be.xplore.pricescraper.scrapers.ItemScraper;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service that executes all the business logic around retrieving data from items.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ScraperServiceImpl implements ScraperService {

  private final Map<String, ItemScraper> scrapers;

  /**
   * Scrape url by entity.
   */
  public Optional<ShopItem> scrapeTrackedItem(TrackedItem trackedItem) {
    try {
      var scraper = getScraper(trackedItem.getShop().getUrl());
      return scraper.scrape(trackedItem.getUrl());
    } catch (IOException ioException) {
      log.error(ioException.getMessage());
    }
    return Optional.empty();
  }

  /**
   * Get scraper instance.
   *
   * @param rootDomain root domain
   * @return scraper
   */
  private ItemScraper getScraper(String rootDomain) {
    var scraper = scrapers.getOrDefault("scraper-" + rootDomain, null);
    if (scraper == null) {
      throw new ScraperNotFoundException();
    }
    return scraper;
  }

  /**
   * Get root domain by full url.
   *
   * @param url full url
   * @return root domain
   */
  public Optional<String> getScraperRootDomain(String url) {
    return scrapers.keySet().stream()
        .map(key -> key.replace("scraper-", ""))
        .filter(base -> url.toLowerCase().contains(base.toLowerCase()))
        .findAny();
  }

  /**
   * Scrape item by full itemUrl.
   */
  public Optional<ShopItem> scrapeFullUrl(String itemUrl) {
    try {
      var rootDomain = getScraperRootDomain(itemUrl);
      if (rootDomain.isEmpty()) {
        throw new Exception("Failed to find root domain for tracked item.");
      }
      var scraper = getScraper(rootDomain.get());
      return scraper.scrapeFromFullUrl(itemUrl);
    } catch (Exception ioException) {
      log.error(ioException.getMessage());
      return Optional.empty();
    }
  }

  /**
   * Get Identifier area of full Item URL.
   */
  public Optional<String> getItemIdentifier(String url) {
    var root = getScraperRootDomain(url);
    if (root.isEmpty()) {
      return root;
    }
    var scraper = getScraper(root.get());
    return Optional.of(url.toLowerCase().replace(scraper.getBaseUrl().toLowerCase(), ""));
  }
}
