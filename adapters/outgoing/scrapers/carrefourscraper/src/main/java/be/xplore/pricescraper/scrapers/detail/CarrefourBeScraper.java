package be.xplore.pricescraper.scrapers.detail;

import be.xplore.pricescraper.scrapers.ItemDetailScraper;
import java.util.Optional;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

/**
 * Scraper for carrefour.be.
 */
@Component("scraper-carrefour.be")
public class CarrefourBeScraper extends ItemDetailScraper {

  public CarrefourBeScraper() {
    super("https://drive.carrefour.be/nl/");
  }

  protected Optional<String> getItemTitle(Document document) {
    var section = document.getElementById("mylists");
    if (hasArgumentFailed(section, "mylists")) {
      return Optional.empty();
    }
    assert section != null;
    return Optional.of(section.attr("data-item_name"));
  }

  protected Optional<Double> getItemPrice(Document document) {
    var section = document.getElementById("mylists");
    if (hasArgumentFailed(section, "mylists")) {
      return Optional.empty();
    }
    assert section != null;
    return Optional.of(Double.parseDouble(section.attr("data-price")));
  }

  @Override
  protected Optional<String> getItemImage(Document document) {
    var inputWithDetails = document.getElementsByClass("notification-details");
    if (inputWithDetails.size() == 0) {
      return Optional.empty();
    }
    return Optional.of(inputWithDetails.get(0).attr("data-src"));
  }
}
