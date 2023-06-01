package be.xplore.pricescraper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import be.xplore.pricescraper.exceptions.RootDomainNotFoundException;
import be.xplore.pricescraper.repositories.ItemPriceRepository;
import be.xplore.pricescraper.repositories.ItemRepository;
import be.xplore.pricescraper.scrapers.detail.CarrefourBeScraper;
import be.xplore.pricescraper.services.ScraperServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = {ScraperServiceImpl.class, CarrefourBeScraper.class})
@ActiveProfiles("test")
class ScraperServiceIT {

  @MockBean
  ItemRepository itemRepository;
  @MockBean
  ItemPriceRepository itemPriceRepository;

  @Autowired
  private ScraperServiceImpl scraperService;

  @Test
  void getScraperRootDomain() {
    var baseUrl = "https://drive.carrefour.be/nl/";
    var identifier =
        "Dranken/Softdrinks/Sport--%26-gezonde-dranken/Aquarius%7CLemon-500-ml/p/06093663";
    var response = scraperService.getScraperRootDomain(baseUrl + identifier);
    assertThat(response).isNotNull();
    assertThat(response).isEqualTo("carrefour.be");
  }

  @Test
  void getScraperRootDomainShouldFail() {
    assertThatThrownBy(
        () -> scraperService.getScraperRootDomain("https://www.example.com")
    ).isInstanceOf(RootDomainNotFoundException.class);
  }
}
