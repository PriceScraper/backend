package be.xplore.pricescraper.scrapers;

import be.xplore.pricescraper.dtos.ShopItem;
import java.io.IOException;
import java.util.Optional;

public interface ItemScraper {
  Optional<ShopItem> scrape(String itemIdentifier) throws IOException;

  Optional<ShopItem> scrapeFromFullUrl(String url) throws IOException;

  String getBaseUrl();
}
