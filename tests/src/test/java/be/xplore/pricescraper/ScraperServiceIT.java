package be.xplore.pricescraper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.xplore.pricescraper.repositories.ItemPriceRepository;
import be.xplore.pricescraper.repositories.ItemRepository;
import be.xplore.pricescraper.scrapers.detail.CarrefourBeScraper;
import be.xplore.pricescraper.services.ScraperServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = {ScraperServiceImpl.class, CarrefourBeScraper.class})
class ScraperServiceIT {

  @MockBean
  ItemRepository itemRepository;
  @MockBean
  ItemPriceRepository itemPriceRepository;

  @Autowired
  private ScraperServiceImpl scraperService;

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
