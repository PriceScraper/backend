package be.xplore.pricescraper.scrapers.search;

import be.xplore.pricescraper.config.SearchScraperConfig;
import be.xplore.pricescraper.dtos.ItemScraperSearch;
import be.xplore.pricescraper.scrapers.SearchScraper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

/**
 * Scraper to discover new items from carrefour.
 */
@Slf4j
@Component("search-scraper-carrefour.be")
public class CarrefourBeScraper extends SearchScraper {
  /**
   * Constructor to get the baseUrl.
   */
  public CarrefourBeScraper(SearchScraperConfig config) {
    super("https://drive.carrefour.be/nl/", config);
  }

  @Override
  protected String urlToSearchQuery(String filter) {
    return getBaseUrl() + "search?q=" + filter;
  }

  @Override
  protected Optional<String> getUrlToItem(Element element) {
    var titleElement = element.getElementsByClass("select_item");
    if (hasArgumentFailed(titleElement, 2, "select_item")) {
      return Optional.empty();
    }
    return Optional.of(titleElement.get(1).attr("href").substring(4));
  }

  @Override
  protected Optional<String> getTitle(Element element) {
    var titleElement = element.getElementsByClass("select_item");
    if (hasArgumentFailed(titleElement, 2, "select_item")) {
      return Optional.empty();
    }
    return Optional.of(titleElement.get(1).attr("title"));
  }

  @Override
  protected Elements getItems(Document document) {
    return document.getElementsByClass("product-item");
  }

  /**
   * Scrape results.
   *
   * @return list of items with sufficient details.
   */
  @Override
  public List<ItemScraperSearch> scrape(String queryValue) throws IOException {
    var page = 0;
    List<ItemScraperSearch> foundItems = new ArrayList<ItemScraperSearch>();
    var sizeBefore = 0;
    do {
      sizeBefore = foundItems.size();
      var document = getDocument(urlToSearchQuery(queryValue) + "&page=" + page);
      var itemElements = getItems(document);
      itemElementsToDto(itemElements).forEach(e -> {
        if (entityNotInArray(foundItems, e)) {
          foundItems.add(e);
        }
      });
      log.debug("Found items for "
          + baseUrl
          + " (page "
          + page
          + "): "
          + itemElements.size()
          + ", new size: "
          + foundItems.size());
      page++;
    } while (stillFindingItems(sizeBefore, foundItems.size())
        && foundItems.size() < config.getItemLimit());
    if (foundItems.size() > config.getItemLimit()) {
      return foundItems.subList(0, config.getItemLimit() - 1);
    }
    return foundItems;
  }

  private boolean entityNotInArray(List<ItemScraperSearch> array, ItemScraperSearch entity) {
    return array.stream()
        .noneMatch(m -> m.url().equalsIgnoreCase(entity.url()));
  }

  private boolean stillFindingItems(int before, int after) {
    return after > before;
  }
}
