package be.xplore.pricescraper.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.xplore.pricescraper.utils.scrapers.CarrefourBeScraper;
import be.xplore.pricescraper.utils.scrapers.Scraper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ScraperServiceTests {

  private ScraperService scraperService;

  @BeforeAll
  void setup() {
    Map<String, Scraper> scrapers = new HashMap<>();
    scrapers.put("scraper-carrefour.be", new CarrefourBeScraper());
    scraperService = new ScraperServiceImpl(scrapers);
  }

  @Test
  void getItemIdentifier() {
    var baseUrl = "https://drive.carrefour.be/nl/";
    var identifier =
        "Dranken/Softdrinks/Sport--%26-gezonde-dranken/Aquarius%7CLemon-500-ml/p/06093663";
    var response = scraperService.getItemIdentifier(baseUrl + identifier);
    assertTrue(response.isPresent());
    assertEquals(identifier.toLowerCase(), response.get().toLowerCase());
  }

  @Test
  void getItemIdentifierFail() {
    var baseUrl = "https://anUnknownDomain.be/nl/";
    var identifier =
        "Dranken/Softdrinks/Sport--%26-gezonde-dranken/Aquarius%7CLemon-500-ml/p/06093663";
    var response = scraperService.getItemIdentifier(baseUrl + identifier);
    assertTrue(response.isEmpty());
  }

  @Test
  void getScraperRootDomain() {
    var baseUrl = "https://drive.carrefour.be/nl/";
    var identifier =
        "Dranken/Softdrinks/Sport--%26-gezonde-dranken/Aquarius%7CLemon-500-ml/p/06093663";
    var response = scraperService.getScraperRootDomain(baseUrl + identifier);
    assertTrue(response.isPresent());
    assertEquals("carrefour.be", response.get());
  }
}
