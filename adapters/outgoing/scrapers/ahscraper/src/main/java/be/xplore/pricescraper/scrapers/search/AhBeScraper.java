package be.xplore.pricescraper.scrapers.search;

import be.xplore.pricescraper.scrapers.SearchScraper;
import java.util.Optional;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

/**
 * Scraper to discover new items from ah.
 */
@Component("search-scraper-ah.be")
public class AhBeScraper extends SearchScraper {
  /**
   * Constructor to get the baseUrl.
   */
  public AhBeScraper() {
    super("https://www.ah.be");
  }

  @Override
  protected String urlToSearchQuery(String queryValue) {
    return getBaseUrl() + "/zoeken?query=" + queryValue;
  }

  @Override
  protected Optional<String> getUrlToItem(Element element) {
    var link = element.getElementsByTag("a");
    if (hasArgumentFailed(link, 2, "a")) {
      return Optional.empty();
    }
    return Optional.of(link.get(0).attr("href"));
  }

  @Override
  protected Optional<String> getTitle(Element element) {
    var link = element.getElementsByTag("a");
    if (hasArgumentFailed(link, 2, "a")) {
      return Optional.empty();
    }
    return Optional.of(link.get(0).attr("title"));
  }

  @Override
  protected Elements getItems(Document document) {
    return document.getElementsByAttributeValue("data-testhook", "product-card");
  }
}
