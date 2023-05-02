package be.xplore.pricescraper;

import be.xplore.pricescraper.repositories.ItemPriceRepository;
import be.xplore.pricescraper.repositories.ItemRepository;
import be.xplore.pricescraper.repositories.TrackedItemRepository;
import be.xplore.pricescraper.services.ItemService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
public class RestTestConfig {
  @MockBean
  private ItemService itemService;
  @MockBean
  private ItemRepository itemRepository;
  @MockBean
  private ItemPriceRepository itemPriceRepository;
  @MockBean
  private TrackedItemRepository trackedItemRepository;
}
