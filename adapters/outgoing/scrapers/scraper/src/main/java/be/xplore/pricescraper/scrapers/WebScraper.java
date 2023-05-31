package be.xplore.pricescraper.scrapers;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Abstract class with the most common implementations of a scraper.
 */
@Slf4j
public abstract class WebScraper {
  /**
   * Basic implementation to get the HTML tree of an item be.xplore.pricescraper.detail page.
   */
  protected Document getDocument(String url) throws IOException {
    return Jsoup.connect(url).get();
  }

  /**
   * We expect a certain amount of elements within the tree be.xplore.pricescraper.search.
   */
  protected boolean hasArgumentFailed(Elements elements, int expected, String identifier) {
    if (elements.size() != expected) {
      log.warn(
          "Expected " + expected + " element in " + identifier + ", had: " + elements.size());
    }
    return elements.size() != expected;
  }

  /**
   * We expect a certain amount of elements within the tree be.xplore.pricescraper.search.
   */
  protected boolean hasArgumentFailed(Element element, String identifier) {
    if (element == null) {
      log.warn(
          "Expected 1 element in " + identifier + ", had: 0");
    }
    return element == null;
  }
}
