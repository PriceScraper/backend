package be.xplore.pricescraper.scrapers;

import be.xplore.pricescraper.dtos.ShopItem;
import java.io.IOException;
import java.util.Optional;

/**
 * Interface for ItemScraper.
 */
public interface ItemScraper {
  Optional<ShopItem> scrape(String itemIdentifier) throws IOException;

  String getBaseUrl();
}
