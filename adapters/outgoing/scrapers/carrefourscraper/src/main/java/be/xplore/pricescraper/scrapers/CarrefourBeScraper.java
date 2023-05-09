package be.xplore.pricescraper.scrapers;

import java.util.Optional;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

/**
 * Scraper for carrefour.be.
 */
@Component("scraper-carrefour.be")
public class CarrefourBeScraper extends Scraper {

  public CarrefourBeScraper() {
    super("https://drive.carrefour.be/nl/");
  }

  protected Optional<String> getItemTitle(Document document) {
    var titleSection = document.getElementsByClass("pdp-titleSection");
    if (hasArgumentFailed(titleSection, 2, "pdp-titleSection")) {
      return Optional.empty();
    }
    var title = titleSection.get(0).getElementsByClass("prod-title");
    if (hasArgumentFailed(title, 1, "prod-title")) {
      return Optional.empty();
    }
    return Optional.of(title.get(0).text());
  }

  protected Optional<Double> getItemPrice(Document document) {
    var priceSection = document.getElementsByClass("prod-price");
    if (hasArgumentFailed(priceSection, 2, "prod-price")) {
      return Optional.empty();
    }
    var price = priceSection.get(0).getElementsByClass("showRed");
    if (hasArgumentFailed(price, 1, "showRed")) {
      return Optional.empty();
    }

    var result = Double.parseDouble(price.get(0).text().replace("€", "").replace(",", "."));

    return Optional.of(result);
  }
}
