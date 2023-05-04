package be.xplore.pricescraper.scrapers;


import java.util.Optional;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

/**
 * Scraper for our dummy website running on port :9000.
 */
@Component("scraper-localhost:9000")
public class LocalDummyScraper extends ItemDetailScraper {
  public LocalDummyScraper() {
    super("http://localhost:9000/p/");
  }

  @Override
  protected Optional<String> getItemTitle(Document document) {
    var title = document.getElementsByClass("product-title");
    if (hasArgumentFailed(title, 2, "product-title")) {
      return Optional.empty();
    }
    return Optional.of(title.get(1).text());
  }

  @Override
  protected Optional<String> getItemImage(Document document) {
    return Optional.empty();
  }

  @Override
  protected Optional<Double> getItemPrice(Document document) {
    var price = document.getElementsByClass("product-price");
    if (hasArgumentFailed(price, 1, "product-price")) {
      return Optional.empty();
    }
    return Optional.of(Double.parseDouble(price.get(0).text()));
  }
}
