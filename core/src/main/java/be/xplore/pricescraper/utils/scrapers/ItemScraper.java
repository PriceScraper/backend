package be.xplore.pricescraper.utils.scrapers;

import be.xplore.pricescraper.dtos.ShopItem;
import java.io.IOException;
import java.util.Optional;

/**
 * Interface for scrapers that fetch item details.
 */
public interface ItemScraper {
  Optional<ShopItem> scrape(String url) throws IOException;

  String getBaseUrl();
}
