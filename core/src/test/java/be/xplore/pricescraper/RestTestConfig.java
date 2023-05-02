package be.xplore.pricescraper;

import be.xplore.pricescraper.repositories.ItemPriceRepository;
import be.xplore.pricescraper.repositories.ShopRepository;
import be.xplore.pricescraper.repositories.TrackedItemRepository;
import be.xplore.pricescraper.services.ScraperService;
import be.xplore.pricescraper.services.ScraperServiceImpl;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

@TestConfiguration
@SpringBootConfiguration
@EnableAutoConfiguration
@ContextConfiguration(classes = {ScraperService.class, ScraperServiceImpl.class})
public class RestTestConfig {
  @MockBean
  private ItemPriceRepository itemPriceRepository;
  @MockBean
  private TrackedItemRepository trackedItemRepository;
  @MockBean
  private ShopRepository shopRepository;
}
