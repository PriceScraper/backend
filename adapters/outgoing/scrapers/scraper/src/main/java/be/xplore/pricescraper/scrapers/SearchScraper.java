package be.xplore.pricescraper.scrapers;

import be.xplore.pricescraper.config.SearchScraperConfig;
import be.xplore.pricescraper.dtos.ItemScraperSearch;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Abstract class containing base logic.
 */
@Slf4j
public abstract class SearchScraper extends WebScraper implements SearchItemsScraper {
  /**
   * Url to the website to scrape.
   */
  @Getter
  protected String baseUrl;
  protected SearchScraperConfig config;

  /**
   * Constructor to get the baseUrl.
   */
  protected SearchScraper(String baseUrl, SearchScraperConfig config) {
    this.baseUrl = baseUrl;
    this.config = config;
  }

  /**
   * Scrape results.
   *
   * @return list of items with sufficient details.
   */
  public List<ItemScraperSearch> scrape(String queryValue) throws IOException {
    var document = getDocument(urlToSearchQuery(queryValue));
    var itemElements = getItems(document);
    log.debug("Found items for " + baseUrl + ": " + itemElements.size());
    return itemElementsToDto(itemElements);
  }

  /**
   * Looping over items and putting them in dto.
   */
  protected List<ItemScraperSearch> itemElementsToDto(Elements itemElements) throws IOException {
    var foundItems = new ArrayList<ItemScraperSearch>();
    for (var itemElement : itemElements) {
      var title = getTitle(itemElement);
      var url = getUrlToItem(itemElement);
      if (title.isEmpty() || url.isEmpty()) {
        continue;
      }
      log.debug("Search scraper: Found item '" + title.get() + "' at " + url.get());
      foundItems.add(new ItemScraperSearch(title.get(), getBaseUrl() + url.get()));
    }
    if (foundItems.size() > config.getItemLimit()) {
      return foundItems.subList(0, config.getItemLimit() - 1);
    }
    return foundItems;
  }

  /**
   * Url to scrape results from.
   */
  protected abstract String urlToSearchQuery(String queryValue);

  /**
   * Get item url.
   */
  protected abstract Optional<String> getUrlToItem(Element element);

  /**
   * Get item title.
   */
  protected abstract Optional<String> getTitle(Element element);

  /**
   * Get list of elements.
   */
  protected abstract Elements getItems(Document document);
}
