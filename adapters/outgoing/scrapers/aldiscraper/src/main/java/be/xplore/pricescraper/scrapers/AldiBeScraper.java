package be.xplore.pricescraper.scrapers;

import java.util.Optional;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

/**
 * Scraper for aldi.be.
 */
@Component("scraper-aldi.be")
public class AldiBeScraper extends ItemDetailScraper {
  public AldiBeScraper() {
    super("https://www.aldi.be/nl/p/");
  }

  protected Optional<String> getItemTitle(Document document) {
    var titleSection = document.getElementsByClass("mod-article-intro__header-headline");
    if (hasArgumentFailed(titleSection, 1, "mod-article-intro__header-headline")) {
      return Optional.empty();
    }
    var title = titleSection.get(0).getElementsByTag("h1");
    if (hasArgumentFailed(title, 1, "h1")) {
      return Optional.empty();
    }
    return Optional.of(title.get(0).textNodes().get(1).text().strip());
  }

  @Override
  protected Optional<String> getItemImage(Document document) {
    return Optional.empty();
  }

  protected Optional<Double> getItemPrice(Document document) {
    var priceSection = document.getElementsByClass("price__main");
    if (hasArgumentFailed(priceSection, 1, "price__main")) {
      return Optional.empty();
    }
    var price = priceSection.get(0).getElementsByClass("price__wrapper");
    if (hasArgumentFailed(price, 1, "price_wrapper")) {
      return Optional.empty();
    }
    var result = Double.parseDouble(price.get(0).text());
    return Optional.of(result);
  }
}
