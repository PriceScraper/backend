package be.xplore.pricescraper.scrapers.search;

import be.xplore.pricescraper.scrapers.SearchScraper;
import be.xplore.pricescraper.scrapers.config.LocalDummyConfig;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

/**
 * Discovery scraper for dummy web shop.
 */
@Component("search-scraper-localhost:9000")
@Slf4j
public class LocalDummyScraper extends SearchScraper {
  /**
   * Constructor to get the baseUrl of web shop.
   */
  public LocalDummyScraper(LocalDummyConfig config) {
    super(config.getUrl());
  }

  @Override
  protected String urlToSearchQuery(String queryValue) {
    return getBaseUrl() + "/zoeken?query=" + queryValue;
  }

  @Override
  protected Optional<String> getUrlToItem(Element element) {
    return Optional.of(element.getElementsByClass("card-link").get(0).attr("href"));
  }

  @Override
  protected Optional<String> getTitle(Element element) {
    return Optional.of(element.getElementsByClass("card-title").get(0).text());
  }

  @Override
  protected Elements getItems(Document document) {
    var items = document.getElementsByClass("card-body");
    log.debug("Found " + items.size() + " items at " + getBaseUrl());
    return items;
  }
}
