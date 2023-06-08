package be.xplore.pricescraper.scrapers.detail;


import be.xplore.pricescraper.scrapers.ItemDetailScraper;
import be.xplore.pricescraper.scrapers.config.LocalDummyConfig;
import java.util.Map;
import java.util.Optional;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

/**
 * Scraper for our dummy website running on port :9000.
 */
@Component("scraper-q91pg5kgq322k.eu-west-3.cs.amazonlightsail.com")
public class LocalDummyScraper extends ItemDetailScraper {
  public LocalDummyScraper(LocalDummyConfig config) {
    super(config.getUrl());
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
    return Optional.of(document.getElementsByClass("productImg").get(0).attr("src"));
  }

  @Override
  protected Optional<String> getItemIngredients(Document document) {
    var infoBlock = document.getElementsByClass("productDetailsInfo");
    if (hasArgumentFailed(infoBlock, 1, "productDetailsInfo")) {
      return Optional.empty();
    }
    var ingredientsBlock = infoBlock.get(0).getElementsByTag("div");
    if (hasArgumentFailed(ingredientsBlock, 2, "div")) {
      return Optional.empty();
    }
    var ingredients = ingredientsBlock.get(1).getElementsByTag("span");
    if (ingredients.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(String.join("", ingredients.stream().map(Element::text).toList()));

  }

  @Override
  protected Optional<Map<String, String>> getNutritionValues(Document document) {
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
