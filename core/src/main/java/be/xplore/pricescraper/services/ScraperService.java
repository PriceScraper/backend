package be.xplore.pricescraper.services;

import be.xplore.pricescraper.domain.shops.TrackedItem;
import be.xplore.pricescraper.dtos.ItemScraperSearch;
import be.xplore.pricescraper.dtos.ShopItem;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Service that executes all the business logic around retrieving data from items.
 */
@Service
public interface ScraperService {
  /**
   * Scrape url by entity.
   */
  Optional<ShopItem> scrapeTrackedItem(TrackedItem trackedItem);

  /**
   * Get root domain by full url.
   *
   * @param url full url
   * @return root domain
   */
  Optional<String> getScraperRootDomain(String url);

  /**
   * Scrape item by full itemUrl.
   */
  Optional<ShopItem> scrapeFullUrl(String itemUrl);

  /**
   * Get Identifier area of full Item URL.
   */
  Optional<String> getItemIdentifier(String url);

  /**
   * Get brief item info by query.
   */
  List<ItemScraperSearch> discoverItems(String query);
}
