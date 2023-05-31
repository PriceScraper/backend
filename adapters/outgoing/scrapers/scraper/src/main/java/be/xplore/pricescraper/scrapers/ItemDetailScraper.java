package be.xplore.pricescraper.scrapers;

import be.xplore.pricescraper.dtos.ItemAmountDetails;
import be.xplore.pricescraper.dtos.ShopItem;
import be.xplore.pricescraper.utils.AmountDetailsUtil;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;

/**
 * Abstract class with the most common implementations of scraping items.
 */
@Slf4j
public abstract class ItemDetailScraper extends WebScraper implements ItemScraper {
  /**
   * Url to the website to scrape.
   */
  @Getter
  protected String baseUrl;

  /**
   * Constructor to get the baseUrl.
   */
  protected ItemDetailScraper(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  /**
   * Code to fetch the item title.
   */
  protected abstract Optional<String> getItemTitle(Document document);

  /**
   * We expect a certain amount of elements within the tree search.
   */
  protected abstract Optional<String> getItemImage(Document document);

  /**
   * We expect a certain amount of elements within the tree search.
   */
  protected abstract Optional<String> getItemIngredients(Document document);

  /**
   * Parse nutrition value table.
   */
  protected abstract Optional<Map<String, String>> getNutritionValues(Document document);

  /**
   * Code to fetch the item price.
   */
  protected abstract Optional<Double> getItemPrice(Document document);

  /**
   * Code to fetch the item unit.
   */
  protected Optional<ItemAmountDetails> getItemAmountDetails(Document document) {
    var title = getItemTitle(document).orElse(null);
    if (title == null) {
      return Optional.empty();
    }
    return AmountDetailsUtil.extractFromString(title);
  }

  /**
   * Scrape a certain item by the full URL of the item.
   * The derived classes contain the functionality to find the attributes for the item.
   */
  public Optional<ShopItem> scrape(String url) throws IOException {
    var document = getDocument(url);
    var title = getItemTitle(document);
    var price = getItemPrice(document);
    if (title.isEmpty() || price.isEmpty()) {
      log.warn("Scraped "
          + baseUrl
          + ", and failed to fetch item by '"
          + url + "'. Title: "
          + title.orElse("")
          + ", Price: "
          + price.orElse(-1.0));
      return Optional.empty();
    }

    var image = getItemImage(document);
    var ingredients = getItemIngredients(document);
    var nutritionValues = getNutritionValues(document);
    var amountDetails = getItemAmountDetails(document);
    var item =
        new ShopItem(title.get(), price.get(), image, amountDetails, ingredients, nutritionValues);
    log.debug("Scraped " + baseUrl + " to find item: " + item);
    return Optional.of(item);
  }
}
