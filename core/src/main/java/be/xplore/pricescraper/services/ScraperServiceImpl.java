package be.xplore.pricescraper.services;

import be.xplore.pricescraper.domain.shops.TrackedItem;
import be.xplore.pricescraper.dtos.ItemScraperSearch;
import be.xplore.pricescraper.dtos.ShopItem;
import be.xplore.pricescraper.exceptions.RootDomainNotFoundException;
import be.xplore.pricescraper.exceptions.ScraperNotFoundException;
import be.xplore.pricescraper.scrapers.ItemScraper;
import be.xplore.pricescraper.scrapers.SearchItemsScraper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
  private final Map<String, SearchItemsScraper> searchScrapers;

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
   * Scrape item by full itemUrl.
   */
  public Optional<ShopItem> scrapeFullUrl(String itemUrl) {
    try {
      var rootDomain = getScraperRootDomain(itemUrl);
      var scraper = getScraper(rootDomain);
      return scraper.scrape(itemUrl);
    } catch (Exception ioException) {
      log.error(ioException.getMessage());
      return Optional.empty();
    }
  }


  /**
   * Get root domain by full url.
   *
   * @param url full url
   * @return root domain
   */
  public String getScraperRootDomain(String url) {
    return scrapers.values().stream()
        .map(base -> {
          var b = base.getBaseUrl().toLowerCase();
          var split = b.split("/");
          if (Arrays.stream(split[2].split("")).filter(e -> e.equals(".")).count() > 1) {
            var firstDotIndex = split[2].indexOf(".");
            var value = split[2].substring(firstDotIndex + 1);
            log.debug("Found scraper root domain: " + value);
            return value;
          }
          log.debug("Found scraper root domain: " + split[2]);
          return split[2];
        })
        .filter(rootDomain -> url.toLowerCase().contains(rootDomain.toLowerCase()))
        .findAny().orElseThrow(RootDomainNotFoundException::new);
  }

  @Override
  public List<ItemScraperSearch> discoverItems(String query) {
    var items = new ArrayList<ItemScraperSearch>();
    for (var scraper : searchScrapers.values()) {
      try {
        items.addAll(scraper.scrape(query));
      } catch (IOException ioException) {
        log.error(ioException.getMessage());
      }
    }
    return items;
  }
}
