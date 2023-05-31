package be.xplore.pricescraper.scrapers.detail;

import be.xplore.pricescraper.dtos.ItemAmountDetails;
import be.xplore.pricescraper.dtos.ShopItem;
import be.xplore.pricescraper.scrapers.ItemDetailScraper;
import be.xplore.pricescraper.utils.AmountDetailsUtil;
import be.xplore.pricescraper.utils.ColruytHttpClient;
import be.xplore.pricescraper.utils.ColruytHttpHeaders;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

/**
 * Item scraper for colruyt.be.
 */
@Component("scraper-colruyt.be")
@Slf4j
public class ColruytBeScraper extends ItemDetailScraper {
  private final ColruytHttpClient httpClient;

  /**
   * Constructor to get the baseUrl.
   */
  public ColruytBeScraper(ColruytHttpClient httpClient) {
    super("https://www.colruyt.be/nl/");
    this.httpClient = httpClient;
  }

  @Override
  protected Optional<String> getItemTitle(Document document) {
    return Optional.empty();
  }

  @Override
  protected Optional<String> getItemImage(Document document) {
    return Optional.empty();
  }

  @Override
  protected Optional<String> getItemIngredients(Document document) {
    var divs = document.getElementsByClass("col-xs-12");
    for (org.jsoup.nodes.Element div : divs) {
      if (div.text().contains("Ingrediënten: ")) {
        return Optional.of(div
            .text()
            .replace("Ingrediënten: ", "")
            .replaceAll("Ingredienten", ""));
      }
    }
    return Optional.empty();
  }

  @Override
  protected Optional<Double> getItemPrice(Document document) {
    return Optional.empty();
  }

  @Override
  public Optional<ShopItem> scrape(String url) throws IOException {
    try {
      TimeUnit.SECONDS.sleep(7);
      var response = httpClient.findItem(ColruytHttpHeaders.get(), new URI(url));
      var document = getDocument(getProductDetailsUri(response.getTechnicalArticleNumber()));
      var nameToUse = response.getLongName() != null ? response.getLongName() :
          String.format("%s %s", response.getBrand(), response.getName());
      if(response.getPrice() == null) return Optional.empty();
      return Optional.of(
          new ShopItem(
              nameToUse,
              response.getPrice().getBasicPrice(),
              Optional.of(response.getFullImage()),
              AmountDetailsUtil.extractFromString(response.getContent()),
              getItemIngredients(document)
          )
      );
    } catch (URISyntaxException e) {
      log.error("Wrong uri for 2nd Colruyt scrape, msg: "+e.getMessage());
      throw new RuntimeException(e);
    } catch (InterruptedException e) {
      log.error("Failed to sleep before executing Colruyt scrape due to: "+e.getMessage());
      throw new RuntimeException(e);
    }
  }

  private String getProductDetailsUri(String technicalId) {
    var uri = "https://fic.colruytgroup.com/productinfo/nl/algc";
    return String.format("%s/%s", uri, technicalId);
  }
}
