package be.xplore.pricescraper.utils.scrapers;

import java.util.Optional;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

/**
 * Scraper for ah.be.
 */
@Component("scraper-ah.be")
public class AhBeScraper extends Scraper {
  public AhBeScraper() {
    super("https://www.ah.be/producten/product/");
  }

  protected Optional<String> getItemTitle(Document document) {
    var title = document.getElementsByAttributeValueContaining("role", "main");
    if (hasArgumentFailed(title, 1, "Attribute role with value main")) {
      return Optional.empty();
    }
    return Optional.of(title.get(0).attr("title"));
  }

  protected Optional<Double> getItemPrice(Document document) {
    var base = document.getElementsByClass("price-amount_integer__+e2XO");
    if (hasArgumentFailed(base, 1, "price-amount_integer__+e2XO")) {
      return Optional.empty();
    }
    var decimals = document.getElementsByClass("price-amount_fractional__kjJ7u");
    if (hasArgumentFailed(base, 1, "price-amount_fractional__kjJ7u")) {
      return Optional.empty();
    }
    var result = Double.parseDouble(base.get(0).text() + "." + decimals.get(0).text());
    return Optional.of(result);
  }
}
