package be.xplore.pricescraper.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import be.xplore.pricescraper.domain.shops.Item;
import be.xplore.pricescraper.domain.shops.ItemPrice;
import be.xplore.pricescraper.domain.shops.Shop;
import be.xplore.pricescraper.domain.shops.TrackedItem;
import be.xplore.pricescraper.domain.shops.UnitType;
import be.xplore.pricescraper.repositories.ItemRepository;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ItemServiceTests {

  private static final TrackedItem trackedItem1 =
      new TrackedItem("1_1", new Shop(), null);
  private static final TrackedItem trackedItem2 =
      new TrackedItem("1_2", new Shop(), null);
  private static final List<TrackedItem> trackedItems = List.of(trackedItem1, trackedItem2);
  private static final Item itemFromRepository =
      new Item(1, "Pizza hawai", "", 1, UnitType.KG, 0.5, "zout, peper", null, trackedItems);
  private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  private static final ItemPrice price1 =
      new ItemPrice(1, Instant.from(
          ZonedDateTime.of(LocalDateTime.from(formatter.parse("2023-04-21 12:00:00")),
              ZoneId.of("Europe/Brussels"))), 3.19, trackedItem1);
  private static final ItemPrice price2 =
      new ItemPrice(2, Instant.from(
          ZonedDateTime.of(LocalDateTime.from(formatter.parse("2023-04-22 12:00:00")),
              ZoneId.of("Europe/Brussels"))), 3.25, trackedItem1);
  private static final ItemPrice price3 =
      new ItemPrice(3, Instant.from(
          ZonedDateTime.of(LocalDateTime.from(formatter.parse("2023-04-21 12:00:00")),
              ZoneId.of("Europe/Brussels"))), 3.29, trackedItem2);
  private static final List<ItemPrice> itemPricesFromRepository = List.of(price1, price2, price3);
  @Mock
  ItemRepository itemRepository;
  @InjectMocks
  ItemServiceImpl itemService;

  @BeforeEach
  void setup() {
    when(itemRepository.findItemWithTrackedItemsById(1)).thenReturn(
        Optional.of(itemFromRepository));
    when(itemRepository.findLatestPricesForTrackedItems(
        itemFromRepository.getTrackedItems())).thenReturn(itemPricesFromRepository);
  }

  @Test
  void trackedItemsShouldContain1ItemPrice() {
    Item item =
        itemService.findItemWithTrackedItemsAndLatestPriceById(1);
    assertThat(item.getTrackedItems()).isNotEmpty();
    assertThat(item.getTrackedItems().get(0).getItemPrices()).hasSize(1);
  }

  @Test
  void itemPriceShouldBeTheMostRecent() {
    Item item =
        itemService.findItemWithTrackedItemsAndLatestPriceById(1);
    List<String> timestampsAsStrings = item.getTrackedItems().stream()
        .map(trackedItem -> trackedItem.getItemPrices().get(0).getTimestamp().toString())
        .collect(Collectors.toList());
    List<String> expectedTimestamps = List.of("2023-04-22T10:00:00Z", "2023-04-21T10:00:00Z");
    assertThat(timestampsAsStrings).usingRecursiveComparison().ignoringCollectionOrder()
        .isEqualTo(expectedTimestamps);
  }

}
