package be.xplore.pricescraper.scrapers.detail;

import be.xplore.pricescraper.scrapers.ItemDetailScraper;
import java.util.Arrays;
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

  @Override
  protected Optional<String> getItemIngredients(Document document) {
    var infoDiv = document.getElementById("gotoSection4");
    if (infoDiv == null) {
      return Optional.empty();
    }
    var sections = infoDiv.getElementsByClass("pdp-attribute-head-style");
    var ingredientsTitle =
        sections.stream().filter(e -> e.outerHtml().contains("Ingrediënten")).findFirst();
    if (ingredientsTitle.isEmpty()) {
      return Optional.empty();
    }
    var elPosition = sections.indexOf(ingredientsTitle.get());
    var inputFields = infoDiv.getElementsByTag("input");
    if (inputFields.size() <= elPosition) {
      return Optional.empty();
    }
    var field = inputFields.get(elPosition);
    var ingredientsWithTitle = field.attr("value")
        .replaceAll("<br>", ", ")
        .replaceAll("<[a-zA-Z ='\"-:,]+>", "")
        .replaceAll("</[a-z]+>", "")
        .replace("Ingrediënten:, ", "")
        .split(", ");
    var ingredientsWithoutTitle =
        Arrays.copyOfRange(ingredientsWithTitle, 1, ingredientsWithTitle.length);
    return Optional.of(String.join(", ", ingredientsWithoutTitle).trim());
  }
}
