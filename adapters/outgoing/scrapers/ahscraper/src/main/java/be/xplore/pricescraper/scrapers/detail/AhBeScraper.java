package be.xplore.pricescraper.scrapers.detail;

import be.xplore.pricescraper.dtos.ItemAmountDetails;
import be.xplore.pricescraper.scrapers.ItemDetailScraper;
import be.xplore.pricescraper.scrapers.utils.AmountDetailsUtil;
import java.util.Optional;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

/**
 * Scraper for ah.be.
 */
@Component("scraper-ah.be")
public class AhBeScraper extends ItemDetailScraper {
  private static final String PRICE_BASE_KEY = "price-amount_integer__+e2XO";
  private static final String PRICE_DEC_KEY = "price-amount_fractional__kjJ7u";

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

  @Override
  protected Optional<String> getItemIngredients(Document document) {
    var allSpanBlocks = document.getElementsByTag("span");
    var ingredientsBlock = allSpanBlocks.stream()
        .filter(e -> e.text().toLowerCase().startsWith("ingrediënten"))
        .findFirst();
    return ingredientsBlock.map(element -> element.text().substring(14).trim());
  }

  protected Optional<Double> getItemPrice(Document document) {
    var base = document.getElementsByClass(PRICE_BASE_KEY);
    if (base.size() == 1) {
      return getItemPriceFromRegularProduct(document);
    } else {
      return getItemPriceFromDiscountProduct(document);
    }
  }

  private Optional<Double> getItemPriceFromDiscountProduct(Document document) {
    var base = document.getElementsByClass(PRICE_BASE_KEY);
    if (hasArgumentFailed(base, 2, PRICE_BASE_KEY)) {
      return Optional.empty();
    }
    var decimals = document.getElementsByClass(PRICE_DEC_KEY);
    if (hasArgumentFailed(base, 2, PRICE_DEC_KEY)) {
      return Optional.empty();
    }
    var result = Double.parseDouble(base.get(1).text() + "." + decimals.get(1).text());
    return Optional.of(result);
  }

  private Optional<Double> getItemPriceFromRegularProduct(Document document) {
    var base = document.getElementsByClass(PRICE_BASE_KEY);
    if (hasArgumentFailed(base, 1, PRICE_BASE_KEY)) {
      return Optional.empty();
    }
    var decimals = document.getElementsByClass(PRICE_DEC_KEY);
    if (hasArgumentFailed(base, 1, PRICE_DEC_KEY)) {
      return Optional.empty();
    }
    var result = Double.parseDouble(base.get(0).text() + "." + decimals.get(0).text());
    return Optional.of(result);
  }

  @Override
  protected Optional<ItemAmountDetails> getItemAmountDetails(Document document) {
    var amountDetails =
        document.getElementsByAttributeValueContaining("data-testhook", "product-unit-size");
    if (hasArgumentFailed(amountDetails, 1, "data-testhook=product-unit-size")) {
      return Optional.empty();
    }
    return AmountDetailsUtil.extractFromString(amountDetails.text().toLowerCase());
  }
}
