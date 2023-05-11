package be.xplore.pricescraper.scrapers;

import java.util.Optional;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

/**
 * Scraper for lidl.be.
 */
@Component("scraper-lidl.be")
public class LidlBeScraper extends ItemDetailScraper {

  public LidlBeScraper() {
    super("https://www.lidl.be/p/nl-BE/");
  }

  protected Optional<String> getItemTitle(Document document) {
    var titleSection = document.getElementsByClass("keyfacts__title");
    if (hasArgumentFailed(titleSection, 1, "Title section")) {
      return Optional.empty();
    }
    var title = titleSection.get(0).getElementsByTag("h1");
    if (hasArgumentFailed(title, 1, "h1")) {
      return Optional.empty();
    }
    return Optional.of(title.get(0).text());
  }

  @Override
  protected Optional<String> getItemImage(Document document) {
    return Optional.empty();
  }

  protected Optional<Double> getItemPrice(Document document) {
    var price = document.getElementsByClass("m-price__price");
    if (hasArgumentFailed(price, 1, "m-price__price")) {
      return Optional.empty();
    }
    var result = Double.parseDouble(price.get(0).text());
    return Optional.of(result);
  }
}
