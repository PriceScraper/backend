package be.xplore.pricescraper.integration;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import be.xplore.pricescraper.domain.shops.Shop;
import be.xplore.pricescraper.domain.shops.TrackedItem;
import be.xplore.pricescraper.exceptions.ItemNotFoundException;
import be.xplore.pricescraper.exceptions.TrackItemException;
import be.xplore.pricescraper.repositories.ItemPriceRepository;
import be.xplore.pricescraper.repositories.ItemRepository;
import be.xplore.pricescraper.repositories.ShopRepository;
import be.xplore.pricescraper.repositories.TrackedItemRepository;
import be.xplore.pricescraper.scrapers.CarrefourBeScraper;
import be.xplore.pricescraper.scrapers.ItemScraper;
import be.xplore.pricescraper.services.ItemServiceImpl;
import be.xplore.pricescraper.services.ScraperServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = {ItemServiceImpl.class, ItemScraper.class,
    ScraperServiceImpl.class, CarrefourBeScraper.class})
class ItemServiceIntegrationTests {
  @MockBean
  private ItemRepository itemRepository;
  @MockBean
  private ItemPriceRepository itemPriceRepository;
  @MockBean
  private TrackedItemRepository trackedItemRepository;
  @MockBean
  private ShopRepository shopRepository;
  @Autowired
  private ItemServiceImpl itemService;

  @BeforeEach
  void prepare() {
    given(shopRepository.findByUrl(anyString()))
        .willReturn(Optional.of(new Shop(1, "", "")));
    given(trackedItemRepository.save(any())).willReturn(new TrackedItem());
  }

  @Test
  void trackItemSuccess() {
    var response = itemService.addTrackedItem(
        "https://drive.carrefour.be/nl/Dranken/Softdrinks/Sport--%26-gezonde-dranken/Aquarius%7CLemon-500-ml/p/06093663");
    assertNotNull(response);

  }

  @Test
  void trackItemFailure() {
    assertThatThrownBy(() -> itemService.addTrackedItem(
        "https://drive.carrefour.be/nl/itemdoesnotexist")).isInstanceOf(
        TrackItemException.class);
  }

  @Test
  void findItemShouldThrow() {
    assertThatThrownBy(
        () -> itemService.findItemWithTrackedItemsAndLatestPricesById(5)).isInstanceOf(
        ItemNotFoundException.class);
  }

}
