package be.xplore.pricescraper.scrapers.detail;

import be.xplore.pricescraper.scrapers.ItemDetailScraper;
import java.util.Optional;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

/**
 * Scraper for ah.be.
 */
@Component("scraper-ah.be")
public class AhBeScraper extends ItemDetailScraper {
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

  @Override
  protected Optional<String> getItemImage(Document document) {
    var img = document.getElementsByClass("lazy-image_image__o9P+M");
    if (hasArgumentFailed(img, 1, "lazy-image_image__o9P+M")) {
      return Optional.empty();
    }
    return Optional.of(img.get(0).attr("src"));
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
