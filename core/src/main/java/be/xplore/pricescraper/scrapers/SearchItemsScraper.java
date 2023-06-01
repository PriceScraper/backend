package be.xplore.pricescraper.scrapers;

import be.xplore.pricescraper.dtos.ItemScraperSearch;
import java.io.IOException;
import java.util.List;

/**
 * Interface for scrapers that find new (untracked) items.
 */
public interface SearchItemsScraper {
  List<ItemScraperSearch> scrape(String queryValue) throws IOException;
}
