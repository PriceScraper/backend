package be.xplore.pricescraper.scrapers.search;


import be.xplore.pricescraper.config.SearchScraperConfig;
import be.xplore.pricescraper.dtos.ItemScraperSearch;
import be.xplore.pricescraper.model.SmallProduct;
import be.xplore.pricescraper.scrapers.SearchScraper;
import be.xplore.pricescraper.utils.ColruytHttpClient;
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
    var response = httpClient.searchItems(getHeaders(), 1, queryValue.toLowerCase(), 25);
    log.info("Found items for " + baseUrl + ": " + response.getProducts().size());
    return response.getProducts().stream()
        .map(this::productToDto)
        .toList();
  }

  private ItemScraperSearch productToDto(SmallProduct p) {
    return new ItemScraperSearch(p.getLongName(), getProductUrl(p));
  }

  private String getProductUrl(SmallProduct p) {
    return String.format("https://apip.colruyt.be/gateway/ictmgmt.emarkecom.cgproductretrsvc.v2/v2/v2/nl/products/%s?clientCode=CLP&placeId=604", p.getProductId());
  }

  private HashMap<String, String> getHeaders() {
    var map = new HashMap<String, String>();
    map.put("X-CG-APIKey", "a8ylmv13-b285-4788-9e14-0f79b7ed2411");
    map.put("Cookie", "TS01461a64=016303f95536c8fa9c056cf7fb1dfcaa6ef14e125ce4877139ac4bf5dee9209a2981eef0df60e7dd0f0c04319ed958a28a3f384b3d; dtCookie=v_4_srv_5_sn_FF6256A6B98116C847CA404C43A58081_perc_100000_ol_0_mul_1_app-3Ab84fed97a8123cd5_0; TS019261f5=016303f955d3680475e13741dad867edf878f311b6fffaabb56c5ea5734fff774fd058bf0c75b97535c895681fde6fea0acf9a4335");
    map.put("Host", "apip.colruyt.be");
    map.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36 Edg/113.0.1774.57");
    return map;
  }
}
