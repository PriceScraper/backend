package be.xplore.pricescraper.integration;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import be.xplore.pricescraper.exceptions.ItemNotFoundException;
import be.xplore.pricescraper.exceptions.ScrapeItemException;
import be.xplore.pricescraper.repositories.ItemPriceRepository;
import be.xplore.pricescraper.repositories.ItemRepository;
import be.xplore.pricescraper.repositories.ShopRepository;
import be.xplore.pricescraper.repositories.TrackedItemRepository;
import be.xplore.pricescraper.scrapers.ItemScraper;
import be.xplore.pricescraper.scrapers.detail.CarrefourBeScraper;
import be.xplore.pricescraper.services.ItemServiceImpl;
import be.xplore.pricescraper.services.ScraperServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;


@Import(IntegrationConfig.class)
@SpringBootTest(classes = {ItemServiceImpl.class, ItemScraper.class,
    ScraperServiceImpl.class, CarrefourBeScraper.class})
@ActiveProfiles("test")
class ItemServiceIT {

  @Autowired
  private ItemRepository itemRepository;
  @Autowired
  private ItemPriceRepository itemPriceRepository;
  @Autowired
  private TrackedItemRepository trackedItemRepository;
  @Autowired
  private ShopRepository shopRepository;
  @Autowired
  private ItemServiceImpl itemService;

  @Test
  void trackItemFailure() {
    assertThatThrownBy(() -> itemService.addTrackedItem(
        "https://drive.carrefour.be/nl/itemdoesnotexist")).isInstanceOf(
        ScrapeItemException.class);
  }

  @Test
  void findItemShouldThrow() {
    assertThatThrownBy(
        () -> itemService.findItemWithTrackedItemsAndLatestPricesById(999999999)).isInstanceOf(
        ItemNotFoundException.class);
  }

}
