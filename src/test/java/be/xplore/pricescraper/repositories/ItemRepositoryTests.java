package be.xplore.pricescraper.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import be.xplore.pricescraper.domain.shops.Item;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql({"/sql/item_test_data.sql"})
@SpringBootTest
class ItemRepositoryTests {

  @Autowired
  ItemRepository itemRepository;

  @Test
  void trackedItemsShouldContain1ItemPrice() {
    List<Item> items =
        itemRepository.findItemsWithTrackedItemsAndLastPriceByNameLike("PIzza hawai");
    Item item = items.get(0);
    assertThat(item.getTrackedItems()).isNotEmpty();
    assertThat(item.getTrackedItems().get(0).getItemPrices()).hasSize(1);
  }

  @Test
  void itemPriceShouldBeTheMostRecent() {
    List<Item> items =
        itemRepository.findItemsWithTrackedItemsAndLastPriceByNameLike("PIzza hawai");
    Item item = items.get(0);
    List<String> timestampsAsStrings = item.getTrackedItems().stream()
        .map(trackedItem -> trackedItem.getItemPrices().get(0).getTimestamp().toString())
        .collect(Collectors.toList());
    List<String> expectedTimestamps = List.of("2023-04-22 12:00:00.0", "2023-04-21 12:00:00.0");
    assertThat(timestampsAsStrings).usingRecursiveComparison().ignoringCollectionOrder()
        .isEqualTo(expectedTimestamps);
  }

  @Test
  void itemShouldHave2TrackedItems() {
    List<Item> items =
        itemRepository.findItemsWithTrackedItemsAndLastPriceByNameLike("PIzza hawai");
    Item item = items.get(0);
    assertThat(item.getTrackedItems()).hasSize(2);
  }

  @Test
  void resultShouldHave2Items() {
    List<Item> items =
        itemRepository.findItemsWithTrackedItemsAndLastPriceByNameLike("PIzZa");
    assertThat(items).hasSize(2);
  }

}
