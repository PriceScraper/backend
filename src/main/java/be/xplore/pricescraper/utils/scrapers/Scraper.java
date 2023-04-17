package be.xplore.pricescraper.utils.scrapers;

import be.xplore.pricescraper.dto.ShopItem;
import java.io.IOException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Abstract class with the most common implementations of a scraper.
 */
@Slf4j
public abstract class Scraper {
  /**
   * Code to fetch the item title.
   */
  protected abstract Optional<String> getItemTitle(Document document);

  /**
   * Code to fetch the item price.
   */

  protected abstract Optional<Double> getItemPrice(Document document);

  /**
   * Url to the website to scrape.
   */
  protected String baseUrl;

  public Scraper(String baseUrl) {
    this.baseUrl = baseUrl;
  }


  /**
   * We expect a certain amount of elements within the tree search.
   */
  protected boolean hasArgumentFailed(Elements elements, int expected, String identifier) {
    if (elements.size() != expected) {
      System.out.println(
          "Expected " + expected + " element in " + identifier + ", had: " + elements.size());
    }
    return elements.size() != expected;
  }

  /**
   * Scape a certain item by combining the baseURL with the identifier.
   * The derived classes contain the functionality to find the attributes for the item.
   */
  public Optional<ShopItem> scrape(String itemIdentifier) throws IOException {
    var document = getDocument(itemIdentifier);
    var title = getItemTitle(document);
    var price = getItemPrice(document);
    if (title.isEmpty() || price.isEmpty()) {
      log.warn("Scraped "
          + baseUrl
          + ", and failed to fetch item by '"
          + itemIdentifier + "'. Title: "
          + title.orElse("")
          + ", Price: "
          + price.orElse(-1.0));
      return Optional.empty();
    }

    var item = new ShopItem(title.get(), price.get());
    log.info("Scraped " + baseUrl + " to find item: " + item);
    return Optional.of(item);
  }

  /**
   * Basic implementation to get the HTML tree of an item detail page.
   */
  protected Document getDocument(String itemIdentifier) throws IOException {
    return Jsoup.connect(baseUrl + itemIdentifier).get();
  }
}
