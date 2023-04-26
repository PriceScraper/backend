package be.xplore.pricescraper.services;

import be.xplore.pricescraper.utils.scrapers.Scraper;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service that executes all the business logic around retrieving data from items.
 */
@Service
@AllArgsConstructor
public class ScraperService {
  private final Map<String, Scraper> scrapers;
}
