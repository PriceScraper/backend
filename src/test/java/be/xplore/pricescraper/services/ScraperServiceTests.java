package be.xplore.pricescraper.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ScraperServiceTests {
  @Autowired
  private ScraperService scraperService;

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
