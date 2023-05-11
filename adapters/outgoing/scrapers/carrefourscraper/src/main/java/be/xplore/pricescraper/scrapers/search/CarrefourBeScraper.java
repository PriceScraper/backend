package be.xplore.pricescraper.scrapers.search;

import be.xplore.pricescraper.scrapers.SearchScraper;
import java.util.Optional;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

/**
 * Scraper to discover new items from carrefour.
 */
@Component("search-scraper-carrefour.be")
public class CarrefourBeScraper extends SearchScraper {
  /**
   * Constructor to get the baseUrl.
   */
  public CarrefourBeScraper() {
    super("https://drive.carrefour.be/nl/");
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
}
