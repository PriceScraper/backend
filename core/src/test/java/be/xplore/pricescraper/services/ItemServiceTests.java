package be.xplore.pricescraper.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.ItemPrice;
import be.xplore.pricescraper.domain.shops.ItemUnit;
import be.xplore.pricescraper.domain.shops.Shop;
import be.xplore.pricescraper.domain.shops.TrackedItem;
import be.xplore.pricescraper.repositories.ItemRepository;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTests {

  @Mock
  ItemRepository itemRepository;
  @InjectMocks
  ItemServiceImpl itemService;

  private static final TrackedItem trackedItem1 =
      new TrackedItem("1_1", new Shop(), null);
  private static final TrackedItem trackedItem2 =
      new TrackedItem("1_2", new Shop(), null);
  private static final List<TrackedItem> trackedItems = List.of(trackedItem1, trackedItem2);
  private static final Item itemFromRepository = new Item(1, "Pizza hawai", "", 1, new ItemUnit(
      ItemUnit.UnitType.KILOGRAMS, 320), "zout, peper", trackedItems);
  private static final ItemPrice price1 =
      new ItemPrice(1, Timestamp.valueOf("2023-04-20 12:00:00"), 3.19, trackedItem1);
  private static final ItemPrice price2 =
      new ItemPrice(2, Timestamp.valueOf("2023-04-22 12:00:00"), 3.25, trackedItem1);
  private static final ItemPrice price3 =
      new ItemPrice(3, Timestamp.valueOf("2023-04-21 12:00:00"), 3.29, trackedItem2);
  private static final List<ItemPrice> itemPricesFromRepository = List.of(price1, price2, price3);


  @BeforeEach
  void setup() {
    when(itemRepository.findItemWithTrackedItemsById(1)).thenReturn(itemFromRepository);
    when(itemRepository.findLatestPricesForTrackedItems(
        itemFromRepository.getTrackedItems())).thenReturn(itemPricesFromRepository);
  }

  @Test
  void trackedItemsShouldContain1ItemPrice() {
    Item item =
        itemService.findItemWithTrackedItemsAndLatestPricesById(1);
    assertThat(item.getTrackedItems()).isNotEmpty();
    assertThat(item.getTrackedItems().get(0).getItemPrices()).hasSize(1);
  }

  @Test
  void itemPriceShouldBeTheMostRecent() {
    Item item =
        itemService.findItemWithTrackedItemsAndLatestPricesById(1);
    List<String> timestampsAsStrings = item.getTrackedItems().stream()
        .map(trackedItem -> trackedItem.getItemPrices().get(0).getTimestamp().toString())
        .collect(Collectors.toList());
    List<String> expectedTimestamps = List.of("2023-04-22 12:00:00.0", "2023-04-21 12:00:00.0");
    assertThat(timestampsAsStrings).usingRecursiveComparison().ignoringCollectionOrder()
        .isEqualTo(expectedTimestamps);
  }

}
