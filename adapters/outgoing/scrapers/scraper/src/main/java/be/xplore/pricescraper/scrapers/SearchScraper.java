package be.xplore.pricescraper.scrapers;

import be.xplore.pricescraper.dtos.ItemScraperSearch;
import be.xplore.pricescraper.utils.scrapers.SearchItemsScraper;
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

  /**
   * Constructor to get the baseUrl.
   */
  public SearchScraper(String baseUrl) {
    this.baseUrl = baseUrl;
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
