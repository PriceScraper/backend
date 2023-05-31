package be.xplore.pricescraper.scrapers.search;


import be.xplore.pricescraper.config.SearchScraperConfig;
import be.xplore.pricescraper.dtos.ItemScraperSearch;
import be.xplore.pricescraper.model.SmallProduct;
import be.xplore.pricescraper.scrapers.SearchScraper;
import be.xplore.pricescraper.utils.ColruytHttpClient;
import be.xplore.pricescraper.utils.ColruytHttpHeaders;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

/**
 * Scraper to discover new items from Colruyt.
 */
@Slf4j
@Component("search-scraper-colruyt.be")
public class ColruytBeScraper extends SearchScraper {
  private final ColruytHttpClient httpClient;

  /**
   * Constructor to get the baseUrl.
   */
  public ColruytBeScraper(SearchScraperConfig config, ColruytHttpClient httpClient) {
    super("https://www.colruyt.be/nl/", config);
    this.httpClient = httpClient;
  }

  @Override
  protected String urlToSearchQuery(String queryValue) {
    return null;
  }

  @Override
  protected Optional<String> getUrlToItem(Element element) {
    return Optional.empty();
  }

  @Override
  protected Optional<String> getTitle(Element element) {
    return Optional.empty();
  }

  @Override
  protected Elements getItems(Document document) {
    return null;
  }

  @Override
  public List<ItemScraperSearch> scrape(String queryValue) {
    var response = httpClient.searchItems(ColruytHttpHeaders.get(), 1, queryValue.toLowerCase(), 25);
    log.info("Found items for " + baseUrl + ": " + response.getProducts().size());
    return response.getProducts().stream()
        .map(this::productToDto)
        .toList();
  }

  private ItemScraperSearch productToDto(SmallProduct p) {
    return new ItemScraperSearch(p.getLongName(), getProductUrl(p));
  }

  private String getProductUrl(SmallProduct p) {
    var uri = "https://apip.colruyt.be/gateway/ictmgmt.emarkecom.cgproductretrsvc.v2/v2/v2/nl/products";
    return String.format("%s/%s?clientCode=CLP&placeId=604", uri, p.getProductId());
  }
}
